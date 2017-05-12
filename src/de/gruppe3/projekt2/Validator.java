package de.gruppe3.projekt2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * Created by guhennin on 12.05.17.
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

            if (date.after(Date.from(Instant.now())))
                return false;

            if (date.before(format.parse("01.01.1970")))
                return false;

        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    static boolean validateName(String name) {
        if (name.matches("(\\p{Lu}\\p{Ll}+)([\\- ](\\p{Lu}\\p{Ll}+))+"))
            return true;
        else
            return false;
    }

    static boolean validateSubject(String subject) {
        if (subject.matches("(\\p{Lu}\\p{Ll}+)( ([\\p{L}0-9]+))*")) {
            return true;
        } else
            return false;
    }

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
