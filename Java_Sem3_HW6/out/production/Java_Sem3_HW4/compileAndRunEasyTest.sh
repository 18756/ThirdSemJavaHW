#!/bin/bash
javac $(find -name "*.java") -d compiled
java -cp compiled -p tests -m info.kgeorgiy.java.advanced.implementor jar-interface ru.ifmo.rain.smirnov.implementor.InterfaceImplementor M32351