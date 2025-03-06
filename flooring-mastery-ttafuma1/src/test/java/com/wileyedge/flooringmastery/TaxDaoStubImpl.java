package com.wileyedge.flooringmastery;
import com.wileyedge.flooringmastery.dao.TaxDao;
import com.wileyedge.flooringmastery.model.Tax;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxDaoStubImpl implements TaxDao {
    public List<Tax> taxes = new ArrayList<>();
    public Tax onlyTax;

    public TaxDaoStubImpl() {
        onlyTax = new Tax();
        onlyTax.setStateAbbreviation("TX");
        onlyTax.setStateName("Texas");
        onlyTax.setTaxRate(BigDecimal.valueOf(4.45));
        taxes.add(onlyTax);
    }

    @Override
    public List<Tax> getAllTaxInfo() {
        return new ArrayList<>(taxes);
    }

    @Override
    public Tax getStateTax(String state) {
        if (state.equalsIgnoreCase(onlyTax.getStateAbbreviation())) {
            return onlyTax;
        }
        return null;
    }
}