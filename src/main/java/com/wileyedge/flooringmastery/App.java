package com.wileyedge.flooringmastery;

import com.wileyedge.flooringmastery.controller.FlooringController;
import com.wileyedge.flooringmastery.dao.*;
import com.wileyedge.flooringmastery.service.OrderService;
import com.wileyedge.flooringmastery.service.OrderServiceImpl;
import com.wileyedge.flooringmastery.view.FlooringView;
import com.wileyedge.flooringmastery.view.UserIO;
import com.wileyedge.flooringmastery.view.UserIOImpl;

public class App {
    public static void main(String[] args)  {
        try {
            UserIO io = new UserIOImpl();
            FlooringView view = new FlooringView(io);
            ProductDao productDao = new ProductDaoImpl();
            OrderDao orderDao = new OrderDaoImpl();
            TaxDao taxDao = new TaxDaoImpl();
            OrderService service = new OrderServiceImpl(orderDao, productDao, taxDao);
            FlooringController controller = new FlooringController(view, service);
            controller.run();
        } catch (FlooringPersistenceException e) {
            System.out.println("Runtime Error!");
        }
    }
}