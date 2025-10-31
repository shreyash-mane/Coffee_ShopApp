package uk.ac.hw.group20.order.service;

import java.util.Date;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.main.view.HomeGui;
import uk.ac.hw.group20.order.dto.ServerInfoDto;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.Server;

public class OrderConsumer implements Runnable {

    private final OrderQueue orderQueue;
    private final Server server;
    private final String userId;
    private ServerInfoDto dto;
    private final HomeGui home;
    //private final ThreadTimerDto timerDto;
    private final DynamicConsumerManager manager; 

    public OrderConsumer(OrderQueue orderQueue, HomeGui home, Server server, String userId, ServerInfoDto dto,
    		DynamicConsumerManager manager) {
        this.orderQueue = orderQueue;
        this.server = server;
        this.userId = userId;
        this.dto = dto;
        this.home = home;
        this.manager = manager;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Order order;
                synchronized (orderQueue) {
                    order = orderQueue.getOrderToProcess(server);
                    if (order == null) {
                        if (orderQueue.isOrdersFinished() && orderQueue.isOrdersSavingFinished()) {
                            orderQueue.markPreparationFinished();
                            if (Server.SERVER_ONE.equals(server)) dto.setLblServer1(null);
                            if (Server.SERVER_TWO.equals(server)) dto.setLblServer2(null);
                            if (Server.SERVER_THREE.equals(server)) dto.setLblServer3(null);

                            Logger.logMessage(LogLevel.INFO, "Processing completed. No more orders.");
                            home.updateOrderDisplay(orderQueue.getCurrentQueue(), dto, userId, server);
                            break;
                        }
                    }
                }

                if (order == null) continue;

                String action = order.isPrepared() ? "saving" : "processing";
                Logger.logMessage(LogLevel.INFO, server + " is " + action + " Order: " + order.getOrderId());

                dto = getServerInformation(server, dto, order);
                home.updateOrderDisplay(orderQueue.getCurrentQueue(), dto, userId, server);

                int sleepTime = getDynamicTimer();
                Logger.logMessage(LogLevel.INFO, "Next order will be saved in (" + server + " Sleep Time): " + sleepTime);
                Thread.sleep(sleepTime);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        Logger.logMessage(LogLevel.INFO, "Consumer Thread for " + server + " has been Stopped");
    }

    private ServerInfoDto getServerInformation(Server server, ServerInfoDto dto, Order order) {
        if (dto == null) {
            dto = new ServerInfoDto();
        }
        
        String action = "Processing ";
        if(order.isSaved()) {
        	action = "Saving ";
        	order.setSavedBy(server);
        	order.setDateSaved(new Date());
        }

        switch (server) {
            case SERVER_ONE:
                dto.setLblServer1(order.formatOrder(action));
                return dto;
            case SERVER_TWO:
                dto.setLblServer2(order.formatOrder(action));
                return dto;
            case SERVER_THREE:
                dto.setLblServer3(order.formatOrder(action));
                return dto;
            default:
                return dto;
        }
    }
    
    private int getDynamicTimer() {
        return manager.getTimerForServer(server);
    }
}