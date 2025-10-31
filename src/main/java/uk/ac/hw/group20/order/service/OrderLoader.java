package uk.ac.hw.group20.order.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.order.model.ShopMenuItem;
import uk.ac.hw.group20.utils.FileType;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.OrderStatus;
import uk.ac.hw.group20.utils.OrderType;
import uk.ac.hw.group20.utils.Server;

public class OrderLoader {
    private static final String FILE_NAME = "data/orders.csv";
    private static final String ARCHIVE_FILE = "data/orders_archive.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //private static LinkedList<Order> orders = new LinkedList<>();
    //MenuItem menuItem = new MenuItem();

    /**
     * Loads orders from the CSV file into the LinkedList.
     */
    public static synchronized LinkedList<Order> loadOrdersFromCSV(FileType type) {
    	
    	LinkedList<Order> orders = new LinkedList<>();
    	File file;
    	int dataLength = 8;
    	
    	if(FileType.ORDER_ARCHIVE == type) {
    		file = new File(ARCHIVE_FILE);
    		dataLength = 10;
    	} else {
    		file = new File(FILE_NAME);
    	}
        

        if (!file.exists()) {
        	Logger.logMessage(LogLevel.ERROR, "Orders file not found. Creating a new order list.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] data = line.split(",");
                if (data.length == dataLength) {
                	List<ShopMenuItem> menuItems = ShopMenuItem.getFormatedMenuItems(data[3].trim());
                	OrderStatus status;
                	OrderType orderType;
                	Server server;
                	try {
                	    status = OrderStatus.valueOf(data[6].trim());
                	} catch (IllegalArgumentException e) {
                		Logger.logMessage(LogLevel.ERROR, "Unknown order status: " + data[6].trim() + " -> " + data);
                	    status = OrderStatus.UNKNOWN;
                	}
                	
                	try {
                		orderType = OrderType.valueOf(data[7].trim());
                	} catch (IllegalArgumentException e) {
                		Logger.logMessage(LogLevel.ERROR, "Unknown order type: " + data[7].trim() + " -> " + data);
                		orderType = OrderType.UNKNOWN;
                	}
                	
                	Order order = new Order(
                            data[0].trim(),
                            DATE_FORMAT.parse(data[1].trim()),
                            data[2].trim(),
                            menuItems,
                            Double.parseDouble(data[4].trim()),
                            Double.parseDouble(data[5].trim()),
                            status,
                            orderType
                        );
                	
                	if(data.length == 10) {
                		try {
                    		server = Server.valueOf(data[8].trim());
                    	} catch (IllegalArgumentException e) {
                    		Logger.logMessage(LogLevel.ERROR, "Unknown Server: " + data[7].trim() + " -> " + data);
                    		server = Server.UNKNOWN;
                    	}
                		order.setSavedBy(server);
                		order.setDateSaved(DATE_FORMAT.parse(data[9].trim()));
                	}
                	
                	orders.add(order);
                    
                } else {
                	Logger.logMessage(LogLevel.ERROR, "Invalid order details: " + line);
                }
            }
        } 
        catch (IllegalArgumentException e) {
            if(e.getMessage().contains("Invalid Order ID format")) {
            	Logger.logMessage(LogLevel.ERROR, e.getMessage());
            } else {
            	throw new RuntimeException("Failed to load orders.", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load orders.", e);
        }
        
        return orders;
    }

    /**
     * Adds a new order to the LinkedList and saves it to CSV.
     */
    public static synchronized boolean addOrder(Order newOrder) {
    	boolean response = false;
        try {
        	LinkedList<Order> orders = loadOrdersFromCSV(FileType.ORDERS);
			orders.add(newOrder);
			saveOrdersToCSV(orders);
			response = true;
			
			OrderQueue orderQueue = OrderQueue.getInstance();
			int maxQueueSize = orderQueue.getQueueSize() + 1;
			if(!orderQueue.isQueueOperationCompleted()) {
				orderQueue.addOrder(newOrder, maxQueueSize);
			}
			
		} catch (Exception e) {
			Logger.logMessage(LogLevel.ERROR, "Failed to add an order: " + e.getMessage());
			 throw new RuntimeException("Failed to add an order: " + e.getMessage());
		}
        
        return response;
    }

    /**
     * Updates an existing order in the LinkedList.
     */
    public static synchronized void updateOrder(String orderId, OrderStatus newStatus) {
    	LinkedList<Order> orders = loadOrdersFromCSV(FileType.ORDERS);
    	if(orders.isEmpty() || orders.size() < 1) {
    		Logger.logMessage(LogLevel.ERROR, "No orders available to update.");
            return;
    	}
    	
    	boolean updated = false;
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.setStatus(newStatus);
                updated = true;
                break;
            }
        }
        
        if (updated) {
            saveOrdersToCSV(orders);
            Logger.logMessage(LogLevel.ERROR, "Order " + orderId + " has been updated to " + newStatus);
        } else {
        	Logger.logMessage(LogLevel.ERROR, "Order ID " + orderId + " was not found.");
        }
    }

