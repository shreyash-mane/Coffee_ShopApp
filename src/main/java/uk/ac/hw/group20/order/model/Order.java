package uk.ac.hw.group20.order.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.ac.hw.group20.customer.model.Customer;
import uk.ac.hw.group20.customer.service.CustomerLoaderService;
import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.errorhandler.InvalidListSizeException;
import uk.ac.hw.group20.utils.OrderStatus;
import uk.ac.hw.group20.utils.OrderType;
import uk.ac.hw.group20.utils.Server;

public class Order {
    private String orderId;
    private Date dateCreated;
    private String customerId;
    private List<ShopMenuItem> menuItemList;
    private double subTotal;
    private double discount;
    private OrderStatus status;
    private OrderType orderType;
    private boolean isPrepared;
    private boolean isSaved;
    private Date dateSaved;
    private Server savedBy;
    
    public Order() {
    	
    }

    public Order(String orderId, Date dateCreated, String customerId, List<ShopMenuItem> menuItemList, double subTotal, double discount, OrderStatus status, OrderType orderType) throws InvalidInputException {
       
    	if (!orderId.matches("^ORDER\\d+$")) {
    	    throw new IllegalArgumentException("Invalid Order ID format (" + orderId + "). It must be like 'ORDER132'.");
    	}
    	
    	if(dateCreated == null) {
			throw new InvalidInputException("Order date can not be blank");
		}
    	
    	if(customerId == null || customerId.trim().isEmpty()) {
			throw new InvalidInputException("Customer ID can not be blank");
		}
    	
    	if(menuItemList == null || menuItemList.size() < 1) {
			throw new InvalidListSizeException("Order cannot be created without menu items");
		}
    	
    	if(subTotal < 0) {
			throw new InvalidInputException("Order amount cannot be less than 0");
		}
    	
    	this.orderId = orderId;
        this.dateCreated = dateCreated;
        this.customerId = customerId;
        this.menuItemList = menuItemList;
        this.subTotal = subTotal;
        this.discount = discount;
        this.status = status;
        this.orderType = orderType;
        this.isSaved = false;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<ShopMenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<ShopMenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public OrderType getOrderType() {
        return orderType;
    }

    public void setType(OrderType orderType) {
        this.orderType = orderType;
    }

    public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public Date getDateSaved() {
		return dateSaved;
	}

	public void setDateSaved(Date dateSaved) {
		this.dateSaved = dateSaved;
	}

	public Server getSavedBy() {
		return savedBy;
	}

	public void setSavedBy(Server savedBy) {
		this.savedBy = savedBy;
	}
    
    public boolean isPrepared() {
		return isPrepared;
	}

	public void setPrepared(boolean isPrepared) {
		this.isPrepared = isPrepared;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", dateCreated=" + dateCreated + ", customerId=" + customerId
				+ ", menuItemList=" + menuItemList + ", subTotal=" + subTotal + ", discount=" + discount + ", status="
				+ status + ", orderType=" + orderType + ", isPrepared=" + isPrepared + ", isSaved=" + isSaved
				+ ", dateSaved=" + dateSaved + ", savedBy=" + savedBy + "]";
	}

	public String formatOrder(String action) {
        StringBuilder sb = new StringBuilder();
        Customer customer = CustomerLoaderService.getCustomerById(customerId);
        sb.append(action);
        
        if (isOnlineOrder()) sb.append("(Priority) ");
        
        sb.append(customer.getFirstName()).append(" ")
        	.append(customer.getLastName())
        	.append("'s order.\n");

        // Count unique items in the order
        Map<String, Integer> itemCounts = new LinkedHashMap<>();
        for (ShopMenuItem item : menuItemList) {
            itemCounts.put(item.getName(), itemCounts.getOrDefault(item.getName(), 0) + 1);
        }

        // Append items with counts
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            sb.append("   ").append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }

        // Append total and discount
        sb.append("Total: £").append(subTotal)
          .append(" (with £").append(discount).append(" discount)");

        return sb.toString();
    }
    
    public boolean isOnlineOrder() {
		boolean isOnlineOrder = false;

		if (OrderType.ONLINE.equals(orderType)) {
			isOnlineOrder = true;
		}

		return isOnlineOrder;
	}
    
    public long getOrderDuration() {
        Date orderDate = dateCreated;
        Date savingDate = dateSaved;

        if (orderDate == null || savingDate == null) {
            throw new IllegalArgumentException("Order date and saving date must not be null");
        }

        Instant orderInstant = orderDate.toInstant();
        Instant savingInstant = savingDate.toInstant();

        return Duration.between(orderInstant, savingInstant).toMillis();
    }
    
	public boolean hasFoodItems() {
		if (menuItemList == null) {
			return false;
		}

		for (ShopMenuItem item : menuItemList) {
			if (item.getCategoryId().equalsIgnoreCase("FOOD")) {
				return true;
			}
		}

		return false;
	}
}