package com.wileyedge.flooringmastery.dao;
import com.wileyedge.flooringmastery.model.Tax;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaxDaoImpl implements TaxDao {
    private final List<Tax> allTaxInfo = new ArrayList<>();
    public static final String TAX_FILE = "src/main/resources/Data/Taxes.txt";
    public static final String DELIMITER = ",";

    public TaxDaoImpl() {
        try {
            loadTaxInfo();
        } catch (FlooringPersistenceException e) {
            System.out.print("Could not load tax data from " + TAX_FILE);
        }
    }

    @Override
    public List<Tax> getAllTaxInfo() {
        return new ArrayList<>(allTaxInfo);
    }

    @Override
    public Tax getStateTax(String state) {
        return allTaxInfo.stream()
                .filter(t -> t.getStateAbbreviation().equalsIgnoreCase(state))
                .findFirst().orElse(null);
    }

    private void loadTaxInfo() throws FlooringPersistenceException {
        try (Scanner scanner = new Scanner(new File(TAX_FILE))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(DELIMITER);
                Tax tax = new Tax();
                tax.setStateAbbreviation(tokens[0]);
                tax.setStateName(tokens[1]);
                tax.setTaxRate(new BigDecimal(tokens[2]));
                allTaxInfo.add(tax);
            }
        } catch (FileNotFoundException e) {
                throw new FlooringPersistenceException("Could not load tax data from " + TAX_FILE, e);
            }
        }
    }
