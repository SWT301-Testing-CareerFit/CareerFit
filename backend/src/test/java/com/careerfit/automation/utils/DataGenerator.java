package com.careerfit.automation.utils;

/**
 * Utility class to generate dynamic test data based on timestamp
 * to prevent duplicate data issues during test execution (e.g., registration).
 */
public class DataGenerator {

    private DataGenerator() {
        // Utility class: prevent instantiation
    }

    /**
     * Returns the current timestamp in milliseconds as a String.
     */
    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Generates a random email with timestamp: test_{timestamp}@gmail.com
     */
    public static String randomEmail() {
        return "test_" + getTimestamp() + "@gmail.com";
    }

    /**
     * Generates a random email with custom prefix and timestamp: {prefix}_{timestamp}@gmail.com
     *
     * @param prefix custom prefix
     * @return generated email
     */
    public static String randomEmail(String prefix) {
        return prefix + "_" + getTimestamp() + "@gmail.com";
    }

    /**
     * Generates a random username with timestamp: user_{timestamp}
     */
    public static String randomUsername() {
        return "user_" + getTimestamp();
    }

    /**
     * Generates a random username with custom prefix and timestamp: {prefix}_{timestamp}
     *
     * @param prefix custom prefix
     * @return generated username
     */
    public static String randomUsername(String prefix) {
        return prefix + "_" + getTimestamp();
    }

    /**
     * Generates a random Vietnamese mobile phone number (10 digits starting with 09).
     */
    public static String randomPhoneNumber() {
        long random8Digits = (long) (Math.random() * 90000000L) + 10000000L;
        return "09" + random8Digits;
    }

    /**
     * Generates a random string with custom prefix: {prefix}_{timestamp}
     *
     * @param prefix custom prefix
     * @return generated string
     */
    public static String randomString(String prefix) {
        return prefix + "_" + getTimestamp();
    }
}
