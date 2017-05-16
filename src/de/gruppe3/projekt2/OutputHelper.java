package de.gruppe3.projekt2;

import java.util.Comparator;
import java.util.Set;

class OutputHelper {
    enum SortingCriterium {
        SORT_BY_NAME(Comparator.comparing(Student::getName)),
        SORT_BY_GRADE(Comparator.comparingDouble(Student::getAvgGrade)),
        DONT_CARE(Comparator.comparingInt(Student::getId));

        Comparator<Student> sortingMethod;

        SortingCriterium(Comparator<Student> sortingMethod) {
            this.sortingMethod = sortingMethod;
        }
    }

    private static String indent = "";

    static void enterSubMenu() {
        indent += "\t";
    }

    static void leaveSubMenu() {
        indent = indent.substring(1);
    }

    static void makeStudentTable(Set<Student> students, SortingCriterium sortBy) {
        print("--------+------------------------+------------+--------------");
        print("   ID   |          Name          |  Birthday  | Average Grade");
        print("--------+------------------------+------------+--------------");

        students.stream()
                .sorted(sortBy.sortingMethod)
                .forEach(s -> printf(" %6d | %22.22s | %10.10s | %s%1.2f\u001B[0m",
                        s.getId(),
                        s.getName(),
                        s.getBirthday(),
                        //Coloring: Red (31) if failed, green (32) if passed
                        s.getAvgGrade() > 4 ? "\u001B[31m" : "\u001B[32m",
                        s.getAvgGrade()));

        print("--------+------------------------+------------+--------------");
        printf("%61s\n", "Anzahl: " + students.size());
    }

    static void makeStudentTable(Set<Student> students) {
        makeStudentTable(students, SortingCriterium.DONT_CARE);
    }

    static void makeExamTable(Set<Exam> exams) {
        print("-------------------------------+-------+-------");
        print("            Subject            | Grade | Passed");
        print("-------------------------------+-------+-------");

        exams.forEach(e -> printf(" %29.29s | %1.2f  | %s\u001B[0m", e.subject, e.grade, e.grade <= 4 ? "\u001B[32mPassed" : "\u001B[31mFailed"));

        print("-------------------------------+-------+-------");
        printf("%47s\n", "Anzahl: " + exams.size());
    }

    static void print(String s) {
        System.out.println(indent + s);
    }

    static void printError(String s) {
        System.err.println(indent + s);
    }

    @SuppressWarnings("WeakerAccess")
    static void printf(String s, Object... args) {
        System.out.printf(indent + s + "\n", args);
    }
}
