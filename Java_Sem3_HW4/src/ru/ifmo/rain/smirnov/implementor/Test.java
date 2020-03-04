package ru.ifmo.rain.smirnov.implementor;


import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.CachedRowSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Test {
    public static void main(String[] args) throws IOException, ImplerException, ClassNotFoundException {
        Path root = Paths.get("src/a");

        Class<?> token = CachedRowSet.class;
        System.out.println(token.getPackageName());
        InterfaceImplementor interfaceImplementor = new InterfaceImplementor();
        interfaceImplementor.implement(token, root);
        URLClassLoader loader = new URLClassLoader(new URL[]{root.toUri().toURL()});
        String name = token.getPackageName() + "." + token.getSimpleName() + "Impl";
    }

    interface A {
        static int f() {
            return 1;
        }
        default int f1() {
            return 1;
        }
        int foo3();
    }

    interface B extends A {
        float foo4(ArrayList<Integer>... a) throws IOException, NullPointerException;
    }

    class C implements B {

        @Override
        public int foo3() {
            return 0;
        }

        @Override
        public float foo4(ArrayList<Integer>[] a) throws IOException, NullPointerException {
            return 0;
        }
    }
}
