package uk.ac.hw.group20.order.service;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.main.view.HomeGui;
import uk.ac.hw.group20.order.dto.ServerInfoDto;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.Server;

public class KitchenStaff implements Runnable {
	private final OrderQueue orderQueue;
	private final String staffName;
	private ServerInfoDto dto;
	private int timer;
	private final HomeGui home;
	private final Server server;
	private final String userId;

	public KitchenStaff(OrderQueue orderQueue, String staffName, ServerInfoDto dto, HomeGui home, int timer,
			Server server, String userId) {
		this.orderQueue = orderQueue;
		this.staffName = staffName;
		this.dto = dto;
		this.timer = timer;
		this.home = home;
		this.server = server;
		this.userId = userId;
	}

	public void setTimer(int newTimer) {
		Logger.logMessage(LogLevel.INFO, "Updating Barista Sleep Time to: " + newTimer);
		this.timer = newTimer;
	}

	public void run() {
		try {
			while (true) {
				Order order;
				synchronized (orderQueue) {
					order = orderQueue.getPreparationOrder();
					if (order == null) {
						Logger.logMessage(LogLevel.INFO, staffName + " completed preparing orders ");
						home.updateOrderDisplay(orderQueue.getCurrentQueue(), null, userId, server);
						break;
					}
				}

				Logger.logMessage(LogLevel.INFO, staffName + " is preparing Order: " + order.getOrderId());
				dto.setLblServer4(order.formatOrder("Preparing "));
				home.updateOrderDisplay(orderQueue.getCurrentQueue(), dto, userId, server);
				Logger.logMessage(LogLevel.INFO, "Next order will be prepared in (Barista Sleep Time): " + this.timer);
				Thread.sleep(timer);

				orderQueue.addToServingQueue(order);
				Logger.logMessage(LogLevel.INFO, staffName + " completed Order: " + order.getOrderId());
			}

			Logger.logMessage(LogLevel.INFO, "Preparation of order completed");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}
}
