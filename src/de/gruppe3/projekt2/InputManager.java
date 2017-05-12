package de.gruppe3.projekt2;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by jonasliske on 12.05.17.
 */
public class InputManager {
    static String readString(Scanner scanner, ValidationMethod validationMethod) {
        String input = scanner.nextLine();

        if(!validationMethod.validate(input)) {
            return null;
        } else return input;
    }

    static int readInt(Scanner scanner, ValidationMethod validationMethod) {
        String input = scanner.nextLine();

        int inInt = Integer.parseInt(input);

        if(!validationMethod.validate(inInt)) {
            return -1;
        } else return inInt;
    }

    static float readFloat(Scanner scanner, ValidationMethod validationMethod) {
        String input = scanner.nextLine();

        float inFloat = Float.parseFloat(input);

        if(!validationMethod.validate(inFloat)) {
            return -1;
        } else return inFloat;
    }

    @FunctionalInterface
    public interface ValidationMethod {
        boolean validate(Object o);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(readString(sc, Validator.valName));
    }
}
