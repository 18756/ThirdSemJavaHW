package ru.ifmo.rain.smirnov.implementor;

import java.util.*;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Class implements interfaces
 * @version 0.5
 * @author Alexandr Smirnov
 */
public class InterfaceImplementor implements Impler, JarImpler {

    /**
     * const {@link String} TAB equivalent to the 4 spaces
     */
    protected final String TAB = "    ";

    /**
     * Generate implemented class.
     * Generate class implemented token interface with path = root/package,
     * where package is the token package
     * @param token {@link Class interface} for implementing
     * @param root {@link Path} start path to class
     * @throws ImplerException
     * <ul>
     *     <li>token is null</li>
     *     <li>root is null</li>
     *     <li>token isn't interface</li>
     *     <li>token is private</li>
     *     <li>writing exception</li>
     * </ul>
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (token == null) {
            throw new ImplerException("Token cannot be null");
        } else if (root == null) {
            throw new ImplerException("Root cannot be null");
        } else if (!token.isInterface() || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Token should be an interface");
        } else {
            try (BufferedWriter writer = Files.newBufferedWriter(createFilePath(token, root), StandardCharsets.UTF_8)) {
                StringBuilder classAndMethods = new StringBuilder();
                classAndMethods.append("public class ").append(token.getSimpleName()).append("Impl").append(" implements ").append(token.getCanonicalName()).append(" {");
                for (Method m : token.getMethods()) {
                    if (!m.isDefault()) {
                        classAndMethods.append("\n").append(TAB).append("public ").append(m.getReturnType().getCanonicalName()).append(" ").append(m.getName()).append("(");
                        classAndMethods.append(Arrays.stream(m.getParameters()).map(t -> t.getType().getCanonicalName() + " " + t.getName()).collect(Collectors.joining(", ")));
                        classAndMethods.append(")");
                        if (m.getExceptionTypes().length > 0) {
                            classAndMethods.append(" throws ");
                            classAndMethods.append(Arrays.stream(m.getExceptionTypes()).map(Class::getCanonicalName).collect(Collectors.joining(", ")));
                        }
                        classAndMethods.append(" {");
                        if (m.getReturnType() != void.class) {
                            classAndMethods.append("\n").append(TAB).append(TAB).append("return ").append(getDefaultValue(m.getReturnType())).append(";\n");
                        }
                        classAndMethods.append(TAB).append("}\n");
                    }
                }
                classAndMethods.append("}");
                writer.write(token.getPackage() == null ? "" :"package " + token.getPackageName() + ";\n\n");
                writer.write(classAndMethods.toString());
            } catch (IOException e) {
                throw new ImplerException("Writing exception", e);
            }
        }
    }

    /**
     * Create file path for generating class.
     * The path starts with root and continues with package of token.
     * Then the method adds generating class name to dir and return it.
     * @param token {@link Class interface} for implementing
     * @param root {@link Path} start path to class
     * @return {@link Path path} to generating class
     * @throws IOException fail while creating directories
     */
    private Path createFilePath(Class<?> token, Path root) throws IOException {
        Path fileDir = getDirectory(token, root);
        Files.createDirectories(fileDir);
        return fileDir.resolve(token.getSimpleName() + "Impl.java");
    }

    /**
     * Gets directory for generating class.
     * Directory path starts with root and continues with package of token.
     * @param token {@link Class interface} for implementing
     * @param root {@link Path} start path to class
     * @return {@link Path path} to directory of generating class
     */
    private Path getDirectory(Class<?> token, Path root) {
        return token.getPackage() == null ? root :root.resolve(token.getPackageName().replace(".", File.separator));
    }

    /**
     * Gets path to generating class.
     * Path starts with root and continues with package of token.
     * Then the method adds name of class to result
     * @param token {@link Class interface} for implementing
     * @param root {@link Path} start path to class
     * @return {@link Path path} to generating class
     */
    private Path getClassPath(Class<?> token, Path root) {
        return getDirectory(token, root).resolve(token.getSimpleName() + "Impl");
    }

