package uk.ac.hw.group20.order;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.ShoppingCart;
import uk.ac.hw.group20.utils.LogLevel;

public class ShoppingCartManager {
    private static final String FILE_NAME = "data/shopping_cart.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * This method is used to loads shopping cart items from the CSV file.
     * @return List of ShoppingCartItem objects
     */
    public static List<ShoppingCart> loadCartItemsFromCSV() {
        List<ShoppingCart> cartItems = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
        	Logger.logMessage(LogLevel.ERROR, "Shopping cart file not found. Creating a new cart.");
            return cartItems;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Skip empty lines
                
                String[] data = line.split(",");
                if (data.length == 8) {
                    cartItems.add(new ShoppingCart(
                        data[0].trim(),
                        data[1].trim(),
                        data[2].trim(),
                        data[3].trim(),
                        Integer.parseInt(data[4].trim()),
                        Double.parseDouble(data[5].trim()),
                        DATE_FORMAT.parse(data[6].trim()),
                        data[7].trim()
                    ));
                } else {
                	Logger.logMessage(LogLevel.ERROR, "Invalid cart item details: " + line);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load shopping cart items.", e);
        }

        return cartItems;
    }
    
    /**
     * This method shopping cart items by Customer ID.
     * @return List<ShoppingCartItem>
     */
    public static List<ShoppingCart> getCartItemsByCustomerId(String customerId) {
        List<ShoppingCart> cartItems = loadCartItemsFromCSV();
        List<ShoppingCart> customerCart = new ArrayList<>();

        for (ShoppingCart item : cartItems) {
            if (item.getCustomerId().equalsIgnoreCase(customerId.trim())) {
                customerCart.add(item);
            }
        }

        return customerCart;
    }

    /**
     * This method is used to add an item to the shopping cart and writes it to the CSV file.
     * @return nothing
     */
    public static void addItemToCart(ShoppingCart newItem) {
        List<ShoppingCart> cartItems = loadCartItemsFromCSV();
        boolean itemExists = false;

        // If the item already exists in the cart, add quantity
        for (ShoppingCart item : cartItems) {

            if (item.getItemId().equals(newItem.getItemId()) && item.getCustomerId().equals(newItem.getCustomerId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                itemExists = true;
                break;
            }
        }

        // If item is new, add it to the list
        if (!itemExists) {
            cartItems.add(newItem);
        }
        
        saveCartItemsToCSV(cartItems);
    }

    /**
     * This method removes an item from the cart by item ID.
     */
    public static void removeItemFromCart(String itemId) {
        List<ShoppingCart> cartItems = loadCartItemsFromCSV();
        cartItems.removeIf(item -> item.getItemId().equals(itemId));
        saveCartItemsToCSV(cartItems);
    }

    /**
     * This method clears all items from the shopping cart (after checkout).
     */
    public static void clearCart(String customerId) {
        List<ShoppingCart> cartItems = loadCartItemsFromCSV();
        cartItems.removeIf(item -> item.getCustomerId().equals(customerId));
        saveCartItemsToCSV(cartItems);
    }

    /**
     * This method saves the shopping cart items back to the CSV file.
     */
    private static void saveCartItemsToCSV(List<ShoppingCart> cartItems) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write("CartID,ItemID,ItemName,Category,Quantity,UnitPrice,CreatedDate,CustomerID\n");
            for (ShoppingCart item : cartItems) {
                bw.write(String.format("%s,%s,%s,%s,%d,%.2f,%s,%s\n",
                        item.getCartId(),
                        item.getItemId(),
                        item.getItemName(),
                        item.getCategory(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        DATE_FORMAT.format(item.getCreatedDate()),
                        item.getCustomerId()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save shopping cart items.", e);
        }
    }
}