package de.gruppe3.projekt2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * Created by Henning on 12.05.17.
 */

public class Validator {
    public static InputManager.ValidationMethod valBirthday =
            (object) -> validateBirthday((String) object);

    public static InputManager.ValidationMethod valName =
            (object) -> validateName((String) object);

    public static InputManager.ValidationMethod valID =
            (object) -> validateID((int) object);

    public static InputManager.ValidationMethod valGrade =
            (object) -> validateGrade((double) object);

    public static InputManager.ValidationMethod valSubject =
            (object) -> validateSubject((String) object);

    static boolean validateBirthday(String birthday) {
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
    static boolean validateName(String name) {
        if (name.matches("(\\p{Lu}\\p{Ll}+)([\\- ](\\p{Lu}\\p{Ll}+))+"))
            return true;
        else
            return false;
    }

    /**
     * Checks if the subject consists of letters and numbers and returns true.
     * @param subject uppercase and lowercase letters and numbers from 0-9; A-Z, a-z, 0-9
     * @return
     */
    static boolean validateSubject(String subject) {
        if (subject.matches("(\\p{Lu}\\p{Ll}+)( ([\\p{L}0-9]+))*")) {
            return true;
        } else
            return false;
    }

    /**
     * Checks if the grades of a student are below 5.0 and above 1.0. If both statemnts are true, then it returns true.
     * @param grades values 1.0-5.0
     * @return true
     */
    static boolean validateGrade(double grades) {
        if (grades <= 5.0 && grades >= 1.0)
            return true;
        return false;

    }

    static boolean validateID(int id) {
        if (id <= 0)
            return false;
        return true;
    }
}
