package ru.ifmo.smirnov.studentdb;

import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentDB implements StudentQuery {
    protected final Comparator<Student> COMPARATOR_BY_NAME = Comparator.comparing(Student::getLastName)
        .thenComparing(Student::getFirstName)
        .thenComparing(Student::getId);

    protected final Function<Student, String> FULL_NAME = student -> student.getFirstName() + " " + student.getLastName();

    protected List<String> getFields(List<Student> students, Function<Student, String> mapper) {
        return students.stream().map(mapper).collect(Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getFields(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getFields(students, Student::getLastName);
    }

    @Override
    public List<String> getGroups(List<Student> students) {
        return getFields(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getFields(students, FULL_NAME);
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMinStudentFirstName(List<Student> students) {
        return students.stream().min(Comparator.comparing(Student::getId)).map(Student::getFirstName).orElse("");
    }

    protected List<Student> sortStudentsByComparator(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream().sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsByComparator(students, Comparator.comparing(Student::getId));
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsByComparator(students, COMPARATOR_BY_NAME);
    }

    protected List<Student> findStudentsByField(Collection<Student> students,
                                                Function<Student, Object> fieldGetter, Object fieldValue) {
        return students.stream().filter(s -> fieldGetter.apply(s).equals(fieldValue)).collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return sortStudentsByName(findStudentsByField(students, Student::getFirstName, name));
    }


    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return sortStudentsByName(findStudentsByField(students, Student::getLastName, name));
    }

    protected List<Student> findStudentsByGroupAndSort(Collection<Student> students, String group, Comparator<Student> comparator) {
        return sortStudentsByComparator(findStudentsByField(students, Student::getGroup, group), comparator);
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, String group) {
        return findStudentsByGroupAndSort(students, group, COMPARATOR_BY_NAME);
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, String group) {
        return findStudentsByGroup(students, group).stream().collect(Collectors
            .toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(Comparator.naturalOrder())));
    }
}
