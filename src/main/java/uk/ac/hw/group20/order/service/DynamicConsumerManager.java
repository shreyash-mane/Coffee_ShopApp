package uk.ac.hw.group20.order.service;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.main.view.HomeGui;
import uk.ac.hw.group20.order.controller.OrderController;
import uk.ac.hw.group20.order.dto.ThreadTimerDto;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.report.Report;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.Server;

public class DynamicConsumerManager {
	private final OrderQueue orderQueue;
	private final ExecutorService consumerPool;
	private int currentConsumers = 0;
	private final int maxConsumers;
	private final int minConsumers;
	private final int threshold;
	private final HomeGui home;
	private ThreadTimerDto timerDto;
	private OrderController controller;

	public DynamicConsumerManager(OrderQueue orderQueue, LinkedList<Order> orders, HomeGui homeGui, int minConsumers,
			int maxConsumers, int threshold, ThreadTimerDto timerDto, OrderController controller) {
		this.orderQueue = orderQueue;
		this.minConsumers = minConsumers;
		this.maxConsumers = maxConsumers;
		this.threshold = threshold;
		this.consumerPool = Executors.newCachedThreadPool();
		this.home = homeGui;
		this.timerDto = timerDto;
		this.controller = controller;
	}
	
	public void setTimer(ThreadTimerDto timerDto) {
		this.timerDto = timerDto;
	}

	public void start() {
		// Add initial consumers up to the minimum
		for (int i = 0; i < minConsumers; i++) {
			addConsumer();
		}

		// Start monitoring thread to dynamically adjust consumers
		new Thread(() -> {
			while (true) {
				// Adjust consumers dynamically based on queue size
				adjustConsumers();

				try {
					Thread.sleep(1000); // Check every 1 seconds
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}


				if (orderQueue.isOrdersSavingFinished() && currentConsumers == 0) {
					Logger.logMessage(LogLevel.INFO, "All orders added. Shutting down consumer pool and Generating report");
					shutdown(); 
					
					LinkedList<Order> completedOrders = orderQueue.getCompleteOrder();
					Logger.logMessage(LogLevel.INFO, "Generating report before application exits. Number (Size) of Complete order: " + completedOrders.size());
					Report.getCompletedOrderReport(completedOrders, controller);
					break;
				}
			}
		}).start();
	}

	private synchronized void adjustConsumers() {
		
		int queueSize = orderQueue.getQueueSize() + orderQueue.getPrepareSaveQueueSize();
		//Logger.logMessage("Queue Size - " + queueSize + " Order - " + orderQueue.getQueueSize() + " Preparation - " + orderQueue.getPreparationQueueSize() + " Saving - " + orderQueue.getSavingQueueSize());

		// Add consumers if queue size is large
		//Logger.logMessage((queueSize > threshold) + " - " + (currentConsumers < maxConsumers) + " currentConsumers - " + currentConsumers + " maxConsumers - " + maxConsumers);
		if (queueSize > threshold && currentConsumers < maxConsumers) {
			addConsumer();
		}

		// Remove extra consumers if queue size is small
		//Logger.logMessage(LogLevel.INFO, "Here-----------------------" + (queueSize < threshold) + " - " + (currentConsumers > minConsumers) + " - " + (orderQueue.isOrdersSavingFinished()));
		if (queueSize < threshold && orderQueue.isOrdersSavingFinished()) {
			removeConsumer();
		}
	}
	
	private void addConsumer() {
		currentConsumers++;
		Server currentServer = getNextAvailableServer(currentConsumers);
		// Logger.logMessage("Pool Timers: " + timerDto);
		consumerPool.execute(new OrderConsumer(orderQueue, home, currentServer, null, null, this));
	}

	private void removeConsumer() {
		currentConsumers--;
		Logger.logMessage(LogLevel.INFO, "Removing a consumer. Active consumers: " + currentConsumers);
	}
	
	public void shutdown() {
		// Shutdown only after all tasks are completed
		if (orderQueue.isOrdersFinished() && orderQueue.isOrdersSavingFinished() && !consumerPool.isShutdown() && !consumerPool.isTerminated()) {
			Logger.logMessage(LogLevel.INFO, "Shutting down the pool " + consumerPool);
			consumerPool.shutdown();
		}
	}

	private Server getNextAvailableServer(int currentConsumers) {
		switch (currentConsumers) {
		case 1:
			return Server.SERVER_ONE;
		case 2:
			return Server.SERVER_TWO;
		case 3:
			return Server.SERVER_THREE;
		case 4:
			return Server.SERVER_FOUR;
		default:
			return Server.SERVER_ONE;
		}
	}
	
	public int getTimerForServer(Server server) {
	    switch (server) {
	        case SERVER_ONE: return timerDto.getServer1Timer();
	        case SERVER_TWO: return timerDto.getServer2Timer();
	        case SERVER_THREE: return timerDto.getServer3Timer();
	        default: return 2000;
	    }
	}
}