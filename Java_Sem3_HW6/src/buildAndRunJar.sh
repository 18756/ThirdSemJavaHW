#!/bin/bash
# IDEA doesn't update files fast, to see new jar file you can collapse and expand src folder
IMPLEMENTING_INTERFACE="ru.ifmo.rain.smirnov.implementor.InterfaceForTest"
JAR_PATH="impl.jar"
javac $(find -name "*.java") -d compiled
jar -cvfm InterfaceImplementor.jar META-INF/MANIFEST.MF -C compiled/ .
java -jar InterfaceImplementor.jar -jar "$IMPLEMENTING_INTERFACE" "$JAR_PATH"
