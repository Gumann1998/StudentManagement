package de.gruppe3.projekt2;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Helper class to check if input is correct
 */
public class InputManager {
    //checks the String input
    static String readString(Scanner scanner, ValidationMethod validationMethod) {
        String input = scanner.nextLine();

        if(!validationMethod.validate(input)) {
            return null;
        } else return input;
    }
    // checks the int input
    static int readInt(Scanner scanner, ValidationMethod validationMethod) {
        String input = scanner.nextLine();

        int inInt = Integer.parseInt(input);

        if(!validationMethod.validate(inInt)) {
            return -1;
        } else return inInt;
    }
    //checks the float input
    static float readFloat(Scanner scanner, ValidationMethod validationMethod) {
        String input = scanner.nextLine();

        float inFloat = Float.parseFloat(input);

        if(!validationMethod.validate(inFloat)) {
            return -1;
        } else return inFloat;
    }

    /**
     * Functional interface to allow validators in form of lambdas to be passed to the read methods.
     */
    @FunctionalInterface
    public interface ValidationMethod {
        /**
         * Check if input matches validation criteria.
         *
         * @param o The input to validate.
         * @return true if valid, false if invalid
         */
        boolean validate(Object o);
    }
}
