package com.wileyedge.flooringmastery;
import com.wileyedge.flooringmastery.controller.FlooringController;
import com.wileyedge.flooringmastery.dao.FlooringPersistenceException ;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args)  {
        ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        try {
            FlooringController controller = appContext.getBean("controller", FlooringController.class);
            controller.run();
        } catch (FlooringPersistenceException e) {
            System.out.println("Runtime Error!");
        }
    }
}