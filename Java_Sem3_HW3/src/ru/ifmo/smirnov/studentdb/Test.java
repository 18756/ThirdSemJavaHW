package ru.ifmo.smirnov.studentdb;

import info.kgeorgiy.java.advanced.student.Student;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Student> students = List.of(
            new Student(2, "Николай", "Тананыкин", "M3235"),
            new Student(1, "Дмитрий", "Гнатюк", "M3239")
            );
        StudentDB studentDB = new StudentDB();
        studentDB.getDistinctFirstNames(students).forEach(System.out::println);
    }
}
