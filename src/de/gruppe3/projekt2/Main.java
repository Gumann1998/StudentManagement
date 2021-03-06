package de.gruppe3.projekt2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static de.gruppe3.projekt2.OutputHelper.SortingCriterium.SORT_BY_GRADE;
import static de.gruppe3.projekt2.OutputHelper.SortingCriterium.SORT_BY_NAME;

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
     * Loads save file and then starts program loop.
     *
     * @param args not used
     */
    public static void main(String... args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("saveFile.txt"));

            //Why map? Exams store id of their holder, with this map the holder is easily queried
            Map<Integer, Student> idToStudentMap = new HashMap<>();

            //Read next line into currentLine until end of file
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                //Split line into variables described in format spec
                String[] lineValues = currentLine.split(" ");

                /* Format of save file:
                 * One data object per line
                 * Student: s [id] [firstName] [secondName] [...] [lastName] [birthday]
                 * Exam: e [holderId] [subjectFirstWord] [...] [subjectLastWord] [grade] */

                switch (lineValues[0]) {
                    case "s":
                        //Validate id
                        int id = Integer.parseInt(lineValues[1]); //If this is not a number, exception is caught below
                        if (!Validator.validateID(id)) {
                            System.err.println("Die ID eines Studenten ist nicht korrekt.");
                            break;
                        }

                        //Name can have arbitrarily many secondary names, need to calculate last name index
                        int nameEndIndex = lineValues.length - 2; //Last index (length - 1) is the birthday
                        String name = lineValues[2]; //Add first name to whole name

                        //Append the rest of the names
                        for (int i = 3; i <= nameEndIndex; i++) {
                            name += " " + lineValues[i];
                        }

                        //Validate name and birthday
                        if (!Validator.validateName(name)) {
                            System.err.println("Der Name eines Studenten ist nicht valide!");
                            break;
                        }

                        String birthday = lineValues[lineValues.length - 1];
                        if (!Validator.validateBirthday(birthday)) {
                            System.err.println("Der Geburtstag eines Studenten ist nicht valide.");
                            break;
                        }

                        //Add new student to map
                        Student newStudent = new Student(name, birthday, id, Collections.emptyList());
                        idToStudentMap.put(id, newStudent);
                        break;
                    case "e":
                        //Get associated student for this exam
                        int studentId = Integer.parseInt(lineValues[1]);
                        Student examHolder = idToStudentMap.get(studentId);

                        if (examHolder == null) {
                            System.err.println("Zu einer Prüfung wurde kein Student gefunden!");
                            break;
                        }

                        int subjectEndIndex = lineValues.length - 2; //Last index is grade
                        String subject = lineValues[2]; //First word of subject

                        //Add all remaining words to the subject name
                        for (int i = 3; i <= subjectEndIndex; i++) {
                            subject += " " + lineValues[i];
                        }

                        //Validate subject
                        if (!Validator.validateSubject(subject)) {
                            System.err.println("Der Vorlesungsname einer Prüfung ist nicht korrekt!");
                            break;
                        }

                        //Validate grade
                        float grade = Float.parseFloat(lineValues[lineValues.length - 1]);
                        if (!Validator.validateGrade(grade)) {
                            System.err.println("Die Note einer Prüfung ist nicht korrekt.");
                            break;
                        }

                        //Add new exam to its holder
                        Exam newExam = new Exam(grade, subject);
                        examHolder.addExam(newExam);
                }
            }

            reader.close();

            System.out.println("Aus der Speicherdatei wurden " + idToStudentMap.size() + " Studenten eingelesen.\n");
            students.addAll(idToStudentMap.values()); //Add all students from the file to the main set
        } catch (IOException e) { //When file not found or permission denied
            System.out.println("Es wurde keine Speicherdatei gefunden. Wir fangen von ganz vorne an.\n");
        } catch (NumberFormatException e) { //When wrong id or grade is supplied
            System.err.println("Die Speicherdatei enthält inkorrekte Daten!\n");
        } catch (IndexOutOfBoundsException e) { //When wrong number of args is used
            System.err.println("Die Speicherdatei enthält nicht die richtige Zahl an Argumenten!\n");
        }

        loop();
    }

    /**
     * Shows menu and receives commands until user exits program.
     * Repeats until exit command.
     */
    private static void loop() {
        OutputHelper.print("Hauptmenü: Was möchten Sie tun? \n" +
                "1: Studenten zur Liste hinzufügen \n" +
                "2: Studenten aus der Liste entfernen \n" +
                "3: Studenten auswählen und bearbeiten \n" +
                "4: Studenten ausgeben \n" +
                "5: Speichern & Beenden");

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

        loop();
    }

    /**
     * Saves the list of students and ends the program
     */
    private static void exit() {
        OutputHelper.print("Programm wurde erfolgreich beendet");

        //No need to save when nothing is there
        if (students.size() != 0) {
            //Save students in text file
            File saveFile = new File("saveFile.txt");

            //Sicherstellen, dass SaveFile existiert
            if (!saveFile.exists()) {
                try {
                    saveFile.createNewFile();
                } catch (IOException e) {
                    System.err.println("Could not save students to file - no permission!");
                    System.exit(1);
                }
            }

            //Daten in Savefile schreiben
            try {
                PrintWriter writer = new PrintWriter(saveFile);

                //First, write student, then his/her exams
                for (Student student : students) {
                    writer.write("s " + student.getId() + " " + student.getName() + " " + student.getBirthday() + "\n");

                    for (Exam exam : student.getExams()) {
                        writer.write("e " + student.getId() + " " + exam.subject + " " + exam.grade + "\n");
                    }
                }

                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        System.exit(0);
    }

    /**
     * Lists all students and lets users choose the ordering criteria
     */
    private static void listStudents() {
        OutputHelper.makeStudentTable(students);

        OutputHelper.print("Was möchten Sie tun?");
        OutputHelper.print("1: Nach Name sortieren");
        OutputHelper.print("2: Nach Note sortieren");
        OutputHelper.print("3: Menü verlassen");

        //Validate input, if incorrect start over
        int choice = InputHelper.readInt(sc, i -> (int)i >= 1 && (int)i <= 3);
        if (choice == -1) {
            OutputHelper.printError("Dies ist keine mögliche Option!");
            listStudents();
            return;
        }

        //1: Nach Name sortieren, 2: Nach Note sortieren, 3: Menü verlassen (überspringt switch, springt zu Ende)
        switch (choice) {
            case 1:
                OutputHelper.makeStudentTable(students, SORT_BY_NAME);
                break;
            case 2:
                OutputHelper.makeStudentTable(students, SORT_BY_GRADE);
                break;
        }
    }

    private static void editStudent() {
        OutputHelper.print("Sie können aus den folgenden Studenten auswählen:");
        OutputHelper.makeStudentTable(students);
        OutputHelper.print("Studenten können mit Name oder ID ausgewählt werden.");

        Set<Student> selected;

        String input = sc.next();
        if (Validator.validateName(input)) { //Check if student with input name exists
            selected = students.stream().filter(s -> s.getName().equals(input)).collect(Collectors.toSet());

            if (selected.size() > 1) { //More than one student with same name
                OutputHelper.printError("Es gibt mehrere Studenten mit diesem Namen, " +
                        "bitte geben Sie die ID  Betroffenen ein.");
                editStudent();
                return;
            } else if (selected.size() == 0) { //Nobody found
                OutputHelper.printError("Es wurde kein Student mit dem Namen '" + input + "' gefunden.");
                editStudent();
                return;
            } // Else: Jump over rest of if-statements and start editing
        } else if (input.matches("[0-9]+") //Check if student was referenced by ID
                && Validator.validateID(Integer.parseInt(input))) {
            int id = Integer.parseInt(input);

            selected = students.stream().filter(s -> s.getId() == id).collect(Collectors.toSet());

            if (selected.size() == 0) { //Nobody found
                OutputHelper.printError("Es wurde kein Student mit ID '" + id + "' gefunden.");
                editStudent();
                return;
            } else if (selected.size() > 1) { //Cannot possibly be because of hashset and id as hash!!
                OutputHelper.printError("Es wurden mehrere Studenten mit derselben ID gefunden. " +
                        "Bitte entfernen Sie alle außer einen.");
                return;
            } //Else: jump to editing
        } else { //Wrong ID
            OutputHelper.printError("Bitte geben Sie eine valide ID oder einen validen Namen ein!");
            editStudent();
            return;
        }

        //Selected stream HAS to have students because of validation above - if not: Illegal State
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

        OutputHelper.printf("Notendurchschnitt: %1.2f", studentToEdit.getAvgGrade());

        boolean editGrades = true;

        do {
            OutputHelper.makeExamTable(studentToEdit.getExams());

            OutputHelper.print("Was möchten Sie tun?");
            OutputHelper.print("1: Note hinzufügen");
            OutputHelper.print("2: Note löschen");
            OutputHelper.print("3: Note bearbeiten");
            OutputHelper.print("4: Weiter im Programm\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\u001B[35m<-- Geiler Wortwitz right here ;)\u001B[0m");
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

        //Remove replaced exams and add new one because exam fields are final

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

        Set<Exam> toRemove = examsToEdit.stream()
                .filter(e -> e.subject.equals(subject))
                .collect(Collectors.toSet());

        examsToEdit.removeAll(toRemove);
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

        //
        //  See editStudent for explanation
        //

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
