package uk.ac.hw.group20.order.bill;

import java.util.List;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.ShoppingCart;
import uk.ac.hw.group20.utils.LogLevel;

public class BillManager {
	
	public BillResponse getCustomerBill(List<ShoppingCart> cartItems) {
		
		// Calculate total price
        double grandTotal = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
        
        //Count categories per each items
        int countBeverage =0;
        int countFood =0;
        for(ShoppingCart item : cartItems) {
        	if("Beverage".equalsIgnoreCase(item.getCategory())) {
        		countBeverage = countBeverage + 1;
        	}
        	
        	if("Food".equalsIgnoreCase(item.getCategory())) {
        		countFood = countFood  + 1;
        	}
        }
        
        Logger.logMessage(LogLevel.INFO, "Count Beverage: " + countBeverage + " Count Food: " + countFood);
        
        //Calculate Tax
        double taxAmount = grandTotal * 20.0 / 100;
        
        //Calculate discount
        //At least two beverage and one food item
        double discountedAmount = 0;
        if(countBeverage >= 2 && countFood >= 1) {
        	Logger.logMessage(LogLevel.INFO, "Discount condition pass ");
        	discountedAmount = grandTotal * 5.0 / 100;
        }
        
        //Calculate Payable Amount
        double payableAmount = grandTotal - taxAmount - discountedAmount;
        
        BillResponse response = new BillResponse();
        response.setTotalAmount(grandTotal);
        response.setTaxAmount(taxAmount);
        response.setPayableAmount(payableAmount);
		response.setDiscount(discountedAmount);
		
		return response;	
	}

}
