package de.gruppe3.projekt2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jonasliske on 12.05.17.
 */
public class Student {

    private String name;
    private int date;
    private int id;
    private Set<Exam> exams = new HashSet<>();
    private double total = 0.0;

    public Student(String students, int date, int id, List<Exam> examList) {
        this.name = students;
        this.date = date;
        this.id = id;
        exams.addAll(examList);


        for (Exam exam : exams) {
            total += exam.grade;
        }
        total /= exams.size();

    }

    @Override
    public String toString() {
        return name + "(BDay: " + date + ", Mat.-Nr: " + id + ", Avg. Grade: " + total + ")";
    }
}
