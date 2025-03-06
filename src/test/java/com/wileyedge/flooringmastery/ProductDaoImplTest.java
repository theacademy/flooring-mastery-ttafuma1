package com.wileyedge.flooringmastery;
import com.wileyedge.flooringmastery.dao.ProductDao;
import com.wileyedge.flooringmastery.dao.ProductDaoImpl;
import com.wileyedge.flooringmastery.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoImplTest {
    ProductDao dao;

    @BeforeEach
    public void setUp() {
        dao = new ProductDaoImpl();
    }

    @Test
    public void testGetProduct() {
        Product product = dao.getProduct("Tile");
        assertNotNull(product);
        assertEquals("Tile", product.getProductType());
        assertEquals(new BigDecimal("3.50"), product.getCostPerSquareFoot());
    }

    @Test
    public void testReadProducts() {
        List<Product> products = dao.readProducts();
        assertFalse(products.isEmpty());
    }
}