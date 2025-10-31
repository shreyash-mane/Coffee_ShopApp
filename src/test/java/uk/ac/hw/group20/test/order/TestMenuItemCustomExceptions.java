package uk.ac.hw.group20.test.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.ac.hw.group20.errorhandler.ConvertionException;
import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.ShopMenuItem;
import uk.ac.hw.group20.utils.LogLevel;

public class TestMenuItemCustomExceptions {
	
	String menuItemId;
	String name;
	String categoryId;
	String description;
	int quantity;
	double currentPrice;
	ShopMenuItem item;
	
	@BeforeEach
	void setupVariables() {
		this.menuItemId = "MENUOOO";
		try {
			this.item = new ShopMenuItem("OOO", "TEST NAME", "BEV002", "TEST", 1, 1.0);
		} catch (InvalidInputException e) {
			Logger.logMessage(LogLevel.ERROR, "Testing - " + e.getMessage());
		}
		this.name = "TEST NAME";
		this.categoryId = "BEV002";
		this.description = "TEST";
		this.quantity = 1;
		this.currentPrice = 1.0;
	}
	
	@Test
    void testInvalidNameException() {
		
		this.name = null;
		
		InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> {
			new ShopMenuItem(this.menuItemId, this.name, this.categoryId, this.description, this.quantity, this.currentPrice);
        });

        Assertions.assertEquals("Item name can not be blank", exception.getMessage());
    }
	
	@Test
    void testInvalidCategoryIdException() {
		
		this.categoryId = null;
		
		InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> {
			new ShopMenuItem(this.menuItemId, this.name, this.categoryId, this.description, this.quantity, this.currentPrice);
        });

        Assertions.assertEquals("Item category can not be blank", exception.getMessage());
    }

	@Test
    void testConvertionExceptionForQuantity() {
		
		this.quantity = 0;
		
		ConvertionException exception = Assertions.assertThrows(ConvertionException.class, () -> {
			new ShopMenuItem(this.menuItemId, this.name, this.categoryId, this.description, this.quantity, this.currentPrice);
        });

        Assertions.assertEquals("Item quantity must be greater than 1", exception.getMessage());
    }
	
	@Test
    void testConvertionExceptionForPrice() {
		
		this.currentPrice = 0;
		
		ConvertionException exception = Assertions.assertThrows(ConvertionException.class, () -> {
			new ShopMenuItem(this.menuItemId, this.name, this.categoryId, this.description, this.quantity, this.currentPrice);
        });

        Assertions.assertEquals("Item price must be greater than 0", exception.getMessage());
    }
}
