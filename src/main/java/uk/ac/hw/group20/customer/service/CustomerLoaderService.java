package uk.ac.hw.group20.customer.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import uk.ac.hw.group20.customer.model.Customer;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.utils.CustomerType;
import uk.ac.hw.group20.utils.LogLevel;

public class CustomerLoaderService {
	
	private static final String FILE_NAME = "data/customers.csv";

    /**
     * This method is used to load customer details from the CSV file.
     * @return List of Customer objects
     */
    public static List<Customer> loadCustomersFromCSV() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
        	Logger.logMessage(LogLevel.ERROR, "Customer file not found.");
            return customers;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Skip empty lines
                
                String[] data = line.split(",");
                if (data.length == 9) {
                    customers.add(new Customer(
                        data[0].trim(), // userId
                        data[1].trim(), // firstName
                        data[2].trim(), // middleName
                        data[3].trim(), // lastName
                        data[4].trim(), // postCode
                        data[5].trim(), // address
                        data[6].trim(), // email
                        data[7].trim(),  // phone
                        CustomerType.valueOf(data[8].trim()) //Customer Type
                    ));
                } else {
                    Logger.logMessage(LogLevel.ERROR, "Invalid customer details: " + line);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load customers.", e);
        }

        return customers;
    }

    /**
     * This method is used to find a customer by their userId.
     * @return Customer object if found, null otherwise
     */
    public static Customer getCustomerById(String userId) {
        List<Customer> customers = loadCustomersFromCSV();
        for (Customer customer : customers) {
            if (customer.userId.equalsIgnoreCase(userId.trim())) {
                return customer;
            }
        }
        return null; // Customer not found
    }

   
}
