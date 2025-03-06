package com.wileyedge.flooringmastery.view;

import java.util.Scanner;

public class UserIOImpl implements UserIO {
    private final Scanner scanner;

    public UserIOImpl() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public double readDouble(String prompt) {
        print(prompt);
        while (!scanner.hasNextDouble()) {
            print("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    @Override
    public int readInt(String prompt) {
        print(prompt);
        while (!scanner.hasNextInt()) {
            print("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    @Override
    public String readString(String prompt) {
        print(prompt);
        return scanner.nextLine().trim();
    }
}