package com.wileyedge.flooringmastery.dao;
import com.wileyedge.flooringmastery.model.Order;
import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
    List<Order> getOrders(LocalDate date) throws FlooringPersistenceException;
    void addOrder(Order order) throws FlooringPersistenceException;
    void editOrder(Order order)throws FlooringPersistenceException;
    void removeOrder(int orderNumber, LocalDate date) throws FlooringPersistenceException;
    void exportData() throws FlooringPersistenceException;
}