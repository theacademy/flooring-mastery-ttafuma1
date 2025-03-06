package com.wileyedge.flooringmastery;
import com.wileyedge.flooringmastery.dao.FlooringPersistenceException;
import com.wileyedge.flooringmastery.dao.OrderDao;
import com.wileyedge.flooringmastery.dao.OrderDaoImpl;
import com.wileyedge.flooringmastery.model.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoImplTest {
    OrderDao dao;
    private static final String TEST_ORDER_FOLDER = "src/test/resources/Orders/";
    private static final String TEST_BACKUP_FOLDER = "src/test/resources/Backup/";

    @BeforeEach
    public void setUp() throws FlooringPersistenceException {
        dao = new OrderDaoImpl() {
            @Override
            public String getOrderFolder() {
                return TEST_ORDER_FOLDER;
            }

            @Override
            public String getExportFilePath() {
                return TEST_BACKUP_FOLDER + "DataExport.txt";
            }
        };
    }

    @AfterEach
    public void tearDown() {
        File orderFolder = new File(TEST_ORDER_FOLDER);
        File[] files = orderFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        new File(TEST_BACKUP_FOLDER + "DataExport.txt").delete();
    }

    @Test
    public void testAddOrder() throws FlooringPersistenceException {
        LocalDate date = LocalDate.now().plusDays(1);
        Order order = new Order();
        order.setOrderNumber(1);
        order.setOrderDate(date);
        order.setName("Test.Customer");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(BigDecimal.valueOf(150));
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setCostPerSquareFoot(BigDecimal.valueOf(3.50));
        order.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.15));
        order.setMaterialCost(BigDecimal.valueOf(525.00));
        order.setLaborCost(BigDecimal.valueOf(622.50));
        order.setTax(BigDecimal.valueOf(51.19));
        order.setTotalCost(BigDecimal.valueOf(1198.69));

        dao.addOrder(order);
        List<Order> orders = dao.getOrders(date);
        assertNotNull(orders, "Order list should not be null.");
        assertFalse(orders.isEmpty(), "Order list should not be empty after adding.");
        assertEquals(1, orders.size(), "List should have 1 order.");
        assertEquals("Test.Customer", orders.get(0).getName(), "Customer name should match.");
    }

    @Test
    public void testGetOrders() throws FlooringPersistenceException {
        LocalDate date = LocalDate.now().plusDays(1);
        Order order = new Order();
        order.setOrderNumber(1);
        order.setOrderDate(date);
        order.setName("Test.Customer");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(BigDecimal.valueOf(150));
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setCostPerSquareFoot(BigDecimal.valueOf(3.50));
        order.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.15));
        order.setMaterialCost(BigDecimal.valueOf(525.00));
        order.setLaborCost(BigDecimal.valueOf(622.50));
        order.setTax(BigDecimal.valueOf(51.19));
        order.setTotalCost(BigDecimal.valueOf(1198.69));

        dao.addOrder(order);
        List<Order> orders = dao.getOrders(date);
        assertNotNull(orders, "Order list should not be null.");
        assertEquals(1, orders.size(), "List should have 1 order.");
        assertEquals(order.getOrderNumber(), orders.get(0).getOrderNumber(), "Order number should match.");
    }

    @Test
    public void testEditOrder() throws FlooringPersistenceException {
        LocalDate date = LocalDate.now().plusDays(1);
        Order order = new Order();
        order.setOrderNumber(1);
        order.setOrderDate(date);
        order.setName("Test.Customer");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(BigDecimal.valueOf(150));
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setCostPerSquareFoot(BigDecimal.valueOf(3.50));
        order.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.15));
        order.setMaterialCost(BigDecimal.valueOf(525.00));
        order.setLaborCost(BigDecimal.valueOf(622.50));
        order.setTax(BigDecimal.valueOf(51.19));
        order.setTotalCost(BigDecimal.valueOf(1198.69));

        dao.addOrder(order);
        Order editedOrder = new Order();
        editedOrder.setOrderNumber(1);
        editedOrder.setOrderDate(date);
        editedOrder.setName("Edited.Customer");
        editedOrder.setState("WA");
        editedOrder.setProductType("Wood");
        editedOrder.setArea(BigDecimal.valueOf(150));
        editedOrder.setTaxRate(BigDecimal.valueOf(4.45));
        editedOrder.setCostPerSquareFoot(BigDecimal.valueOf(5.15));
        editedOrder.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.75));
        editedOrder.setMaterialCost(BigDecimal.valueOf(772.50));
        editedOrder.setLaborCost(BigDecimal.valueOf(712.50));
        editedOrder.setTax(BigDecimal.valueOf(66.15));
        editedOrder.setTotalCost(BigDecimal.valueOf(1551.15));

        dao.editOrder(editedOrder);
        List<Order> orders = dao.getOrders(date);
        assertNotNull(orders, "Order list should not be null.");
        assertEquals(1, orders.size(), "List should have 1 order.");
        Order retrievedOrder = orders.get(0);
        assertEquals("Edited.Customer", retrievedOrder.getName(), "Edited name should match.");
        assertEquals("WA", retrievedOrder.getState(), "Edited state should match.");
        assertEquals("Wood", retrievedOrder.getProductType(), "Edited product type should match.");
    }

    @Test
    public void testRemoveOrder() throws FlooringPersistenceException {
        LocalDate date = LocalDate.now().plusDays(1);
        Order order = new Order();
        order.setOrderNumber(1);
        order.setOrderDate(date);
        order.setName("Test.Customer");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(BigDecimal.valueOf(150));
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setCostPerSquareFoot(BigDecimal.valueOf(3.50));
        order.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.15));
        order.setMaterialCost(BigDecimal.valueOf(525.00));
        order.setLaborCost(BigDecimal.valueOf(622.50));
        order.setTax(BigDecimal.valueOf(51.19));
        order.setTotalCost(BigDecimal.valueOf(1198.69));

        dao.addOrder(order);
        dao.removeOrder(order.getOrderNumber(), date);
        List<Order> orders = dao.getOrders(date);
        assertNotNull(orders, "Order list should not be null.");
        assertTrue(orders.isEmpty(), "Order list should be empty after removal.");
    }

    @Test
    public void testExportData() throws FlooringPersistenceException {
        LocalDate date = LocalDate.now().plusDays(1);
        Order order = new Order();
        order.setOrderNumber(1);
        order.setOrderDate(date);
        order.setName("Export.Test");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(BigDecimal.valueOf(150));
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setCostPerSquareFoot(BigDecimal.valueOf(3.50));
        order.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.15));
        order.setMaterialCost(BigDecimal.valueOf(525.00));
        order.setLaborCost(BigDecimal.valueOf(622.50));
        order.setTax(BigDecimal.valueOf(51.19));
        order.setTotalCost(BigDecimal.valueOf(1198.69));

        dao.addOrder(order);
        dao.exportData();
        File exportFile = new File(TEST_BACKUP_FOLDER + "DataExport.txt");
        assertTrue(exportFile.exists(), "Export file should exist in test backup folder.");
    }
}