package com.wileyedge.flooringmastery.dao;
import com.wileyedge.flooringmastery.model.Order;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoImpl implements OrderDao {
    private Map<LocalDate, List<Order>> orders = new HashMap<>();
    private static final String DEFAULT_ORDER_FOLDER = "src/main/resources/Orders/";
    private static final String DEFAULT_EXPORT_FILE = "src/main/resources/Backup/DataExport.txt";
    public static String DELIMITER = ",";
    public static DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");
    public static DateTimeFormatter EXPORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    protected String getOrderFolder() {
        return DEFAULT_ORDER_FOLDER;
    }

    protected String getExportFilePath() {
        return DEFAULT_EXPORT_FILE;
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws FlooringPersistenceException {
        loadOrders(date);
        return orders.getOrDefault(date, new ArrayList<>());
    }

    @Override
    public void addOrder(Order order) throws FlooringPersistenceException {
        loadOrders(order.getOrderDate());
        List<Order> dateOrders = orders.getOrDefault(order.getOrderDate(), new ArrayList<>());
        dateOrders.add(order);
        orders.put(order.getOrderDate(), dateOrders);
        writeOrders(order.getOrderDate());
    }

    @Override
    public void editOrder(Order order) throws FlooringPersistenceException {
        loadOrders(order.getOrderDate());
        List<Order> dateOrders = orders.get(order.getOrderDate());
        dateOrders.removeIf(o -> o.getOrderNumber() == order.getOrderNumber());
        dateOrders.add(order);
        orders.put(order.getOrderDate(), dateOrders);
        writeOrders(order.getOrderDate());
    }

    @Override
    public void removeOrder(int orderNumber, LocalDate date) throws FlooringPersistenceException {
        loadOrders(date);
        List<Order> dateOrders = orders.get(date);
        dateOrders.removeIf(o -> o.getOrderNumber() == orderNumber);
        orders.put(date, dateOrders);
        writeOrders(date);
    }

    @Override
    public void exportData() throws FlooringPersistenceException {
        File ordersDir = new File(getOrderFolder()); // Use overridden folder
        if (!ordersDir.exists() || !ordersDir.isDirectory()) {
            throw new FlooringPersistenceException("Orders directory not found: " + getOrderFolder());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(getExportFilePath()))) { // Use overridden path
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                    "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");
            File[] orderFiles = ordersDir.listFiles((dir, name) -> name.startsWith("Orders_") && name.endsWith(".txt"));
            if (orderFiles == null || orderFiles.length == 0) {
                return;
            }
            for (File file : orderFiles) {
                String fileName = file.getName();
                String dateStr = fileName.substring(7, 15);
                LocalDate date = LocalDate.parse(dateStr, FILE_DATE_FORMATTER);
                try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {
                    scanner.nextLine();
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        Order order = unmarshallOrder(line, date);
                        writer.println(marshallOrderForExport(order));
                    }
                } catch (IOException e) {
                    throw new FlooringPersistenceException("Could not read file: " + fileName, e);
                }
            }
            writer.flush();
        } catch (IOException e) {
            throw new FlooringPersistenceException("Could not export data to " + getExportFilePath(), e);
        }
    }

    private Order unmarshallOrder(String orderAsText, LocalDate date) throws FlooringPersistenceException {
        String[] parts = splitCSVLine(orderAsText);
        if (parts.length != 12) {
            throw new FlooringPersistenceException("Invalid order format: " + orderAsText);
        }
        Order order = new Order();
        try {
            order.setOrderNumber(Integer.parseInt(parts[0]));
            order.setName(parts[1]);
            order.setState(parts[2]);
            order.setTaxRate(new BigDecimal(parts[3]));
            order.setProductType(parts[4]);
            order.setArea(new BigDecimal(parts[5]));
            order.setCostPerSquareFoot(new BigDecimal(parts[6]));
            order.setLaborCostPerSquareFoot(new BigDecimal(parts[7]));
            order.setMaterialCost(parts[8].equals("null") ? null : new BigDecimal(parts[8])); // Handle nulls
            order.setLaborCost(parts[9].equals("null") ? null : new BigDecimal(parts[9]));
            order.setTax(parts[10].equals("null") ? null : new BigDecimal(parts[10]));
            order.setTotalCost(parts[11].equals("null") ? null : new BigDecimal(parts[11]));
            order.setOrderDate(date);
        } catch (NumberFormatException e) {
            throw new FlooringPersistenceException("Error parsing numeric value in order: " + orderAsText, e);
        }
        return order;
    }

    private String[] splitCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }

    private String marshallOrder(Order order) {
        return order.getOrderNumber() + DELIMITER +
                "\"" + order.getName() + "\"" + DELIMITER +
                order.getState() + DELIMITER +
                order.getTaxRate() + DELIMITER +
                order.getProductType() + DELIMITER +
                order.getArea() + DELIMITER +
                order.getCostPerSquareFoot() + DELIMITER +
                order.getLaborCostPerSquareFoot() + DELIMITER +
                (order.getMaterialCost() != null ? order.getMaterialCost() : "null") + DELIMITER +
                (order.getLaborCost() != null ? order.getLaborCost() : "null") + DELIMITER +
                (order.getTax() != null ? order.getTax() : "null") + DELIMITER +
                (order.getTotalCost() != null ? order.getTotalCost() : "null");
    }

    private String marshallOrderForExport(Order order) {
        return marshallOrder(order) + DELIMITER + order.getOrderDate().format(EXPORT_DATE_FORMATTER);
    }

    private void loadOrders(LocalDate date) throws FlooringPersistenceException {
        String fileName = getOrderFolder() + "Orders_" + date.format(FILE_DATE_FORMATTER) + ".txt";
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            scanner.nextLine();
            List<Order> orderList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Order order = unmarshallOrder(line, date);
                orderList.add(order);
            }
            orders.put(date, orderList);
        } catch (FileNotFoundException e) {
            orders.putIfAbsent(date, new ArrayList<>());
        }
    }

    private void writeOrders(LocalDate date) throws FlooringPersistenceException {
        String fileName = getOrderFolder() + "Orders_" + date.format(FILE_DATE_FORMATTER) + ".txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                    "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            orders.getOrDefault(date, new ArrayList<>()).stream()
                    .map(this::marshallOrder)
                    .forEach(out::println);
            out.flush();
        } catch (IOException e) {
            throw new FlooringPersistenceException("Could not save orders for " + date, e);
        }
    }
}