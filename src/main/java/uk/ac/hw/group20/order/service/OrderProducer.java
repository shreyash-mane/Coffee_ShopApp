package uk.ac.hw.group20.order.service;

import java.util.LinkedList;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.utils.LogLevel;

public class OrderProducer implements Runnable {

	private final OrderQueue orderQueue;
	private final LinkedList<Order> loadedOrders;
	private int maxQueueSize;
	private volatile int timer;

	public OrderProducer(OrderQueue orderQueue, LinkedList<Order> loadedOrders, int timer, int maxQueueSize) {
		this.orderQueue = orderQueue;
		this.loadedOrders = loadedOrders;
		this.timer = timer;
		this.maxQueueSize = maxQueueSize;
	}
	
	public void setTimer(int newTimer) {
		Logger.logMessage(LogLevel.INFO, "Updating Producer Sleep Time to: " + newTimer);
		this.timer = newTimer;
	}

	@Override
	public void run() {
	    if (loadedOrders == null || loadedOrders.isEmpty()) {
	        Logger.logMessage(LogLevel.INFO, "No orders to add in the queue.");
	        orderQueue.markOrdersFinished();
	        return;
	    }
	    
	    try {
	        for (Order order : this.loadedOrders) {
	            orderQueue.addOrder(order, maxQueueSize);
	            Logger.logMessage(LogLevel.INFO, "Next order will be added in (Producer Sleep Time): " + this.timer);
	            Thread.sleep(timer);
	        }
	    } catch (InterruptedException e) {
	        Logger.logMessage(LogLevel.INFO, "Order processing thread was interrupted.");
	        Thread.currentThread().interrupt();
	    } finally {
	        orderQueue.markOrdersFinished();
	    }
	}
}
