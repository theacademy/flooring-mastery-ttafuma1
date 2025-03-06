package com.wileyedge.flooringmastery.dao;
import com.wileyedge.flooringmastery.model.Tax;
import java.util.List;

public interface TaxDao {
    List<Tax> getAllTaxInfo();
    Tax getStateTax(String state);
}