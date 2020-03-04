package ru.ifmo.rain.smirnov.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class InterfaceImplementor implements Impler {
    protected final String TAB = "    ";

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (token == null) {
            throw new ImplerException("Token cannot be null");
        } else if (root == null) {
            throw new ImplerException("Root cannot be null");
        } else if (!token.isInterface()) {
            throw new ImplerException("Token should be an interface");
        } else {
            try (BufferedWriter writer = Files.newBufferedWriter(getClassPath(token, root), StandardCharsets.UTF_8)) {
                StringBuilder classAndMethods = new StringBuilder();
                classAndMethods.append("public class ").append(token.getSimpleName()).append("Impl")
                    .append(" implements ").append(token.getSimpleName()).append(" {");
                for (Method m : token.getMethods()) {
                    if (!m.isDefault()) {
                        classAndMethods.append("\n").append(TAB).append("public ").append(m.getReturnType().getCanonicalName())
                        .append(" ").append(m.getName()).append("(");
                        classAndMethods.append(Arrays.stream(m.getParameters()).map(t -> t.getType().getCanonicalName()
                            + " " + t.getName()).collect(Collectors.joining(", ")));
                        classAndMethods.append(")");
                        if (m.getExceptionTypes().length > 0) {
                            classAndMethods.append(" throws ");
                            classAndMethods.append(Arrays.stream(m.getExceptionTypes()).map(Class::getCanonicalName)
                                .collect(Collectors.joining(", ")));
                        }
                        classAndMethods.append(" {");
                        if (m.getReturnType() != void.class) {
                            classAndMethods.append("\n").append(TAB).append(TAB).append("return ")
                                .append(getReturnValue(m.getReturnType())).append(";\n");
                        }
                        classAndMethods.append(TAB).append("}\n");
                    }
                }
                classAndMethods.append("}");
                writer.write(getPackageLine(token, root));
                writer.write(classAndMethods.toString());
            } catch (IOException e) {
                throw new ImplerException("Writing exception", e);
            }
        }
    }

    private Path getClassPath(Class<?> token, Path root) throws IOException {
        Path fileDir = getFileDirectory(token, root);
        Files.createDirectories(fileDir);
        return fileDir.resolve(token.getSimpleName() + "Impl.java");
    }

    private String getPackageLine(Class<?> token, Path root) {
        return token.getPackage() == null ? "" : "package " + token.getPackageName() + ";\n\n";
    }

    private static Path getFileDirectory(Class<?> token, Path root) {
        return token.getPackage() == null ? root : root.resolve(token.getPackageName().replace(".", File.separator));
    }

    private String getReturnValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return "null";
        } else if (returnType == boolean.class) {
            return "true";
        } else {
            return "0";
        }
    }
}