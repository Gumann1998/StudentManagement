package de.gruppe3.projekt2;

import java.util.*;

public class Main {
    /**
     * One central scanner to read all inputs
     */
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Stores all students that have been added so far
     */
    private static Set<Student> students = new HashSet<>();

    /**
     * Shows menu and receives commands.
     */
    public static void main(String... args) {
        System.out.println("Hauptmenü: Was möchten Sie tun? \n" +
                "1: Studenten zur Liste hinzufügen \n" +
                "2: Studenten aus der Liste entfernen \n" +
                "3: Studenten auswählen und bearbeiten \n" +
                "4: Studenten ausgeben \n" +
                "5: Abbruch ");

        int selection = sc.nextInt();
        switch (selection) {

            case 1:
                addStudent();
                break;

            case 2:
                removeStudent();
                break;

            case 3:
                editStudent();
                break;

            case 4:
                listStudents();
                break;

            case 5:
                exit();
                break;
        }

        main();
    }

    private static void exit() {
        System.out.println("Programm wurde erfolgreich beendet");
        System.exit(0);
    }

    private static void listStudents() {
        students.forEach(System.out::println);
    }

    private static void editStudent() {
        System.out.println("Welchen Studenten möchten Sie auswählen und bearbeiten?");
        String name = InputManager.readString(sc, Validator.valName);
        if(name == null) {
            System.out.println("Bitte geben Sie einen validen Namen ein!");
            editStudent();
            return;
        }

        Student studentToEdit = students.stream().filter(s -> s.getName().equals(name)).findAny().orElse(null);
        if (studentToEdit == null) {
            System.out.println("Der gesuchte Student wurde nicht gefunden.");
            return;
        }

        System.out.println("Name: " + studentToEdit.getName());
        String newName = InputManager.readString(sc, Validator.valName);
        if(newName != null) studentToEdit.setName(newName);

        System.out.println("ID: " + studentToEdit.getId());
        int newID = InputManager.readInt(sc, Validator.valID);
        if(newID != -1) studentToEdit.setId(newID);

        System.out.println("Geburtsdatum: " + studentToEdit.getBirthday());
        String newBirthday = InputManager.readString(sc, Validator.valBirthday);
        if(newBirthday != null) studentToEdit.setBirthday(newBirthday);

        System.out.println("Notendurchschnitt: " + studentToEdit.getAvgGrade());
        studentToEdit.getExams().forEach(System.out::println);

        boolean editGrades = true;

        do {
            System.out.println("Was möchten Sie tun?\n" +
                    "1: Note hinzufügen\n" +
                    "2: Note löschen\n" +
                    "3: Note bearbeiten\n" +
                    "4: Weiter im Programm          <-- Geiler Wortwitz right here ;)");
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    addGrade(studentToEdit);
                    break;
                case 2:
                    removeGrade(studentToEdit);
                    break;
                case 3:
                    editGrade(studentToEdit);
                    break;
                default:
                    System.out.println("Dies ist keine gültige Option!");
                    break;
                case 4:
                    editGrades = false;
            }
        } while (editGrades);
    }

    private static void editGrade(Student studentToEdit) {
        System.out.println("Bitte geben Sie den Vorlesungsnamen ein.");
        String subject = InputManager.readString(sc, Validator.valSubject);
        if (subject == null) {
            System.out.println("Das ist keine gültige Vorlesung!");
            return;
        }

        // checks if the grade really exists
        System.out.println("Geben Sie die neue Note ein.");
        float grade = InputManager.readFloat(sc, Validator.valGrade);
        if(grade == -1) {
            System.out.println("Das ist keine valide Note!");
            return;
        }

        Set<Exam> examsToEdit = studentToEdit.getExams();

        examsToEdit.stream()
                .filter(e -> e.subject.equals(subject))
                .forEach(examsToEdit::remove);

        studentToEdit.addExam(new Exam(grade, subject));
    }

    private static void removeGrade(Student studentToEdit) {
        System.out.println("Bitte geben Sie den Vorlesungsnamen ein.");
        String subject = InputManager.readString(sc, Validator.valSubject);
        if (subject == null) {
            System.out.println("Das ist keine gültige Vorlesung!");
            return;
        }

        Set<Exam> examsToEdit = studentToEdit.getExams();

        examsToEdit.stream()
                .filter(e -> e.subject.equals(subject))
                .forEach(examsToEdit::remove);
    }

    private static void addGrade(Student studentToEdit) {
        // checks if the input is an actual subject
        System.out.println("Bitte geben Sie den Vorlesungsnamen ein.");
        String subject = InputManager.readString(sc, Validator.valSubject);
        if (subject == null) {
            System.out.println("Das ist keine gültige Vorlesung!");
            return;
        }

        // checks if the grade really exists
        System.out.println("Geben Sie die Note ein.");
        float grade = InputManager.readFloat(sc, Validator.valGrade);
        if(grade == -1) {
            System.out.println("Das ist keine valide Note!");
            return;
        }

        Exam examToAdd = new Exam(grade, subject);
        studentToEdit.addExam(examToAdd);
    }

    private static void removeStudent() {
        System.out.println("Welchen Studenten möchten Sie entfernen?");
        String name = InputManager.readString(sc, Validator.valName);
        if(name == null) {
            System.out.println("Bitte geben Sie einen validen Namen ein!");
            removeStudent();
            return;
        }

        students.stream()
                .filter(s -> s.getName().equals(name))
                .forEach(students::remove);
    }

    private static void addStudent() {
        System.out.println("Geben Sie einen Namen ein");
        String name = InputManager.readString(sc, Validator.valName);

        //Check if name is usable, else start over
        if(name == null) {
            System.out.println("Geben Sie einen echten Namen ein!");
            addStudent();
            return;
        }

        System.out.println("Geben Sie das Geburtsdatum ein");
        String birthday = InputManager.readString(sc, Validator.valBirthday);
        if(birthday == null) {
            System.out.println("Geben Sie ein richtiges Geburtsdatum an!");
            addStudent();
            return;
        }

        System.out.println("Geben Sie die ID ein");
        int id = InputManager.readInt(sc, Validator.valID);
        if (id == -1) {
            System.out.println("Geben Sie eine zulässige ID ein!");
            addStudent();
            return;
        }

        System.out.println("Geben Sie die Noten zu den Prüfungen ein");

        List<Exam> exams = new LinkedList<>();

        // checks if user wants to add an exam
        while (true) {
            System.out.println("Wollen Sie noch eine Prüfung eingeben? (j/n)");
            String wantsToAdd = InputManager.readString(sc, s -> ((String) s).matches("[jn]"));
            if(wantsToAdd == null) {
                System.out.println("Bitte geben Sie j oder n ein!");
                continue;
            } else if(wantsToAdd.equals("n")) break;

            // checks if the input is an actual subject
            System.out.println("Geben Sie den Vorlesungsnamen ein.");
            String subject = InputManager.readString(sc, Validator.valSubject);
            if (subject == null) {
                System.out.println("Das ist keine gültige Vorlesung!");
                continue;
            }
            // checks if the grade really exists
            System.out.println("Geben Sie die Note ein.");
            float grade = InputManager.readFloat(sc, Validator.valGrade);
            if(grade == -1) {
                System.out.println("Das ist keine valide Note!");
                continue;
            }

            exams.add(new Exam(grade, subject));
        }
        // creates new student with all his attributes
        Student studentToAdd = new Student(name, birthday, id, exams);
        students.add(studentToAdd);
    }
}
