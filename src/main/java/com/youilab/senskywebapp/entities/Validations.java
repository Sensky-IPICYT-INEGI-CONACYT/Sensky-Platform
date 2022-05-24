package com.youilab.senskywebapp.entities;

import java.util.regex.Pattern;

public class Validations {

    // Regular expression that validates an email.
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    // Regular expression that validates a password.
    private static final String PASSWORD_REGEX = "^[a-z A-Z 0-9 .]{6,20}$";

    private static Pattern REGEX_VALIDATOR;

    /**
     * Verifies that the input number is in the range for a valid Latitude.
     * @param latitude the number to be validated.
     * @return true if the number is in the range, false otherwise.
     */
    public static Boolean validateLatitude(Double latitude){
        return latitude > -90d && latitude < 90d;
    }

    /**
     * Verifies that the input number is in the range for a valid Longitude.
     * @param longitude the number to be validated.
     * @return true if the number is in the range, false otherwise.
     */
    public static Boolean validateLongitude(Double longitude){
        return longitude > -180d && longitude < 180d;
    }

    /**
     * Uses the email regex for validating the input string format.
     * @param email The string to be validated.
     * @return true if the string is an email, false otherwise.
     */
    public static Boolean validateEmail(String email){
        REGEX_VALIDATOR = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        return REGEX_VALIDATOR.matcher(email).matches();
    }

    /**
     * Uses the password regex for validating the input string format.
     * @param password The string to be validated.
     * @return true if the string is a valid password, false otherwise.
     */
    public static Boolean validatePassword(String password){
        REGEX_VALIDATOR = Pattern.compile(PASSWORD_REGEX, Pattern.CASE_INSENSITIVE);
        return REGEX_VALIDATOR.matcher(password).matches();
    }
}
