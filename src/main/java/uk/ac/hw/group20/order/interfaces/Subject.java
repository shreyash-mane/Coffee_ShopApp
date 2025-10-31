package uk.ac.hw.group20.order.interfaces;

import uk.ac.hw.group20.order.service.OrderQueue;

public interface Subject {
	void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(OrderQueue orderQueue);

}
