package uk.ac.hw.group20.order.controller;

import java.util.LinkedList;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.main.view.HomeGui;
import uk.ac.hw.group20.order.dto.ServerInfoDto;
import uk.ac.hw.group20.order.dto.ThreadTimerDto;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.order.service.DynamicConsumerManager;
import uk.ac.hw.group20.order.service.KitchenStaff;
import uk.ac.hw.group20.order.service.OrderLoader;
import uk.ac.hw.group20.order.service.OrderProducer;
import uk.ac.hw.group20.order.service.OrderQueue;
import uk.ac.hw.group20.utils.FileType;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.Server;

public class OrderController {
	
	private LinkedList<Order> orders = OrderLoader.loadOrdersFromCSV(FileType.ORDERS);
	ServerInfoDto dto = new ServerInfoDto();
	OrderQueue orderQueue = OrderQueue.getInstance();
	private OrderProducer producer;
	private Thread producerThread;
	
	private KitchenStaff kitchenStaff;
	private Thread baristaThread;
	
	private DynamicConsumerManager consumerManager;
	
	// Set the default timers
	ThreadTimerDto timerDto = new ThreadTimerDto();
	
	public String updateThreadSleepTimer(ThreadTimerDto timerDto) {
		this.timerDto = timerDto;
		
		OrderQueue shareQueue = OrderQueue.getInstance();
		if(shareQueue.isOrdersFinished() && shareQueue.isOrdersSavingFinished()) {
			return "Threads not updated - Order processing completed.";
		}
		
		Logger.logMessage(LogLevel.INFO, "Changing run time thread timers, " + timerDto.toString());
		
		if (producer != null) {
			producer.setTimer(timerDto.getProducerTimer());
		}
		
		if (kitchenStaff != null) {
			kitchenStaff.setTimer(timerDto.getBaristaTimer());
		}
		
		if (consumerManager != null) {
			
			consumerManager.setTimer(timerDto);
		}
		
		return "Successfully updated timers";
	}
	
	public ServerInfoDto startThreads(String userId, int numberOfServers, HomeGui homeGui) {
       
		int minConsumers = 1;
        int maxConsumers = 3;
        int threshold = 1;
        int maxQueueSize = 2;

        consumerManager = new DynamicConsumerManager(orderQueue, orders, homeGui, minConsumers, maxConsumers, threshold, timerDto, this);

        // Start the producer
        producer = new OrderProducer(orderQueue, orders, timerDto.getProducerTimer(), maxQueueSize);
        producerThread = new Thread(producer);
        producerThread.start();

        // Start dynamic consumers management
        consumerManager.start();
        
        // Start Barista thread.
        kitchenStaff = new KitchenStaff(orderQueue, "Barista", new ServerInfoDto(), homeGui, timerDto.getBaristaTimer(), Server.SERVER_FOUR, null);
        baristaThread = new Thread(kitchenStaff);
        baristaThread.start();

        // Wait for producer to finish
        try {
        	producerThread.join();
        	baristaThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Shutdown consumers once all orders are processed
        Logger.logMessage(LogLevel.INFO, "All new activities to create threads completed. Waiting for the manager to finalize adjustment of consumer of running thread");
        
        return dto;
    }
	
	// Safely stop threads before application stops.
	public void interruptAllThreads() {
	    // Interrupt producer, consumer, and kitchen staff threads
	    if (producerThread != null) {
	    	Logger.logMessage(LogLevel.INFO, producerThread.getName() + " was not stoped. Stopping...");
	        producerThread.interrupt();
	    }
	    if (baristaThread != null) {
	    	Logger.logMessage(LogLevel.INFO, baristaThread.getName() + " was not stoped. Stopping...");
	        baristaThread.interrupt();
	    }
	    
	    // If you have a pool of consumers, interrupt each one
	    if (consumerManager != null) {
	    	Logger.logMessage(LogLevel.INFO, consumerManager + " was not stoped. Stopping...");
	    	consumerManager.shutdown();
	    }
	}

}
