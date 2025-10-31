package uk.ac.hw.group20.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.interfaces.Observer;
import uk.ac.hw.group20.order.interfaces.Subject;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.OrderStatus;
import uk.ac.hw.group20.utils.Server;

public class OrderQueue implements Subject {
	private static OrderQueue instance;
	private final Queue<Order> mainQueue = new LinkedList<>();
	private final Queue<Order> priorityQueue = new LinkedList<>();
	private final Queue<Order> preparationQueue = new LinkedList<>();
	private final Queue<Order> servingQueue = new LinkedList<>();
	private final LinkedList<Order> completeOrder = new LinkedList<>();
	private boolean ordersFinished = false;
	private boolean orderPreparationFinished = false;
	private boolean ordersSavingFinished = false;
	private final List<Observer> observers = new ArrayList<>();

	private OrderQueue() {
	}

	public static synchronized OrderQueue getInstance() {
		if (instance == null) {
			instance = new OrderQueue();
		}
		return instance;
	}

	public synchronized void addOrder(Order order, int maxQueueSize) throws InterruptedException {
		while (mainQueue.size() + priorityQueue.size() == maxQueueSize) {
			Logger.logMessage(LogLevel.INFO, "Queue is full (" + maxQueueSize + "), Order Producer is waiting...");
			wait();
		}

		// Update order time
		order.setDateCreated(new Date());
		if (order.isOnlineOrder()) {
			priorityQueue.add(order);
			Logger.logMessage(LogLevel.INFO, "New Online Order Added: " + order.getOrderId() + " (Priority)");
		} else {
			mainQueue.add(order);
			Logger.logMessage(LogLevel.INFO, "New Order Added: " + order.getOrderId());
		}

		// Notify views to update
		notifyObservers(this);
		notifyAll();
	}

	// Get the order to serve by saving staff
	public synchronized Order getOrder() throws InterruptedException {

		while (priorityQueue.isEmpty() && mainQueue.isEmpty()) {
			if (ordersFinished) {
				return null;
			}
			wait();
		}

		// Poll the priority queue first
		Order order;

		if (!priorityQueue.isEmpty()) {
			order = priorityQueue.poll();
		} else {
			order = mainQueue.poll();
		}

		// Check whether order contains food item to sent to kitchen otherwise send to
		// saving queue
		if (order.hasFoodItems()) {
			// Move the order to preparation queue a.k.a barista
			Logger.logMessage(LogLevel.INFO, "Order " + order.getOrderId() + " has food item(s). Adding to barista");
			preparationQueue.add(order);
		} else {
			Logger.logMessage(LogLevel.INFO, "Order " + order.getOrderId() + " has no food item(s). Adding to saving queue...");
			addToServingQueue(order);
		}

		notifyAll();

		return order;
	}

	// Get order from preparation queue
	public synchronized Order getPreparationOrder() throws InterruptedException {
		while (preparationQueue.isEmpty()) {
			if (orderPreparationFinished) {
				return null;
			}
			wait();
		}

		Order order = preparationQueue.poll();
		notifyAll();
		return order;
	}

	// Add order to a saving queue
	public synchronized void addToServingQueue(Order order) {
		if (!order.isSaved())
			order.setPrepared(true);
			servingQueue.add(order);
		Logger.logMessage(LogLevel.INFO, "Order " + order.getOrderId() + " is ready to be served!");
		notifyAll();
	}

	public synchronized Order getOrderToProcess(Server server) throws InterruptedException {
		while (servingQueue.isEmpty() && getQueueSize() == 0 && getPrepareSaveQueueSize() == 0) {
			if (ordersFinished) {
				checkAndMarkServingFinished();
				markPreparationFinished();
				return null;
			}
			Logger.logMessage(LogLevel.INFO, "Waiting for orders to be added...");
			wait();
		}

		// Give priority to saving queue over the processing queues.
		if (!servingQueue.isEmpty()) {
			Order order = servingQueue.poll();
			Logger.logMessage(LogLevel.INFO, "Processing Order from Serving Queue: " + order.getOrderId());
			completeOrder(order, server);
			return order;
		}

		// Notify views to update
		notifyObservers(this);

		return getOrder();
	}

	// Save the to completed order list and save to file
	private void completeOrder(Order order, Server server) {
		order.setStatus(OrderStatus.COMPLETED);
		order.setDateSaved(new Date());
		order.setSaved(true);
		order.setSavedBy(server);
		OrderLoader.archiveOrder(order);
		completeOrder.add(order);
	}

	// Return the completed orders
	public LinkedList<Order> getCompleteOrder() {
		return completeOrder;
	}

	private synchronized void checkAndMarkServingFinished() {
		while (!preparationQueue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		if (ordersFinished && preparationQueue.isEmpty() && servingQueue.isEmpty() && !ordersSavingFinished) {
			ordersSavingFinished = true;
			Logger.logMessage(LogLevel.INFO, "All orders have been served. Marking serving as completed.");
			notifyAll();
		}
	}

	public synchronized void markOrdersFinished() {
		Logger.logMessage(LogLevel.INFO, "All orders from file completed added, Marking order queue completed");
		ordersFinished = true;
		notifyAll();
	}

	public synchronized void markPreparationFinished() {
		// Logger.logMessage("-->" + ordersFinished + " - " + preparationQueue.size() +
		// " - " + !orderPreparationFinished );
		if (ordersFinished && !orderPreparationFinished) {
			Logger.logMessage(LogLevel.INFO, "All orders from file completed, Marking order queue completed");
			orderPreparationFinished = true;
			notifyAll();
		}

	}

	public synchronized boolean isOrdersFinished() {
		boolean isFinished = ordersFinished && mainQueue.isEmpty() && priorityQueue.isEmpty();

		return isFinished;
	}

	public synchronized boolean isOrdersSavingFinished() {
		boolean isFinished = isOrdersFinished() && ordersSavingFinished && preparationQueue.isEmpty()
				&& servingQueue.isEmpty();

		return isFinished;
	}

	public synchronized boolean isQueueOperationCompleted() {
		return isOrdersFinished() && isOrdersSavingFinished();
	}

	public synchronized int getQueueSize() {
		int queueSize = mainQueue.size() + priorityQueue.size();

		return queueSize;
	}

	public synchronized int getPreparationQueueSize() {
		int queueSize = preparationQueue.size();

		return queueSize;
	}

	public synchronized int getSavingQueueSize() {
		int queueSize = servingQueue.size();

		return queueSize;
	}

	public synchronized int getPrepareSaveQueueSize() {
		int queueSize = preparationQueue.size() + servingQueue.size();

		return queueSize;
	}

	public synchronized LinkedList<Order> getCurrentQueue() {
		LinkedList<Order> combinedQueue = new LinkedList<>(priorityQueue);
		combinedQueue.addAll(mainQueue);

		return combinedQueue;
	}

	public synchronized LinkedList<Order> getCurrentSavingQueue() {
		LinkedList<Order> combinedQueue = new LinkedList<>(servingQueue);

		return combinedQueue;
	}

	@Override
	public void registerObserver(Observer observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}

	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);

	}

	@Override
	public void notifyObservers(OrderQueue orderQueue) {
		for (Observer observer : observers) {
			observer.update(orderQueue);
		}
	}
}