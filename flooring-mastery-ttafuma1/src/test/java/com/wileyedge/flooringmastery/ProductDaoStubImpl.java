package com.wileyedge.flooringmastery;
import com.wileyedge.flooringmastery.dao.ProductDao;
import com.wileyedge.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoStubImpl implements ProductDao {
    public List<Product> products = new ArrayList<>();
    public Product onlyProduct;

    public ProductDaoStubImpl() {
        onlyProduct = new Product("Tile", BigDecimal.valueOf(3.50), BigDecimal.valueOf(4.15));
        this.products.add(onlyProduct);
    }

    @Override
    public Product getProduct(String productType) {
        if (productType.equalsIgnoreCase(onlyProduct.getProductType())) {
            return onlyProduct;
        }
        return null;
    }

    @Override
    public List<Product> readProducts() {
        return new ArrayList<>(products);
    }
}