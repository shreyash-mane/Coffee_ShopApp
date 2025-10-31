package uk.ac.hw.group20.test.order;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.ShopMenuItem;
import uk.ac.hw.group20.utils.LogLevel;

public class ShoppingMenuItemTest {
	
	ShopMenuItem testMenuItem = new ShopMenuItem();
	List<ShopMenuItem> menuItemList = new ArrayList<>();
	String menuData = "BEV109:Matcha Latte:Beverage:Description:2:4.50;BEV108:Cold Brew:Beverage:Description:2:4.25";
	
	@BeforeEach
	public void setupOrderTest() {
		try {
			testMenuItem = new ShopMenuItem("MU001", "Chicken", "FOD001", "Chicken wraps", 2, 2.50);
		} catch (InvalidInputException e) {
			Logger.logMessage(LogLevel.INFO, "Testing - " + e.getMessage());
		}
	}
	
	@Test
	public void testConstructorParameter() {
		
		Assertions.assertTrue(testMenuItem != null);
		
		Assertions.assertEquals("MU001", testMenuItem.getMenuItemId());
		Assertions.assertEquals("Chicken", testMenuItem.getName());
		Assertions.assertEquals("FOD001", testMenuItem.getCategoryId());
		Assertions.assertEquals(2, testMenuItem.getQuantity());
		Assertions.assertEquals(2.50, testMenuItem.getCurrentPrice());
		Assertions.assertEquals("Chicken wraps", testMenuItem.getDescription());
		
	}
	
	@Test
	public void testMenuItemToString() {

	    String menuItem = testMenuItem.toString();
	    
	    Assertions.assertTrue(menuItem.contains("MU001"));
	    Assertions.assertTrue(menuItem.contains("Chicken"));
	    Assertions.assertTrue(menuItem.contains("FOD001"));
	    Assertions.assertTrue(menuItem.contains("2"));
	}
	
	@Test
	public void testMenuItemSetters() {
		ShopMenuItem menuItem = new ShopMenuItem();
	    
	    menuItem.setMenuItemId("MENU002");
	    menuItem.setCategoryId("BEVERAGE");
	    menuItem.setName("Muffin");
	    menuItem.setDescription("Cake");
	    menuItem.setQuantity(20);
	    menuItem.setCurrentPrice(2.50);
	    
	    Assertions.assertEquals("MENU002", menuItem.getMenuItemId());
	    Assertions.assertEquals("BEVERAGE", menuItem.getCategoryId());
	    Assertions.assertEquals("Muffin", menuItem.getName());
	    Assertions.assertEquals("Cake", menuItem.getDescription());
	    Assertions.assertEquals(20, menuItem.getQuantity());
	    Assertions.assertEquals(2.50, menuItem.getCurrentPrice());
	}
	
	@Test
	public void testDisplayMenuItem() {
		String formatedMenuItem = testMenuItem.displayMenuItem();
		
		Assertions.assertFalse(formatedMenuItem == null);
		Assertions.assertTrue(formatedMenuItem.contains("MU001"));
		Assertions.assertTrue(formatedMenuItem.contains("2.5"));
	}
	
	@Test
	public void testFormatedMenuItems() {
		List<ShopMenuItem> formatedMenuItems = testMenuItem.getFormatedMenuItems(menuData);
		
		Assertions.assertFalse(formatedMenuItems == null);
		Assertions.assertEquals(2, formatedMenuItems.size());
	}
	
	@Test
	public void testFormatMenuItems() {
		menuItemList.add(testMenuItem);
		
		String formatMenuItems = testMenuItem.formatMenuItems(menuItemList);
		
		Assertions.assertFalse(menuItemList == null);
		Assertions.assertEquals("",  testMenuItem.formatMenuItems(null));
		Assertions.assertTrue(formatMenuItems.contains("MU001:Chicken"));
	}
	
	@Test
	public void testFormatDisplayMenuItems() {
		menuItemList.add(testMenuItem);
		
		String formatMenuItems = testMenuItem.formatDisplayMenuItems(menuItemList);
		
		Assertions.assertFalse(menuItemList == null);
		Assertions.assertEquals("",  testMenuItem.formatMenuItems(null));
		Assertions.assertTrue(formatMenuItems.contains("MU001:Chicken"));
	}
	
	
	@AfterEach
	public void cleanOrder() {
		//Remove all items created by test class
		this.testMenuItem = null;
	}

}
