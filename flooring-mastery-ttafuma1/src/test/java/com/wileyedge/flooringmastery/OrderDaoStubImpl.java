package com.wileyedge.flooringmastery;
import com.wileyedge.flooringmastery.dao.OrderDao;
import com.wileyedge.flooringmastery.model.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoStubImpl implements OrderDao {
     List<Order> orders = new ArrayList<>();
     Order onlyOrder;
     List<String> exportedData = new ArrayList<>();

    public OrderDaoStubImpl() {
        onlyOrder = new Order();
        onlyOrder.setOrderNumber(1);
        onlyOrder.setOrderDate(LocalDate.now().plusDays(1));
        onlyOrder.setName("Ada Lovelace");
        onlyOrder.setState("TX");
        onlyOrder.setProductType("Tile");
        onlyOrder.setArea(BigDecimal.valueOf(150));
        onlyOrder.setTaxRate(BigDecimal.valueOf(4.45));
        onlyOrder.setCostPerSquareFoot(BigDecimal.valueOf(3.50));
        onlyOrder.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.15));
        onlyOrder.setMaterialCost(BigDecimal.valueOf(525));
        onlyOrder.setLaborCost(BigDecimal.valueOf(622.50));
        onlyOrder.setTax(BigDecimal.valueOf(51.19));
        onlyOrder.setTotalCost(BigDecimal.valueOf(1198.69));
        orders.add(onlyOrder);
    }

    @Override
    public List<Order> getOrders(LocalDate date) {
        if (date.equals(onlyOrder.getOrderDate())) {
            return new ArrayList<>(orders);
        }
        return new ArrayList<>();
    }

    @Override
    public void addOrder(Order order) {
        if (orders.stream().anyMatch(o -> o.getOrderNumber() == order.getOrderNumber())) {
            return;
        }
        orders.add(order);
    }

    @Override
    public void editOrder(Order order) {
        orders.removeIf(o -> o.getOrderNumber() == order.getOrderNumber());
        orders.add(order);
    }

    @Override
    public void removeOrder(int orderNumber, LocalDate date) {
        orders.removeIf(o -> o.getOrderNumber() == orderNumber && o.getOrderDate().equals(date));
    }

    @Override
    public void exportData() {
        exportedData.clear();
        orders.forEach(order -> exportedData.add(order.toString()));
    }
}