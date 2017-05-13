package de.gruppe3.projekt2;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    /**
     * One central scanner to read all inputs
     */
    private static final Scanner sc = new Scanner(System.in).useDelimiter("\n");

    /**
     * Stores all students that have been added so far
     */
    private static Set<Student> students = new HashSet<>();

    /**
     * Shows menu and receives commands.
     */
    public static void main(String... args) {
        OutputHelper.print("Hauptmenü: Was möchten Sie tun? \n" +
                "1: Studenten zur Liste hinzufügen \n" +
                "2: Studenten aus der Liste entfernen \n" +
                "3: Studenten auswählen und bearbeiten \n" +
                "4: Studenten ausgeben \n" +
                "5: Abbruch ");

        int selection = sc.nextInt();
        switch (selection) {
            case 1:
                OutputHelper.enterSubMenu();
                addStudent();
                OutputHelper.leaveSubMenu();
                break;

            case 2:
                OutputHelper.enterSubMenu();
                removeStudent();
                OutputHelper.leaveSubMenu();
                break;

            case 3:
                OutputHelper.enterSubMenu();
                editStudent();
                OutputHelper.leaveSubMenu();
                break;

            case 4:
                OutputHelper.enterSubMenu();
                listStudents();
                OutputHelper.leaveSubMenu();
                break;

            case 5:
                exit();
                break;
        }

        main();
    }

    private static void exit() {
        OutputHelper.print("Programm wurde erfolgreich beendet");
        System.exit(0);
    }

    private static void listStudents() {
        OutputHelper.makeStudentTable(students);
    }

    private static void editStudent() {
        OutputHelper.print("Sie können aus den folgenden Studenten auswählen:");
        OutputHelper.makeStudentTable(students);
        OutputHelper.print("Studenten können mit Name oder ID ausgewählt werden.");

        Set<Student> selected;

        String input = sc.next();
        if (Validator.validateName(input)) {
            selected = students.stream().filter(s -> s.getName().equals(input)).collect(Collectors.toSet());

            if (selected.size() > 1) {
                OutputHelper.printError("Es gibt mehrere Studenten mit diesem Namen, " +
                        "bitte geben Sie die ID  Betroffenen ein.");
                editStudent();
                return;
            } else if (selected.size() == 0) {
                OutputHelper.printError("Es wurde kein Student mit dem Namen '" + input + "' gefunden.");
                editStudent();
                return;
            } // Else: Jump over rest of if-statements and start editing
        } else if (input.matches("[0-9]+")
                && Validator.validateID(Integer.parseInt(input))) {
            int id = Integer.parseInt(input);

            selected = students.stream().filter(s -> s.getId() == id).collect(Collectors.toSet());

            if (selected.size() == 0) {
                OutputHelper.printError("Es wurde kein Student mit ID '" + id + "' gefunden.");
                editStudent();
                return;
            } else if (selected.size() > 1) {
                OutputHelper.printError("Es wurden mehrere Studenten mit derselben ID gefunden. " +
                        "Bitte entfernen Sie alle außer einen.");
                return;
            } //Else: jump to editing
        } else {
            OutputHelper.printError("Bitte geben Sie eine valide ID oder einen validen Namen ein!");
            editStudent();
            return;
        }

        Student studentToEdit = selected.stream().findAny().orElseThrow(IllegalStateException::new);

        OutputHelper.print("Name: " + studentToEdit.getName());
        String newName = InputHelper.readString(sc, Validator.valName);
        if (newName != null) studentToEdit.setName(newName);

        OutputHelper.print("ID: " + studentToEdit.getId());
        int newID = InputHelper.readInt(sc, Validator.valID);
        if (newID != -1) studentToEdit.setId(newID);

        OutputHelper.print("Geburtsdatum: " + studentToEdit.getBirthday());
        String newBirthday = InputHelper.readString(sc, Validator.valBirthday);
        if (newBirthday != null) studentToEdit.setBirthday(newBirthday);

        OutputHelper.print("Notendurchschnitt: " + studentToEdit.getAvgGrade());
        OutputHelper.makeExamTable(studentToEdit.getExams());

        boolean editGrades = true;

        do {
            OutputHelper.print("Was möchten Sie tun?");
            OutputHelper.print("1: Note hinzufügen");
            OutputHelper.print("2: Note löschen");
            OutputHelper.print("3: Note bearbeiten");
            OutputHelper.print("4: Weiter im Programm          <-- Geiler Wortwitz right here ;)");
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    OutputHelper.enterSubMenu();
                    addGrade(studentToEdit);
                    OutputHelper.leaveSubMenu();
                    break;
                case 2:
                    OutputHelper.enterSubMenu();
                    removeGrade(studentToEdit);
                    OutputHelper.leaveSubMenu();
                    break;
                case 3:
                    OutputHelper.enterSubMenu();
                    editGrade(studentToEdit);
                    OutputHelper.leaveSubMenu();
                    break;
                default:
                    OutputHelper.enterSubMenu();
                    OutputHelper.print("Dies ist keine gültige Option!");
                    OutputHelper.leaveSubMenu();
                    break;
                case 4:
                    editGrades = false;
            }
        } while (editGrades);
    }

    private static void editGrade(Student studentToEdit) {
        OutputHelper.print("Bitte geben Sie den Vorlesungsnamen ein.");
        String subject = InputHelper.readString(sc, Validator.valSubject);
        if (subject == null) {
            OutputHelper.printError("Das ist keine gültige Vorlesung!");
            return;
        }

        // checks if the grade really exists
        OutputHelper.print("Geben Sie die neue Note ein.");
        float grade = InputHelper.readFloat(sc, Validator.valGrade);
        if (grade == -1) {
            OutputHelper.printError("Das ist keine valide Note!");
            return;
        }

        Set<Exam> examsToEdit = studentToEdit.getExams();

        examsToEdit = examsToEdit.stream()
                .filter(e -> e.subject.equals(subject))
                .collect(Collectors.toSet());

        studentToEdit.getExams().removeAll(examsToEdit);

        studentToEdit.addExam(new Exam(grade, subject));
    }

    private static void removeGrade(Student studentToEdit) {
        OutputHelper.print("Bitte geben Sie den Vorlesungsnamen ein.");
        String subject = InputHelper.readString(sc, Validator.valSubject);
        if (subject == null) {
            OutputHelper.printError("Das ist keine gültige Vorlesung!");
            return;
        }

        Set<Exam> examsToEdit = studentToEdit.getExams();

        examsToEdit.stream()
                .filter(e -> e.subject.equals(subject))
                .forEach(examsToEdit::remove);
    }

    private static void addGrade(Student studentToEdit) {
        // checks if the input is an actual subject
        OutputHelper.print("Bitte geben Sie den Vorlesungsnamen ein.");
        String subject = InputHelper.readString(sc, Validator.valSubject);
        if (subject == null) {
            OutputHelper.printError("Das ist keine gültige Vorlesung!");
            return;
        }

        // checks if the grade really exists
        OutputHelper.print("Geben Sie die Note ein.");
        float grade = InputHelper.readFloat(sc, Validator.valGrade);
        if (grade == -1) {
            OutputHelper.printError("Das ist keine valide Note!");
            return;
        }

        Exam examToAdd = new Exam(grade, subject);
        studentToEdit.addExam(examToAdd);
    }

    private static void removeStudent() {
        OutputHelper.print("Sie können aus den folgenden Studenten auswählen:");
        OutputHelper.makeStudentTable(students);
        OutputHelper.print("Studenten können mit Name oder ID ausgewählt werden.");

        String input = sc.next();
        if (Validator.validateName(input)) {
            Set<Student> selected = students.stream().filter(s -> s.getName().equals(input)).collect(Collectors.toSet());

            if (selected.size() > 1) {
                OutputHelper.printError("Es gibt mehrere Studenten mit diesem Namen, " +
                        "bitte geben Sie die ID  Betroffenen ein.");
                removeStudent();
            } else if (selected.size() == 0) {
                OutputHelper.printError("Es wurde kein Student mit dem Namen '" + input + "' gefunden.");
                removeStudent();
            } else {
                students.removeAll(selected); // Only 1 student is in here, but sets cannot easily give out one student
            }
        } else if (input.matches("[0-9]+")
                && Validator.validateID(Integer.parseInt(input))) {
            int id = Integer.parseInt(input);

            Set<Student> selected = students.stream().filter(s -> s.getId() == id).collect(Collectors.toSet());

            if (selected.size() != 0) students.removeAll(selected);
            else {
                OutputHelper.printError("Es wurde kein Student mit ID '" + id + "' gefunden.");
                removeStudent();
            }
        } else {
            OutputHelper.printError("Bitte geben Sie eine valide ID oder einen validen Namen ein!");
            removeStudent();
        }
    }

    private static void addStudent() {
        OutputHelper.print("Geben Sie einen Namen ein");
        String name = InputHelper.readString(sc, Validator.valName);

        //Check if name is usable, else start over
        if (name == null) {
            OutputHelper.printError("Geben Sie einen echten Namen ein!");
            addStudent();
            return;
        }

        OutputHelper.print("Geben Sie das Geburtsdatum ein");
        String birthday = InputHelper.readString(sc, Validator.valBirthday);
        if (birthday == null) {
            OutputHelper.printError("Geben Sie ein richtiges Geburtsdatum an!");
            addStudent();
            return;
        }

        OutputHelper.print("Geben Sie die ID ein");
        int id = InputHelper.readInt(sc, Validator.valID);
        if (id == -1) {
            OutputHelper.printError("Geben Sie eine zulässige ID ein!");
            addStudent();
            return;
        }

        OutputHelper.print("Geben Sie die Noten zu den Prüfungen ein");

        List<Exam> exams = new LinkedList<>();

        // checks if user wants to add an exam
        while (true) {
            OutputHelper.print("Wollen Sie noch eine Prüfung eingeben? (j/n)");
            String wantsToAdd = InputHelper.readString(sc, s -> ((String) s).matches("[jn]"));
            if (wantsToAdd == null) {
                OutputHelper.printError("Bitte geben Sie j oder n ein!");
                continue;
            } else if (wantsToAdd.equals("n")) break;

            // checks if the input is an actual subject
            OutputHelper.print("Geben Sie den Vorlesungsnamen ein.");
            String subject = InputHelper.readString(sc, Validator.valSubject);
            if (subject == null) {
                OutputHelper.printError("Das ist keine gültige Vorlesung!");
                continue;
            }
            // checks if the grade really exists
            OutputHelper.print("Geben Sie die Note ein.");
            float grade = InputHelper.readFloat(sc, Validator.valGrade);
            if (grade == -1) {
                OutputHelper.printError("Das ist keine valide Note!");
                continue;
            }

            exams.add(new Exam(grade, subject));
        }
        // creates new student with all his attributes
        Student studentToAdd = new Student(name, birthday, id, exams);
        students.add(studentToAdd);
    }
}