    /**
     * Gets default value of type.
     * Null for objects, true for boolean, 0 for other numeric type
     * @param type token of type
     * @return {@link String} default value for type in string
     */
    private String getDefaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return "null";
        } else if (type == boolean.class) {
            return "true";
        } else {
            return "0";
        }
    }

    /**
     * Generates jar file for class implementing token.
     * Generates class implementing token, compiles it and generate jar for it.
     * At the end delete generated class
     * @param token {@link Class interface} for implementing
     * @param jarFile {@link Path path} to creating jar file
     * @throws ImplerException
     * <ul>
     *     <li>token is null</li>
     *     <li>jarFile is null</li>
     *     <li>jarFile is null</li>
     *     <li>token isn't interface</li>
     *     <li>token is private</li>
     *     <li>writing exception</li>
     *     <li>compile exception</li>
     * </ul>
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        if (jarFile == null) {
            throw new ImplerException("Jar file cannot be null");
        }
        Path tmp = Paths.get("coolFolderForTest");
        implement(token, tmp);
        compile(tmp, getClassPath(token, tmp));
        createJar(token, tmp, jarFile);
        clear(tmp.toFile());
    }

    /**
     * Compiles class.
     * Compile class with class path.
     * @param root {@link Path} start path to class
     * @param classPath {@link Path path} to generated class
     * @throws ImplerException
     * <ul>
     *     <li>Compiler not found</li>
     *     <li>Compiling exception</li>
     * </ul>
     */
    private void compile(Path root, Path classPath) throws ImplerException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ImplerException("Compiler not found");
        }
        final List<String> args = new ArrayList<>();
        args.add(classPath + ".java");
        args.add("-cp");
        args.add(root + File.pathSeparator + System.getProperty("java.class.path"));
        final int exitCode = compiler.run(null, null, null, args.toArray(String[]::new));
        if (exitCode != 0) {
            throw new ImplerException("Compiling exception: return code: " + exitCode);
        }
    }

    /**
     * Create jar file.
     * Write compiled class to jarFile
     * @param token {@link Class interface} for implementing
     * @param root {@link Path} start path to class
     * @param jarFile {@link Path path} to creating jar file
     * @throws ImplerException writing exception
     */
    private void createJar(Class<?> token, Path root, Path jarFile) throws ImplerException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream writer = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            writer.putNextEntry(new ZipEntry(getClassPath(token, Paths.get("")) + ".class"));
            Files.copy(Paths.get(getClassPath(token, root) + ".class"), writer);
        } catch (IOException e) {
            throw new ImplerException("Cannot create jar file", e);
        }
    }

    /**
     * Clear dirs after generating class.
     * Remove files recursively from file.
     * @param file {@link File file} or directory for deleting with content
     */
    private void clear(File file) {
        if (file.isDirectory()) {
            for (File underFile : Objects.requireNonNull(file.listFiles())) {
                clear(underFile);
            }
        }
        file.delete();
    }


    /**
     * Main function for implements tokens and generates jarFiles.
     * Accepts 2 or 3 args. If first param is -jar, so next params is token and jarFilePath, else
     * First param is token, second is rootPath.
     * @param args {@link java.lang.String} array args of main
     * @throws ImplerException
     * <ul>
     *     <li>Wrong token</li>
     *     <li>Wrong root or jarFile path</li>
     *     <li>Implementing exceptions</li>
     * </ul>
     */
    public static void main(String[] args) throws ImplerException {
        if (args == null || args.length < 2) {
            throw new ImplerException("You should give min two params");
        }
        InterfaceImplementor interfaceImplementor = new InterfaceImplementor();
        try {
            if ("-jar".equals(args[0])) {
                interfaceImplementor.implementJar(Class.forName(args[1]), Paths.get(args[2]));
            } else {
                interfaceImplementor.implement(Class.forName(args[0]), Paths.get(args[1]));
            }
        } catch (ClassNotFoundException e) {
            throw new ImplerException("Wrong token " + e.getMessage());
        } catch (InvalidPathException e) {
            throw new ImplerException("Wrong file path " + e.getMessage());
        }
    }
}