package com.wileyedge.flooringmastery.view;
import com.wileyedge.flooringmastery.model.Order;
import com.wileyedge.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FlooringView {
    private UserIO io;

    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        return io.readInt("Please select an option (1-6): ");
    }

    public LocalDate getDateOfOrderDisplay() {
        boolean validInput = false;
        LocalDate date = null;
        while (!validInput) {
            String dateStr = io.readString("Enter the order date (MM/DD/YYYY): ");
            if (dateStr.isEmpty()) {
                io.print("Date cannot be empty. Please try again.");
            }
            else {
                try {
                    date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    validInput = true;
                } catch (DateTimeParseException e) {
                    io.print("Invalid date format. Please use MM/DD/YYYY.");
                }
            }
        }
        return date;
    }

    public String getCustomerName(String n) {
        boolean validInput = false;
        String customername = null;
        while (!validInput) {
            String name = io.readString("Enter customer name (" + n + "): ");
            if(name.isEmpty()){
                return name;
            }
            else if (name.trim().equals("null") || !name.matches("[a-zA-Z0-9., ]+"))
                io.print("Name may not be null and is limited to characters [a-z][0-9] as well as periods and comma characters.");
            else {
                customername = name;
                validInput = true;
            }
        }
        return customername;
    }

    public String getState(String st) {
        boolean validInput = false;
        String state = null;
        while (!validInput) {
            String stateAbbr = io.readString("Enter state abbreviation (" + st + "): ");
            if(stateAbbr.isEmpty()){
                return stateAbbr;
            }
            else if (stateAbbr.equals("null") || !stateAbbr.matches("[A-Z ]{2}"))
                io.print("Must be a valid US State abbreviation.");
            else {
                state = stateAbbr;
                validInput = true;
            }
        }
        return state;
    }

    public BigDecimal getArea(BigDecimal ar) {
        boolean validInput = false;
        BigDecimal area = new BigDecimal("0");
        while (!validInput) {
            String a = io.readString("Enter area in sq ft (" + ar + "): ");
            if(a.isEmpty()){
                return ar;
            }
            else if(!a.matches("[0-9]+")) {
                io.print("The area must be a positive decimal that is at least 100.");
            }
            else if(Double.valueOf(a) > 100) {
                    area = BigDecimal.valueOf(Double.valueOf(a));
                    validInput = true;
            }
            else{
                io.print("The area must be a positive decimal that is at least 100.");
            }
        }
        return area;
    }

    public Order getEditedOrderInfo(Order existingOrder, List<Product> availableProducts) {
        io.print("Editing Order #" + existingOrder.getOrderNumber() + " for " + existingOrder.getOrderDate());
        Order editedOrder = new Order();
        editedOrder.setOrderNumber(existingOrder.getOrderNumber());
        editedOrder.setOrderDate(existingOrder.getOrderDate());
        String name = getCustomerName(existingOrder.getName());
        editedOrder.setName(name.isEmpty() ? existingOrder.getName() : name);
        String state = getState(existingOrder.getState());
        editedOrder.setState(state.isEmpty() ? existingOrder.getState() : state);
        String productType = getProductTypeChoice(existingOrder.getProductType(), availableProducts);
        editedOrder.setProductType(productType.isEmpty() ? existingOrder.getProductType() : productType);
        BigDecimal area = getArea(existingOrder.getArea());
        if (area.equals(existingOrder.getArea())) {
            editedOrder.setArea(existingOrder.getArea());
        } else {
            try {
                editedOrder.setArea(area);
            } catch (NumberFormatException e) {
                io.print("Invalid area format. Keeping original value.");
                editedOrder.setArea(existingOrder.getArea());
            }
        }
        editedOrder.setTaxRate(existingOrder.getTaxRate());
        editedOrder.setCostPerSquareFoot(existingOrder.getCostPerSquareFoot());
        editedOrder.setLaborCostPerSquareFoot(existingOrder.getLaborCostPerSquareFoot());
        editedOrder.setMaterialCost(existingOrder.getMaterialCost());
        editedOrder.setLaborCost(existingOrder.getLaborCost());
        editedOrder.setTax(existingOrder.getTax());
        editedOrder.setTotalCost(existingOrder.getTotalCost());
        return editedOrder;
    }

    public void displayOrdersForDate(List<Order> orders) {
        if (orders.isEmpty()) {
            displayNoOrdersExist();
        } else {
            io.print("Orders for the specified date:");
            orders.forEach(order -> io.print(order.toString()));
        }
    }

    public void displayNoOrdersExist() {
        io.print("No orders exist for that date.");
    }

    public Order getNewOrderInfo(List<Product> availableProducts) {
        Order order = new Order();
        order.setOrderDate(getOrderDate());
        order.setName(getCustomerName());
        order.setState(getState());
        order.setProductType(getProductTypeChoice(availableProducts));
        order.setArea(BigDecimal.valueOf(getArea()));
        return order;
    }

    public double getArea() {
        boolean validInput = false;
        double area = 0;
        while (!validInput) {
            Double a = io.readDouble("Enter area (sq ft): ");
            if (a < 100)
                io.print("The area must be a positive decimal that is at least 100.");
            else {
                area = a;
                validInput = true;
            }
        }
        return area;
    }

    public String getCustomerName() {
        boolean validInput = false;
        String customername = null;
        while (!validInput) {
            String name = io.readString("Enter customer name: ");
            if (name.equals("null") || name.trim().isEmpty() || !name.matches("[a-zA-Z0-9., ]+"))
                io.print("Name may not be blank and is limited to characters [a-z][0-9] as well as periods and comma characters.");
            else {
                customername = name;
                validInput = true;
            }
        }
        return customername;
    }

    public String getState() {
        boolean validInput = false;
        String state = null;
        while (!validInput) {
            String stateAbbr = io.readString("Enter state abbreviation: ");
            if (stateAbbr.equals("null") || stateAbbr.isEmpty() || !stateAbbr.matches("[A-Z ]{2}"))
                io.print("Must be a valid US State abbreviation.");
            else {
                state = stateAbbr;
                validInput = true;
            }
        }
        return state;
    }

    private String getProductTypeChoice(List<Product> availableProducts) {
        io.print("Available Products:");
        io.print("------------------");
        for (Product product : availableProducts) {
            io.print(String.format("%s - Cost/SqFt: $%.2f, Labor/SqFt: $%.2f",
                    product.getProductType(),
                    product.getCostPerSquareFoot(),
                    product.getLaborCostPerSquareFoot()));
        }
        io.print("------------------");
        while (true) {
            String choice = io.readString("Enter the product type from the list above: ").trim();
            if (choice.isEmpty()) {
                io.print("Product type cannot be empty. Please try again.");
                continue;
            }
            for (Product product : availableProducts) {
                if (product.getProductType().equalsIgnoreCase(choice)) {
                    return product.getProductType();
                }
            }
            io.print("Invalid product type. Please choose from the list.");
        }
    }

    private String getProductTypeChoice(String productType, List<Product> availableProducts) {
        io.print("Available Products (" + productType +  "): ");
        io.print("------------------");
        for (Product product : availableProducts) {
            io.print(String.format("%s - Cost/SqFt: $%.2f, Labor/SqFt: $%.2f",
                    product.getProductType(),
                    product.getCostPerSquareFoot(),
                    product.getLaborCostPerSquareFoot()));
        }
        io.print("------------------");
        while (true) {
            String choice = io.readString("Enter the product type from the list above: ").trim();
            if (choice.isEmpty()) {
                return "";
            }
            for (Product product : availableProducts) {
                if (product.getProductType().equalsIgnoreCase(choice)) {
                    return product.getProductType();
                }
            }
            io.print("Invalid product type. Please choose from the list.");
        }
    }

    public LocalDate getOrderDate() {
        while (true) {
            try {
                String dateStr = io.readString("Enter order date (MM/DD/YYYY, must be future): ");
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                if (date.isAfter(LocalDate.now())) {
                    return date;
                }
                io.print("Date must be in the future.");
            }
            catch (DateTimeParseException e) {
                io.print("Invalid date format. Please use MM/DD/YYYY.");
            }
        }
    }

    public int getOrderNumber() {
        return io.readInt("Enter order number: ");
    }

    public void displayOrderDoesNotExist() {
        io.print("Order does not exist.");
    }

    public boolean displayUpdatedOrderAndIfSaved(Order order) {
        io.print("Updated Order: " + order.toString());
        return confirmation("Save this edit? (Y/N): ").equals("Y");
    }

    public boolean getIfRemoveOrder() {
        return confirmation("Are you sure you want to remove this order? (Y/N): ").equals("Y");
    }

    public boolean displayOrderSummaryAndIfOrdered(Order order) {
        io.print("Order Summary: " + order.toString());
        return confirmation("Place this order? (Y/N): ").equals("Y");
    }

    private String confirmation(String message){
        String answer = "";
        boolean validInput = false;
        while (!validInput) {
            String response = io.readString(message);
            if (!response.equals("Y") && !response.equals("N"))
                io.print("Must be Y or N.");
            else {
                answer = response;
                validInput = true;
            }
        }
        return answer;
    }

    public void print(String message) {
        io.print(message);
    }
}