package de.gruppe3.projekt2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

class Validator {
    /*
     * Predefined lambdas, ready to be used in the InputManager (or elsewhere)
     */
    static InputManager.ValidationMethod valBirthday =
            (object) -> validateBirthday((String) object);

    static InputManager.ValidationMethod valName =
            (object) -> validateName((String) object);

    static InputManager.ValidationMethod valID =
            (object) -> validateID((int) object);

    static InputManager.ValidationMethod valGrade =
            (object) -> validateGrade((double) object);

    static InputManager.ValidationMethod valSubject =
            (object) -> validateSubject((String) object);

    private static boolean validateBirthday(String birthday) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        try {
            Date date = format.parse(birthday);

            if (date.after(Date.from(Instant.now())))       //Students typically aren't older than 40-50 and can't be born in the future.
                return false;

            if (date.before(format.parse("01.01.1970")))
                return false;

        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the name has at least one uppercase and several lowercase letters and if there is a dash.
     * @param name uppercase and lowercase letters and dash; A-Z, a-z, '-'
     * @return true
     */
    private static boolean validateName(String name) {
        return name.matches("(\\p{Lu}\\p{Ll}+)([\\- ](\\p{Lu}\\p{Ll}+))+");
    }

    /**
     * Checks if the subject consists of letters and numbers and returns true.
     * @param subject uppercase and lowercase letters and numbers from 0-9; A-Z, a-z, 0-9
     * @return
     */
    private static boolean validateSubject(String subject) {
        return subject.matches("(\\p{Lu}\\p{Ll}+)( ([\\p{L}0-9]+))*");
    }

    /**
     * Checks if the grades of a student are below 5.0 and above 1.0. If both statemnts are true, then it returns true.
     * @param grades values 1.0-5.0
     * @return true
     */
    private static boolean validateGrade(double grades) {
        return grades <= 5.0 && grades >= 1.0;

    }

    private static boolean validateID(int id) {
        return id > 0;
    }
}
