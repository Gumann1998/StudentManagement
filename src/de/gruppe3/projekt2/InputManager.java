package de.gruppe3.projekt2;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Helper class to check if input is correct
 */
class InputManager {
    /**
     * Reads a string with the provided scanner, checks if valid and returns it.
     *
     * @param scanner The scanner used for - you guessed it - SCANNING!
     * @param validationMethod The method used to validate the input
     * @return The input if valid, null if invalid
     */
    static String readString(Scanner scanner, ValidationMethod validationMethod) {
        String input = scanner.nextLine();

        if(!validationMethod.validate(input)) {
            return null;
        } else return input;
    }

    /**
     * Reads a positive (or 0) int, validates it, returns it.
     *
     * @param scanner The scanner used for - you guessed it - SCANNING!
     * @param validationMethod The method used to validate the input
     * @return The input if valid, -1 if invalid
     */
    static int readInt(Scanner scanner, ValidationMethod validationMethod) {
        String input = scanner.nextLine();

        int inInt = Integer.parseInt(input);

        if(!validationMethod.validate(inInt)) {
            return -1;
        } else return inInt;
    }

    /**
     * Reads a float >= 0, validates and returns it.
     *
     * @param scanner The scanner used for - you guessed it - SCANNING!
     * @param validationMethod The method used to validate the input
     * @return The input if valid, -1 if invalid
     */
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
    interface ValidationMethod {
        /**
         * Check if input matches validation criteria.
         *
         * @param o The input to validate.
         * @return true if valid, false if invalid
         */
        boolean validate(Object o);
    }
}
