package de.gruppe3.projekt2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Student {

    private String name;
    private String date;
    private int id;
    private Set<Exam> exams = new HashSet<>();
    private double total = 0.0;

    public Student(String name, String date, int id, List<Exam> examList) {
        this.name = name;
        this.date = date;
        this.id = id;
        exams.addAll(examList);

        // calculates the grade point average
        for (Exam exam : exams) {
            total += exam.grade;
        }
        total /= exams.size();
    }
    // returns the student with all his attributes
    @Override
    public String toString() {
        return name + "(BDay: " + date + ", Mat.-Nr: " + id + ", Avg. Grade: " + total + ")";
    }
}
