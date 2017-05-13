package de.gruppe3.projekt2;

import java.util.Set;

class OutputHelper {
    private static String indent = "";

    static void enterSubMenu() {
        indent += "\t";
    }

    static void leaveSubMenu() {
        indent = indent.substring(1);
    }

    static void makeStudentTable(Set<Student> students) {
        print("--------+------------------------+------------+--------------");
        print("   ID   |          Name          |  Birthday  | Average Grade");
        print("--------+------------------------+------------+--------------");

        students.forEach(s -> printf(" %6d | %22.22s | %10.10s | %1.2f",
                s.getId(),
                s.getName(),
                s.getBirthday(),
                s.getAvgGrade()));

        print("--------+------------------------+------------+--------------\n");
    }

    static void makeExamTable(Set<Exam> exams) {
        print("-------------------------------+------");
        print("            Subject            | Grade");
        print("-------------------------------+------");

        exams.forEach(e -> printf(" %29.29s | %1.2f", e.subject, e.grade));

        print("-------------------------------+------\n");
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
