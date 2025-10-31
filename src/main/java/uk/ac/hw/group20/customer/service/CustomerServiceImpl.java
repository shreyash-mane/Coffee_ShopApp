package uk.ac.hw.group20.customer.service;

import java.util.List;

import uk.ac.hw.group20.customer.model.Customer;

public class CustomerServiceImpl implements CustomerService{
	
	public String[] getCusterDropDownList() {
    	List<Customer> customer = CustomerLoaderService.loadCustomersFromCSV();
    	//String[] userArray = new String[users.size()];
    	String[] userArray = new String[customer.size()];
    	for (int i = 0; i < customer.size(); i++) {
    	    userArray[i] = customer.get(i).getUserId() + " - " + customer.get(i).getFirstName() + " " + customer.get(i).getLastName();
    	}
    	
    	return userArray;

	}

}
