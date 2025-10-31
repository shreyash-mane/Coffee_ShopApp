package uk.ac.hw.group20.test.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.order.model.ShopMenuItem;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.OrderStatus;
import uk.ac.hw.group20.utils.OrderType;

public class OrderTest {
	
	Order testOrder;
	Date currentDate;
	List<ShopMenuItem> menuItemList = new ArrayList<>();
	
	@BeforeEach
	public void setupOrderTest() {
		testOrder = new Order();
		currentDate = new Date();
		ShopMenuItem item = null;
		try {
			item = new ShopMenuItem("MU001", "Chicken", "FOD001", "Chicken wraps", 2, 2.50);
		} catch (InvalidInputException e) {
			Logger.logMessage(LogLevel.INFO, "Testing - " + e.getMessage());
		}
		menuItemList.add(item);
		try {
			testOrder = new Order("TEST0001", currentDate, "CS001", menuItemList, 5.0, 1.0, OrderStatus.COMPLETED, OrderType.OFFLINE);
		} catch (InvalidInputException e) {
			Logger.logMessage(LogLevel.ERROR, "Testing - " + e.getMessage());
		}
	}
	
	@Test
	public void testOrderConstructorParameter() {
		
		double totalAmount = testOrder.getMenuItemList().stream()
			    .mapToDouble(e -> e.getQuantity() * e.getCurrentPrice())
			    .sum();
		
		Assertions.assertTrue(testOrder != null);
		Assertions.assertTrue(menuItemList.size() == 1);
		Assertions.assertFalse(menuItemList.isEmpty());
		
		Assertions.assertEquals("TEST0001", testOrder.getOrderId());
		Assertions.assertEquals(currentDate, testOrder.getDateCreated());
		Assertions.assertEquals("CS001", testOrder.getCustomerId());
		Assertions.assertEquals(1, testOrder.getMenuItemList().size());
		Assertions.assertEquals(testOrder.getSubTotal(), totalAmount, 0.01);
		Assertions.assertEquals(OrderStatus.COMPLETED, testOrder.getStatus());
		
	}
	
	@Test
	public void testOrderToString() {

	    String orderString = testOrder.toString();
	    
	    Assertions.assertTrue(orderString.contains("OD001"));
	    Assertions.assertTrue(orderString.contains("CS001"));
	    Assertions.assertTrue(orderString.contains("Chicken wraps"));
	    Assertions.assertTrue(orderString.contains("COMPLETED"));
	}
	
	@Test
	public void testOrderSetters() {
	    Order order = new Order();
	    
	    order.setOrderId("OD002");
	    order.setDateCreated(currentDate);
	    order.setMenuItemList(menuItemList);
	    order.setCustomerId("CS002");
	    order.setSubTotal(20.0);
	    order.setStatus(OrderStatus.COMPLETED);
	    
	    Assertions.assertEquals("OD002", order.getOrderId());
	    Assertions.assertEquals(currentDate, testOrder.getDateCreated());
	    Assertions.assertEquals("CS002", order.getCustomerId());
	    Assertions.assertEquals(1, testOrder.getMenuItemList().size());
	    Assertions.assertEquals(20.0, order.getSubTotal());
	    Assertions.assertEquals(OrderStatus.COMPLETED, order.getStatus());
	}
	
	@AfterEach
	public void cleanOrder() {
		//Remove all items created by test class
		this.testOrder = null;
		this.currentDate = null;
		this.menuItemList.clear();
	}

}
