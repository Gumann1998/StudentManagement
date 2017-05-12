package de.gruppe3.projekt2;

import java.util.*;

/**
 * Created by jonasliske on 12.05.17.
 */
public class Main {
    private static final Scanner sc = new Scanner(System.in);

    static Set<Student> students = new HashSet<>();

    public static void main(String[] args) {

        System.out.println(" Hauptmenü: Was möchten Sie tun? \n " +
                "1: Studenten zur Liste hinzufügen \n " +
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
        String name = sc.nextLine();
    }

    private static void removeStudent() {
        System.out.println("Welchen Studenten möchten Sie entfernen?");
        String name = sc.nextLine();
    }

    private static void addStudent() {
        System.out.println("Geben Sie einen Namen ein");
        String name = sc.nextLine();

        System.out.println("Geben Sie das Geburtsdatum ein");
        int date = sc.nextInt();

        System.out.println("Geben Sie die ID ein");
        int id = sc.nextInt();

        System.out.println("Geben Sie die Noten zu den Prüfungen ein");

        List<Exam> exams = new LinkedList<>();


        while (true) {

            String subject = sc.nextLine();
            int grade = sc.nextInt();
        }


    }
}
