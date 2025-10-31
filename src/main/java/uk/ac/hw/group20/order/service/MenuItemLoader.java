package uk.ac.hw.group20.order.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.ShopMenuItem;
import uk.ac.hw.group20.utils.LogLevel;

public class MenuItemLoader {
    private static final String FILE_NAME = "data/menu_items.csv";
    List<ShopMenuItem> menuItems = new ArrayList<>();

    public static List<ShopMenuItem> loadMenuItemsFromCSV() {
        List<ShopMenuItem> menuItems = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            throw new RuntimeException("menu_item.csv not found in data folder.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); //Skip reading the first line. This is the csv header
            while ((line = br.readLine()) != null) {
            	line = line.trim();
                if (line.isEmpty()) {
                    continue;  // Skip empty lines
                }
                String[] data = line.split(",");
                if (data.length == 6) {
                    try {
						menuItems.add(new ShopMenuItem(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), Integer.parseInt(data[4].trim()), Double.parseDouble(data[5].trim())));
					} catch (InvalidInputException e) {
						Logger.logMessage(LogLevel.ERROR, e.getMessage());
					}
                } else {
                	Logger.logMessage(LogLevel.ERROR, "Invalid length of menu item details: " + data.length);
                    throw new RuntimeException("Invalid length of menu item details: " + data.length);
                }
            }
        } catch (NumberFormatException e) {
        	throw new RuntimeException("Failed to convert string to number for the menu item. ", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load users from file.", e);
        }

        return menuItems;
    }
    
    public static List<ShopMenuItem> getMenuItemsByCategoryId(List<ShopMenuItem> menuItems, String categoryId) {
        List<ShopMenuItem> filteredItems = new ArrayList<>();

        for (ShopMenuItem item : menuItems) {
            if (item.getCategoryId().equalsIgnoreCase(categoryId.trim())) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }
}