package de.gruppe3.projekt2;

/**
 * creates the exam which contains the grade and subject
 */
class Exam {
    final float grade;
    final String subject;

    Exam(float grade, String subject) {
        this.grade = grade;
        this.subject = subject;
    }

    @Override
    public String toString() {
        return subject + " (Grade: " + grade + ")";
    }
}
