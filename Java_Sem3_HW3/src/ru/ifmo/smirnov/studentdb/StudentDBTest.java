package ru.ifmo.smirnov.studentdb;

import info.kgeorgiy.java.advanced.student.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentDBTest {
    List<Student> students;
    StudentDB studentDB;
    @BeforeEach
    void setUp() {
        studentDB = new StudentDB();
    }

    List<Student> studentList1() {
        return List.of(
            new Student(1, "A", "B", "1"),
            new Student(2, "B", "C", "2"),
            new Student(3, "D", "E", "3"),
            new Student(4, "F", "G", "4"),
            new Student(5, "H", "I", "5")
        );
    }

    List<Student> studentList2() {
        return List.of(
            new Student(1, "A", "B", "1"),
            new Student(1, "A", "B", "1"),
            new Student(1, "A", "E", "2"),
            new Student(1, "A", "V", "1"),
            new Student(2, "B", "B", "2")
        );
    }

    List<Student> studentList3() {
        return List.of(
            new Student(3, "Alex", "Brown", "1"),
            new Student(3, "Aga", "Billy", "1"),
            new Student(3, "Anal", "Eugene", "2"),
            new Student(2, "Anya", "Viagra", "2"),
            new Student(1, "Bob", "Ben", "1")
        );
    }


    @Test
    void getFirstNamesTest() {
        assertEquals(studentDB.getFirstNames(studentList1()), List.of("A", "B", "D", "F", "H"));
        assertEquals(studentDB.getFirstNames(studentList2()), List.of("A", "A", "A", "A", "B"));
        assertEquals(studentDB.getFirstNames(studentList3()), List.of("Alex", "Aga", "Anal", "Anya", "Bob"));
    }

    @Test
    void getLastNamesTest() {
        assertEquals(studentDB.getLastNames(studentList1()), List.of("B", "C", "E", "G", "I"));
        assertEquals(studentDB.getLastNames(studentList2()), List.of("B", "B", "E", "V", "B"));
        assertEquals(studentDB.getLastNames(studentList3()), List.of("Brown", "Billy", "Eugene", "Viagra", "Ben"));
    }

    @Test
    void getGroupsTest() {
        assertEquals(studentDB.getGroups(studentList1()), List.of("1", "2", "3", "4", "5"));
        assertEquals(studentDB.getGroups(studentList2()), List.of("1", "1", "2", "1", "2"));
        assertEquals(studentDB.getGroups(studentList3()), List.of("1", "1", "2", "2", "1"));
    }

    @Test
    void getFullNamesTest() {
        assertEquals(studentDB.getFullNames(studentList1()), List.of("A B", "B C", "D E", "F G", "H I"));
        assertEquals(studentDB.getFullNames(studentList2()), List.of("A B", "A B", "A E", "A V", "B B"));
        assertEquals(studentDB.getFullNames(studentList3()), List.of("Alex Brown", "Aga Billy", "Anal Eugene", "Anya Viagra", "Bob Ben"));
    }

    @Test
    void getDistinctFirstNamesTest() {
        assertEquals(studentDB.getDistinctFirstNames(studentList1()), Set.of("A", "B", "D", "F", "H"));
        assertEquals(studentDB.getDistinctFirstNames(studentList2()), Set.of("A", "B"));
        assertEquals(studentDB.getDistinctFirstNames(studentList3()), Set.of("Alex", "Aga", "Anal", "Anya", "Bob"));
    }

    @AfterEach
    void tearDown() {
        students = null;
        studentDB = null;
    }
}