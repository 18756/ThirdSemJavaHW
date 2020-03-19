package ru.ifmo.rain.smirnov.implementor;

public interface InterfaceForTest {
    void voidMethod();

    int returnIntMethod();

    boolean returnBooleanMethod();

    Object returnObjectMethod();

    default int itWontImplement() {
        return 0;
    }
}
