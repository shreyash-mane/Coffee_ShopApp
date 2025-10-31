package uk.ac.hw.group20.test.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.errorhandler.InvalidListSizeException;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.order.model.ShopMenuItem;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.OrderStatus;
import uk.ac.hw.group20.utils.OrderType;

public class TestOrderCustomExceptions {
	
	String orderId;
	Date dateCreated; 
	String customerId;
	List<ShopMenuItem> menuItemList;
	double subTotal;
	double discount;
	OrderStatus status;
	OrderType orderType;
	
	@BeforeEach
	void setupVariables() {
		this.menuItemList = new ArrayList<>();
		ShopMenuItem item = null;
		try {
			item = new ShopMenuItem("OOO", "TEST MENU", "BEV002", "TEST", 1, 1.0);
		} catch (InvalidInputException e) {
			Logger.logMessage(LogLevel.ERROR, "Testing - " + e.getMessage());
		}
		this.orderId = "OOO";
		this.dateCreated = new Date();
		this.customerId = "OOOO";
		menuItemList.add(item);
		this.subTotal = 1.0;
		this.discount = 0;
		this.status =OrderStatus.COMPLETED;
		this.orderType = OrderType.OFFLINE;
	}
	
	
	@Test
    void testInvalidListSizeException() {
		
		menuItemList.clear();
		
		InvalidListSizeException exception = Assertions.assertThrows(InvalidListSizeException.class, () -> {
			new Order(this.orderId, this.dateCreated, this.customerId, this.menuItemList, this.subTotal, this.discount, this.status, this.orderType);
        });

        Assertions.assertEquals("Order cannot be created without menu items", exception.getMessage());
    }
	
	@Test
    void testInvalidInputException() {
		
		this.customerId = null;
		
		InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> {
			new Order(this.orderId, this.dateCreated, this.customerId, this.menuItemList, this.subTotal, this.discount, this.status, this.orderType);
        });

        Assertions.assertEquals("Customer ID can not be blank", exception.getMessage());
    }
	
	@Test
    void testInvalidOrderAmount() {
		
		this.subTotal = -1;
		
		InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> {
			new Order(this.orderId, this.dateCreated, this.customerId, this.menuItemList, this.subTotal, this.discount, this.status, this.orderType);
        });

        Assertions.assertEquals("Order amount cannot be less than 0", exception.getMessage());
    }
	
	@Test
    void testOrderDate() {
		
		this.dateCreated = null;
		
		InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> {
			new Order(this.orderId, this.dateCreated, this.customerId, this.menuItemList, this.subTotal,this.discount, this.status, this.orderType);
        });

        Assertions.assertEquals("Order date can not be blank", exception.getMessage());
    }

}
