package com.wileyedge.flooringmastery.service;
import com.wileyedge.flooringmastery.dao.FlooringPersistenceException;
import com.wileyedge.flooringmastery.dao.OrderDao;
import com.wileyedge.flooringmastery.dao.ProductDao;
import com.wileyedge.flooringmastery.dao.TaxDao;
import com.wileyedge.flooringmastery.model.Order;
import com.wileyedge.flooringmastery.model.Product;
import com.wileyedge.flooringmastery.model.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService{
    private OrderDao orderDao;
    private ProductDao productDao;
    private TaxDao taxDao;

    public OrderServiceImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    public List<Order> getOrders(LocalDate date) throws FlooringPersistenceException {
        return orderDao.getOrders(date);
    }

    public void addOrder(Order order) throws DataValidationException, FlooringPersistenceException {
        validateOrder(order);
        calculateOrderCost(order);
        orderDao.addOrder(order);
    }

    public void editOrder(Order order) throws DataValidationException, FlooringPersistenceException {
        validateOrder(order);
        calculateOrderCost(order);
        orderDao.editOrder(order);
    }

    public void removeOrder(int orderNumber, LocalDate date) throws FlooringPersistenceException {
        orderDao.removeOrder(orderNumber, date);
    }

    public void exportData() {
        try {
            orderDao.exportData();
        } catch (com.wileyedge.flooringmastery.dao.FlooringPersistenceException e) {
            throw new RuntimeException(e);
        }
    }

    public Order calculateOrderCost(Order order) {
        Product product = getProductByType(order.getProductType());
        Tax tax = getTaxByState(order.getState());
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        order.setTaxRate(tax.getTaxRate());
        order.setMaterialCost(calculateMaterialCost(order));
        order.setLaborCost(calculateLaborCost(order));
        order.setTax(calculateTax(order));
        order.setTotalCost(calculateTotal(order));
        return order;
    }

    private BigDecimal calculateMaterialCost(Order order) {
        return order.getArea().multiply(order.getCostPerSquareFoot());
    }

    private BigDecimal calculateLaborCost(Order order) {
        return order.getArea().multiply(order.getLaborCostPerSquareFoot());
    }

    private BigDecimal calculateTax(Order order) {
        BigDecimal baseCost = order.getMaterialCost().add(order.getLaborCost());
        return baseCost.multiply(order.getTaxRate().divide(BigDecimal.valueOf(100)));
    }

    private BigDecimal calculateTotal(Order order) {
        return order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()).setScale(2,
                RoundingMode.FLOOR);
    }

    private void validateOrder(Order order) throws DataValidationException {
        if (order.getName() == null || order.getName().isBlank() || !order.getName().matches("[a-zA-Z0-9., ]+")) {
            throw new DataValidationException("Invalid customer name.");
        }
        if (order.getArea().compareTo(BigDecimal.valueOf(100)) < 0) {
            throw new DataValidationException("Area must be at least 100 sq ft.");
        }
        if (getTaxByState(order.getState()) == null) {
            throw new DataValidationException("State not found in tax records.");
        }
        if (getProductByType(order.getProductType()) == null) {
            throw new DataValidationException("Product type not found.");
        }
    }

    public Product getProductByType(String type) {
        return productDao.getProduct(type);
    }

    public List<Product> getAllProducts() {
        return productDao.readProducts();
    }

    public Tax getTaxByState(String state) {
        return taxDao.getAllTaxInfo().stream()
                .filter(t -> t.getStateAbbreviation().equalsIgnoreCase(state))
                .findFirst().orElse(null);
    }
}