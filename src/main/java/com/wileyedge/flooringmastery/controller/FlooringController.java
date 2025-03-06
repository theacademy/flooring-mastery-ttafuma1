package com.wileyedge.flooringmastery.controller;
import com.wileyedge.flooringmastery.dao.FlooringPersistenceException;
import com.wileyedge.flooringmastery.model.Order;
import com.wileyedge.flooringmastery.service.DataValidationException;
import com.wileyedge.flooringmastery.service.OrderService;
import com.wileyedge.flooringmastery.view.FlooringView;
import java.time.LocalDate;
import java.util.List;

public class FlooringController {
    private FlooringView view;
    private OrderService service;

    public FlooringController(FlooringView view, OrderService service) {
        this.view = view;
        this.service = service;
    }

    public void run() throws FlooringPersistenceException {
        boolean keepGoing = true;
        int choice = 0;
        while (keepGoing) {
            choice = getMenuSelection();
            switch (choice) {
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportData();
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        quitMessage();
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void displayOrders() throws FlooringPersistenceException {
        boolean hasErrors = false;
        do {
            try {
                LocalDate date = view.getDateOfOrderDisplay();
                if (date == null) {
                    view.print("Order display cancelled.");
                }
                List<Order> orders = service.getOrders(date);
                view.displayOrdersForDate(orders);
            } catch (FlooringPersistenceException e) {
                hasErrors = true;
                view.print("Display order error!");
            }
        } while(hasErrors);
    }

    private void addOrder() {
        boolean hasErrors = false;
        do {
            Order order = view.getNewOrderInfo(service.getAllProducts());
            try {
                service.addOrder(order);
                if (view.displayOrderSummaryAndIfOrdered(order)) {
                    view.print("Order added successfully.");
                } else {
                    service.removeOrder(order.getOrderNumber(), order.getOrderDate());
                    view.print("Order discarded.");
                }
                hasErrors = false;
            }  catch (DataValidationException e) {
                hasErrors = true;
                view.print("Invalid information entered.");
            } catch (FlooringPersistenceException e) {
                hasErrors = true;
                view.print("Date is not valid.");
            }
        } while(hasErrors);
    }

    private void editOrder() {
        LocalDate date = view.getDateOfOrderDisplay();
        if (date == null) {
            view.print("Edit cancelled.");
            return;
        }
        int orderNumber = view.getOrderNumber();
        Order order = null;
        try {
            order = service.getOrders(date).stream()
                    .filter(o -> o.getOrderNumber() == orderNumber)
                    .findFirst().orElse(null);
        } catch (FlooringPersistenceException e) {
            view.print("We don't have that date.");
            return;
        }
        if (order == null) {
            view.displayOrderDoesNotExist();
            return;
        }
        Order editedOrder = view.getEditedOrderInfo(order, service.getAllProducts());
        if (editedOrder == null) {
            view.print("Edit cancelled.");
            return;
        }
        try{
            if (!editedOrder.getState().equals(order.getState()) ||
                !editedOrder.getProductType().equals(order.getProductType()) ||
                !editedOrder.getArea().equals(order.getArea())) {
            service.calculateOrderCost(editedOrder);
            }
        } catch (NullPointerException e){
            view.print("No changes made, invalid or no input.");
            return;
        }
        if (view.displayUpdatedOrderAndIfSaved(editedOrder)) {
            try {
                service.editOrder(editedOrder);
                view.print("Order updated successfully.");
            } catch (DataValidationException e) {
                view.print("Invalid information entered: " + e.getMessage());
            } catch (FlooringPersistenceException e) {
                view.print("Error saving order: " + e.getMessage());
            }
        } else {
            view.print("Edit discarded.");
        }
    }

    private void removeOrder() {
        LocalDate date = view.getDateOfOrderDisplay();
        int orderNumber = view.getOrderNumber();
        Order order = null;
        try {
            order = service.getOrders(date).stream()
                    .filter(o -> o.getOrderNumber() == orderNumber)
                    .findFirst().orElse(null);
        } catch (FlooringPersistenceException e) {
            view.print("The date is not present.");
        }
        if (order == null) {
            view.displayOrderDoesNotExist();
            return;
        }
        view.print(order.toString());
        if (view.getIfRemoveOrder()) {
            try {
                service.removeOrder(orderNumber, date);
            } catch (FlooringPersistenceException e) {
                view.print("Date does not exist.");
            }
            view.print("Order removed.");
        }
    }

    private void exportData() {
        service.exportData();
        view.print("Data exported successfully.");
    }

    private void quitMessage() {
        view.print("Exiting Flooring Program. Goodbye.");
    }

    private void unknownCommand(){
        view.print("Unknown Command.");
    }
}