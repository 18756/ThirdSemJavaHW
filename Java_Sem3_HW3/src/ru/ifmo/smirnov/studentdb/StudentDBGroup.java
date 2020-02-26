package ru.ifmo.smirnov.studentdb;

import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentGroupQuery;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDBGroup extends StudentDB implements StudentGroupQuery {
    protected final Comparator<Group> COMPARATOR_BY_STUDENTS_SIZE = Comparator.comparing((Group group) ->
        group.getStudents().size());

    protected List<Group> getGroupsByComparator(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream().map(Student::getGroup).sorted().distinct()
            .map(g -> new Group(g, findStudentsByGroupAndSort(students, g, comparator))).collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroupsByComparator(students, COMPARATOR_BY_NAME);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroupsByComparator(students, Comparator.comparing(Student::getId));
    }

    protected String getLargestGroupByComparator(Collection<Student> students, Comparator<Group> comparator) {
        return getGroupsByName(students).stream().max(comparator.
            thenComparing(Group::getName, Comparator.reverseOrder())).map(Group::getName).orElse("");
    }

    @Override
    public String getLargestGroup(Collection<Student> students) {
        return getLargestGroupByComparator(students, COMPARATOR_BY_STUDENTS_SIZE);
    }

    @Override
    public String getLargestGroupFirstName(Collection<Student> students) {
        return getLargestGroupByComparator(students, Comparator.comparing((Group group) ->
            group.getStudents().stream().map(Student::getFirstName).distinct().count()));
    }
}
