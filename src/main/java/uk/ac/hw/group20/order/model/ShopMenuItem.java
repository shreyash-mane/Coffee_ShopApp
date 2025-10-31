package uk.ac.hw.group20.order.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.hw.group20.errorhandler.ConvertionException;
import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.utils.LogLevel;

public class ShopMenuItem {
    private String menuItemId;
    private String name;
    private String categoryId;
    private String description;
    private int quantity;
    private double currentPrice;
    
    public ShopMenuItem() {
        this.menuItemId = null;
        this.name = null;
        this.categoryId = null;
        this.description = null;
        this.quantity = 0;
        this.currentPrice = 0.0;
    }

    public ShopMenuItem(String menuItemID, String name, String categoryID, String description, int quantity, double currentPrice) throws InvalidInputException {
        
    	if (!menuItemID.matches("^[A-Z]{3}\\d+$")) {
    	    throw new IllegalArgumentException("Invalid ID format (" + menuItemID + "). It must be like 'BEV132'.");
    	}
    	
    	if(name == null || name.trim().isEmpty()) {
			throw new InvalidInputException("Item name can not be blank");
		}
    	
    	if(categoryID == null || categoryID.trim().isEmpty()) {
			throw new InvalidInputException("Item category can not be blank");
		}
    	
    	if(quantity < 1) {
			throw new ConvertionException("Item quantity must be greater than 1");
		}
    	
    	if(currentPrice <= 0) {
			throw new ConvertionException("Item price must be greater than 0");
		}
    	
    	this.menuItemId = menuItemID;
        this.name = name;
        this.categoryId = categoryID;
        this.description = description;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String displayMenuItem() {
        
        return menuItemId + ", " + name + ", " + currentPrice;
    }

	@Override
	public String toString() {
		return menuItemId + ", " + name + ", " + categoryId + ", "
				+ description + ", " + quantity + ", " + currentPrice;
	}
	
	/**
     * Converts a string of menu items into a list.
     */
	public static List<ShopMenuItem> getFormatedMenuItems(String menuData) {
        List<ShopMenuItem> menuItems = new ArrayList<>();
        String[] items = menuData.split(";");
        for (String item : items) {
            String[] parts = item.split(":");
            if (parts.length == 6) {
                try {
					menuItems.add(new ShopMenuItem(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), Double.parseDouble(parts[5])));
				} catch (NumberFormatException e) {
					Logger.logMessage(LogLevel.ERROR, e.getMessage());
				} catch (InvalidInputException e) {
					Logger.logMessage(LogLevel.ERROR, e.getMessage());
				}
            }
        }
        return menuItems;
    }
	
	/**
     * Converts a list of menu items into a formatted string for saving in CSV.
     */
    public static String formatMenuItems(List<ShopMenuItem> menuItems) {
    	if (menuItems == null || menuItems.isEmpty()) return "";

        return menuItems.stream()
                .map(item -> String.format("%s:%s:%s:%s:%d:%.2f",
                        item.getMenuItemId(),
                        item.getName(),
                        item.getCategoryId(),
                        item.getDescription(),
                        item.getQuantity(),
                        item.getCurrentPrice()))
                .collect(Collectors.joining(";"));
    }
    
    /**
     * Converts a list of menu items into a formatted string for displaying.
     */
    public static String formatDisplayMenuItems(List<ShopMenuItem> menuItems) {
    	if (menuItems == null || menuItems.isEmpty()) return "";

        return menuItems.stream()
                .map(item -> String.format("%s:%s:%s:%s:%d:%.2f",
                        item.getMenuItemId(),
                        item.getName(),
                        item.getCategoryId(),
                        item.getDescription(),
                        item.getQuantity(),
                        item.getCurrentPrice()))
                .collect(Collectors.joining("\n"));
    }
   
}