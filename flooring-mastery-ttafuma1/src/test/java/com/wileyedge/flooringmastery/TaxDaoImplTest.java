package com.wileyedge.flooringmastery;
import com.wileyedge.flooringmastery.dao.TaxDao;
import com.wileyedge.flooringmastery.dao.TaxDaoImpl;
import com.wileyedge.flooringmastery.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TaxDaoImplTest {
    TaxDao dao;

    @BeforeEach
    public void setUp() {
        dao = new TaxDaoImpl();
    }

    @Test
    public void testGetStateTax() {
        Tax tax = dao.getStateTax("TX");
        assertNotNull(tax);
        assertEquals("TX", tax.getStateAbbreviation());
        assertEquals(new BigDecimal("4.45"), tax.getTaxRate());
    }

    @Test
    public void testGetAllTaxInfo() {
        List<Tax> taxes = dao.getAllTaxInfo();
        assertFalse(taxes.isEmpty());
    }
}