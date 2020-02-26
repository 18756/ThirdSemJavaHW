package ru.ifmo.smirnov.studentdb;

import info.kgeorgiy.java.advanced.student.AdvancedStudentGroupQuery;
import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.Student;

import java.util.*;
import java.util.stream.Collectors;

public class AdvancedStudentDBGroup extends StudentDBGroup implements AdvancedStudentGroupQuery {

    @Override
    public String getMostPopularName(Collection<Student> students) {
        return null;
        //return students.stream().collect(Collectors.toMap(FULL_NAME, Student::getGroup));
    }

    @Override
    public List<String> getFirstNames(Collection<Student> students, int[] indices) {
        return null;
//        return getFirstNames(Arrays.stream(indices).mapToObj(i -> findStudentsByField(students, Student::getId, i)).collect(, (List l1, List l2) -> {l1.addAll(l2);}));
    }

    @Override
    public List<String> getLastNames(Collection<Student> students, int[] indices) {
        return null;
    }

    @Override
    public List<String> getGroups(Collection<Student> students, int[] indices) {
        return null;
    }

    @Override
    public List<String> getFullNames(Collection<Student> students, int[] indices) {
        return null;
    }
}
