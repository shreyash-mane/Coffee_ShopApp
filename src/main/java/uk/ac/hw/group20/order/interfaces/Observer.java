package uk.ac.hw.group20.order.interfaces;

import uk.ac.hw.group20.order.service.OrderQueue;

public interface Observer {
	void update(OrderQueue order);
}
