package com.wileyedge.flooringmastery.dao;
import com.wileyedge.flooringmastery.model.Product;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductDaoImpl implements ProductDao {
    private List<Product> products = new ArrayList<>();
    public static final String PRODUCTS_FILE = "src/main/resources/Data/Products.txt";
    public static final String DELIMITER = ",";

    public ProductDaoImpl() {
        try (Scanner scanner = new Scanner(new File(PRODUCTS_FILE))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(DELIMITER);
                Product product = new Product(tokens[0], new BigDecimal(tokens[1]), new BigDecimal(tokens[2]));
                products.add(product);
            }
        } catch (FileNotFoundException e) {
            try {
                throw new FlooringPersistenceException("File could not be found.", e);
            } catch (FlooringPersistenceException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public Product getProduct(String productType) {
        return products.stream()
                .filter(p -> p.getProductType().equalsIgnoreCase(productType))
                .findFirst().orElse(null);
    }

    @Override
    public List<Product> readProducts() {
        return new ArrayList<>(products);
    }
}