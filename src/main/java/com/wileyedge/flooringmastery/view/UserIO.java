package com.wileyedge.flooringmastery.view;

public interface UserIO {
        void print(String msg);

        double readDouble(String prompt);

        int readInt(String prompt);

        String readString(String prompt);
}

