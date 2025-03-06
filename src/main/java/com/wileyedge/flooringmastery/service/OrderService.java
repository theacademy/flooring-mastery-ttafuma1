package com.wileyedge.flooringmastery.service;
import com.wileyedge.flooringmastery.dao.FlooringPersistenceException;
import com.wileyedge.flooringmastery.model.Order;
import com.wileyedge.flooringmastery.model.Product;
import com.wileyedge.flooringmastery.model.Tax;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<Order> getOrders(LocalDate date) throws FlooringPersistenceException;
    void addOrder(Order order) throws DataValidationException, FlooringPersistenceException;
    void editOrder(Order order) throws DataValidationException, FlooringPersistenceException;
    void removeOrder(int orderNumber, LocalDate date) throws FlooringPersistenceException;
    void exportData();
    Order calculateOrderCost(Order order);
    Product getProductByType(String type);
    List<Product> getAllProducts();
    Tax getTaxByState(String state);
}