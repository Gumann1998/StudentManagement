package de.gruppe3.projekt2;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * Created by guhennin on 12.05.17.
 */
public class Validator {
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

    static boolean validateGrades(double grades) {
        if (grades < 5.0 && grades > 1.0)
            return true;
        return false;

    }

    static boolean validateID(int id) {
        if (id <= 0)
            return false;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(validateBirthday("30.03.1998"));
        System.out.println(validateName("Hans Peter"));
        System.out.println(validateGrades(1.5));
        System.out.println(validateID(123));
    }
}
