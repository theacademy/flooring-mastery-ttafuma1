package com.wileyedge.flooringmastery;
import com.wileyedge.flooringmastery.dao.*;
import com.wileyedge.flooringmastery.model.Order;
import com.wileyedge.flooringmastery.service.DataValidationException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.wileyedge.flooringmastery.service.OrderService;
import com.wileyedge.flooringmastery.service.OrderServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    OrderService service;

    public OrderServiceTest() {
        OrderDao orderDao = new OrderDaoStubImpl();
        ProductDao productDao = new ProductDaoStubImpl();
        TaxDao taxDao = new TaxDaoStubImpl();
        service = new OrderServiceImpl(orderDao, productDao, taxDao);
    }

    @Test
    public void testAddValidOrder() {
        Order order = new Order();
        order.setOrderNumber(2); // Unique number
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setName("Charles Babbage");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(BigDecimal.valueOf(200));

        try {
            service.addOrder(order);
        } catch (DataValidationException e) {
            fail("Valid order should not throw an exception.");
        } catch (FlooringPersistenceException e){
            fail("Valid order should not throw an exception.");
        }

        List<Order> orders;
        try {
            orders = service.getOrders(order.getOrderDate());
        } catch (FlooringPersistenceException e) {
            throw new RuntimeException(e);
        }
        assertTrue(orders.stream().anyMatch(o -> o.getOrderNumber() == 2), "Order should be added.");
    }

    @Test
    public void testAddDuplicateOrderNumber() {
        Order order = new Order();
        order.setOrderNumber(1);
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setName("Charles Babbage");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(BigDecimal.valueOf(200));

        try {
            service.addOrder(order);
            List<Order> orders = service.getOrders(order.getOrderDate());
            long count = orders.stream().filter(o -> o.getOrderNumber() == 1).count();
            assertEquals(1, count, "Duplicate order number should not add a new order.");
        } catch (DataValidationException e) {
            fail("Valid order should not throw an exception.");
        } catch (FlooringPersistenceException e){
            fail("Valid order should not throw an exception.");
        }
    }

    @Test
    public void testAddOrderWithInvalidData() {
        Order order = new Order();
        order.setName("Test@Customer");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(BigDecimal.valueOf(150));
        DataValidationException nameException = assertThrows(
                DataValidationException.class,
                () -> service.addOrder(order),
                "Expected DataValidationException for invalid name."
        );
        assertEquals("Invalid customer name.", nameException.getMessage(), "Exception message should match.");

        order.setName("Charles Babbage");
        order.setArea(BigDecimal.valueOf(50));
        DataValidationException areaException = assertThrows(
                DataValidationException.class,
                () -> service.addOrder(order),
                "Expected DataValidationException for invalid area."
        );
        assertEquals("Area must be at least 100 sq ft.", areaException.getMessage(), "Exception message should match.");
    }

    @Test
    public void testGetOrders() {
        try {
            List<Order> orders = service.getOrders(LocalDate.now().plusDays(1));
            assertEquals(1, orders.size(), "Should return one order from stub.");
            assertEquals("Ada Lovelace", orders.get(0).getName(), "The order should be Ada’s.");
        }
        catch (FlooringPersistenceException e){
            fail("Valid order should not throw an exception.");
        }
    }

    @Test
    public void testRemoveOrder() {
        try {
            service.removeOrder(1, LocalDate.now().plusDays(1));
            List<Order> orders = service.getOrders(LocalDate.now().plusDays(1));
            assertEquals(0, orders.size(), "Order should be removed from stub.");
            service.removeOrder(999, LocalDate.now().plusDays(1));
            assertEquals(0, orders.size(), "Removing non-existent order should do nothing.");
        } catch (FlooringPersistenceException e){
            fail("Valid order should not throw an exception.");
        }

    }

    @Test
    public void testExportData() {
        try {
            service.exportData();
        } catch (Exception e) {
            fail("Export data should not throw an exception in stub.");
        }
    }
}