    /**
     * Moves an order to the archive and removes it from the active list.
     */
    public static synchronized void completeOrder(String orderId) {
        Order completedOrder = null;
        LinkedList<Order> orders = loadOrdersFromCSV(FileType.ORDERS);

        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getOrderId().equals(orderId)) {
                completedOrder = order;
                completedOrder.setStatus(OrderStatus.COMPLETED);
                iterator.remove();
                break;
            }
        }

        if (completedOrder != null) {
            saveOrdersToCSV(orders);
            archiveOrder(completedOrder);
        }
    }

    /**
     * Push an order to the last of the list.
     */
    public static synchronized void pushOrder(Order newOrder) {
    	LinkedList<Order> orders = loadOrdersFromCSV(FileType.ORDERS);
        orders.addLast(newOrder);
        saveOrdersToCSV(orders);
    }

    /**
     * Pop (remove) the last order from the list.
     */
    public static synchronized Order popOrder() {
    	LinkedList<Order> orders = loadOrdersFromCSV(FileType.ORDERS);
        if (orders.isEmpty()) {
        	Logger.logMessage(LogLevel.ERROR, "The is no order to process");
        	return null;
        }

        Order lastOrder = orders.removeFirst();
        saveOrdersToCSV(orders);
        return lastOrder;
    }

    /**
     * Saves orders to the CSV file.
     */
    private static synchronized void saveOrdersToCSV(LinkedList<Order> orders) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write("OrderID,DateCreated,CustomerID,MenuItems,SubTotal,Discount,Status,OrderType\n");
            for (Order order : orders) {
                bw.write(String.format("%s,%s,%s,%s,%.2f,%.2f,%s,%s\n",
                        order.getOrderId(),
                        DATE_FORMAT.format(order.getDateCreated()),
                        order.getCustomerId(),
                        ShopMenuItem.formatMenuItems(order.getMenuItemList()),
                        order.getSubTotal(),
                        order.getDiscount(),
                        order.getStatus(),
                        order.getOrderType()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save orders.", e);
        }
    }

    /**
     * Saves completed orders to the archive file.
     */
	public static synchronized void archiveOrder(Order order) {
    	//LinkedList<Order> orders = loadOrdersFromCSV(FileType.ORDERS);
    	//ToDo load saved archived orders, add new order and save
		Logger.logMessage(LogLevel.INFO, "Archiving order to file: " + order.toString());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVE_FILE, true))) {
            bw.write(String.format("%s,%s,%s,%s,%.2f,%.2f,%s,%s,%s,%s\n",
                    order.getOrderId(),
                    DATE_FORMAT.format(order.getDateCreated()),
                    order.getCustomerId(),
                    ShopMenuItem.formatMenuItems(order.getMenuItemList()),
                    order.getSubTotal(),
                    order.getDiscount(),
                    order.getStatus(),
                    order.getOrderType(),
                    order.getSavedBy(),
                    DATE_FORMAT.format(order.getDateSaved())));
        } catch (IOException e) {
            throw new RuntimeException("Failed to archive order.", e);
        }
    }

}