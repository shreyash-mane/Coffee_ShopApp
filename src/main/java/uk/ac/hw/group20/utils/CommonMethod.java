package uk.ac.hw.group20.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import uk.ac.hw.group20.customer.model.Customer;
import uk.ac.hw.group20.customer.service.CustomerLoaderService;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.order.model.ShopMenuItem;
import uk.ac.hw.group20.order.service.MenuItemLoader;

public class CommonMethod {
	
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DefaultListModel getModel(String category) {

		List<ShopMenuItem> menuItems = MenuItemLoader.loadMenuItemsFromCSV();

		List<ShopMenuItem> categoryItems = MenuItemLoader.getMenuItemsByCategoryId(menuItems, category);

		DefaultListModel model = new DefaultListModel();

		if (categoryItems.isEmpty()) {
			model.addElement("There are no items in " + category + " category");
		} else {
			for (ShopMenuItem item : categoryItems) {
				model.addElement(item.displayMenuItem());
			}
		}

		return model;
	}

	public DefaultTableModel getTableModel(String[] columnNames, List<Object[]> tableRows, String table) {
		//Logger.logMessage("Header Columns: " + Arrays.toString(columnNames));
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columnNames);
		int totalQuantity = 0;
		double totalPrice = 0.0;

		String valueIdentifier = "COST";

		if ("Report".equalsIgnoreCase(table)) {
			valueIdentifier = "SALES";
		}

		try {
			for (Object[] tableRow : tableRows) {
				tableModel.addRow(tableRow);
				try {
					totalQuantity += (int) tableRow[2];
				} catch (ClassCastException e) {
					totalQuantity += 0;
				}
				try {
					totalPrice += (double) tableRow[4];
				} catch (ClassCastException e) {
					totalPrice += 0;
				}
			}

			Object[] overLine = { "", "", "-----", "", "-----", "" };
			tableModel.addRow(overLine);

			Object[] summaryRow = { "TOTAL", totalQuantity > 0 ? "QUANTITY" : "",
					totalQuantity > 0 ? totalQuantity : "", totalPrice > 0 ? valueIdentifier : "",
					totalPrice > 0 ? totalPrice : "" };
			tableModel.addRow(summaryRow);

			Object[] underLine = { "", "", "-----", "", "-----", "" };
			tableModel.addRow(underLine);

		} catch (Exception e) {
			Logger.logMessage(LogLevel.ERROR, "Failed to create table model table headers: " + Arrays.toString(columnNames)
					+ " due to: " + e.getMessage());
		}

		return tableModel;
	}

	public JTable getOrderTable(LinkedList<Order> orders) {
		DefaultTableModel tableModel;
		JTable table = new JTable();
		List<Object[]> tableRows = new ArrayList<>();

		Map<String, Integer> customerOrdersCount = new HashMap<>();

		for (Order order : orders) {
			String customerId = order.getCustomerId();
			int numberOfItems = order.getMenuItemList().size();

			customerOrdersCount.put(customerId, numberOfItems);
		}

		for (Order order : orders) {
			Customer customer = CustomerLoaderService.getCustomerById(order.getCustomerId());
			String priority = "";
			if(order.isOnlineOrder()) priority = " (Priority)";
			
			if (customer != null) {
				tableRows.add(new Object[] { order.getOrderId() + priority, customer.firstName + " " + customer.lastName, order.getMenuItemList().size(), DATE_FORMAT.format(order.getDateCreated()) });
			}
		}

		String[] columnNames = { "Order ID", "Customer Name", "Items Ordered", "Date" };
		tableModel = new DefaultTableModel(tableRows.toArray(new Object[0][]), columnNames);
		table.setModel(tableModel);

		return table;
	}

}
