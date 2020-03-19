package ru.ifmo.rain.smirnov.implementor;


import java.io.IOException;

/**
 * class for test some functional
 */
public class Test {
    /**
     * main function for test
     */
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(Class.forName("ru.ifmo.rain.smirnov.implementor.InterfaceForTest").getCanonicalName());
    }
}
