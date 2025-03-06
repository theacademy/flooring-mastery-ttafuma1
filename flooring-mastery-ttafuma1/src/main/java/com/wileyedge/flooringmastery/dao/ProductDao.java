package com.wileyedge.flooringmastery.dao;
import com.wileyedge.flooringmastery.model.Product;
import java.util.List;

public interface ProductDao {
    Product getProduct(String productType);
    List<Product> readProducts();
}