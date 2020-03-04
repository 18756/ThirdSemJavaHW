package info.kgeorgiy.java.advanced.implementor;

import info.kgeorgiy.java.advanced.implementor.full.interfaces.InterfaceWithoutMethods;
import info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces;
import info.kgeorgiy.java.advanced.implementor.full.interfaces.Proxies;
import info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;

import javax.accessibility.Accessible;
import javax.management.Descriptor;
import java.nio.file.Paths;
import java.util.RandomAccess;
import java.util.logging.Logger;

/**
 * Tests for easy version
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-implementor">Implementor</a> homework
 * for <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class InterfaceImplementorTest extends BaseImplementorTest {
    private String methodName;
    @Rule
    public TestWatcher watcher = watcher(description -> methodName = description.getMethodName());

    @Test
    public void test01_constructor() {
        assertConstructor(Impler.class);
    }

    @Test
    public void test02_methodlessInterfaces() {
        System.setSecurityManager(null);
        test(false, RandomAccess.class, InterfaceWithoutMethods.class);
    }

    @Test
    public void test03_standardInterfaces() {
        test(false, Accessible.class, AccessibleAction.class, SDeprecated.class);
    }

    @Test
    public void test04_extendedInterfaces() {
        test(false, Descriptor.class, CachedRowSet.class, DataInput.class, DataOutput.class, Logger.class);
    }

    @Test
    public void test05_standardNonInterfaces() {
        test(true, void.class, String[].class, int[].class, String.class, boolean.class);
    }

    /*@Test
    public void test06_java8Interfaces() {
        test(false, InterfaceWithStaticMethod.class, InterfaceWithDefaultMethod.class);
    }*/

    protected void test(final boolean shouldFail, final Class<?>... classes) {
        test(Paths.get(methodName), shouldFail, classes);
    }
}