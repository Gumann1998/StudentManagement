package de.gruppe3.projekt2;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Student {

    private String name;
    private String birthday;
    private int id;
    private Set<Exam> exams = new HashSet<>();
    private float avgGrade;

    Student(String name, String birthday, int id, List<Exam> examList) {
        this.name = name;
        this.birthday = birthday;
        this.id = id;
        exams.addAll(examList);

        // calculates the grade point average
        float total = 0;
        for (Exam exam : exams) {
            total += exam.grade;
        }
        avgGrade = total / exams.size();
    }

    void setName(String name) {
        if (Validator.valName.validate(name)) this.name = name;
    }

    void setBirthday(String birthday) {
        if (Validator.valBirthday.validate(birthday)) this.birthday = birthday;
    }

    void setId(int id) {
        if (Validator.valID.validate(id)) this.id = id;
    }

    String getName() {
        return name;
    }

    String getBirthday() {
        return birthday;
    }

    int getId() {
        return id;
    }

    Set<Exam> getExams() {
        return exams;
    }

    float getAvgGrade() {
        return avgGrade;
    }

    /**
     * Nicely formats the sutdent's attributes into a string
     *
     * @return The string...
     */
    @Override
    public String toString() {
        return name + "(BDay: " + birthday + ", Mat.-Nr: " + id + ", Avg. Grade: " + avgGrade + ")";
    }

    /**
     * Compares birthday and name to check for equality.
     * No two students are allowed to have the same birthday AND name.
     *
     * @param o The object to check for equality
     * @return true if equal, false if different
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Student
                && ((Student) o).getName().equals(name)
                && ((Student) o).birthday.equals(birthday);
    }

    /**
     * Mashes together birthday and name to produce a hash.
     * No two students are allowed to have the same birthday AND name.
     *
     * @return The resulting hash code, die 2 most significant bytes sind ein Teil des birthday-Hashes,
     * die LSBs ein Teil des name-Hashes
     */
    @Override
    public int hashCode() {
        return ((short)birthday.hashCode()) << ((short)name.hashCode());
    }

    public void addExam(Exam examToAdd) {
        exams.add(examToAdd);
    }
}
