package com.careerfit.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read configuration from config.properties.
 * Prioritizes System properties (e.g. -Dkey=value) over config.properties.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("[ConfigReader] Warning: File " + CONFIG_FILE + " not found in classpath.");
            }
        } catch (IOException e) {
            System.err.println("[ConfigReader] Error loading " + CONFIG_FILE + ": " + e.getMessage());
        }
    }

    private ConfigReader() {
        // Utility class: prevent instantiation
    }

    /**
     * Reads a configuration value by key.
     * System.getProperty(key) takes precedence over config.properties.
     *
     * @param key the property key
     * @return the resolved property value, or null if not found
     */
    public static String get(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.trim().isEmpty()) {
            return systemProperty.trim();
        }
        return properties.getProperty(key);
    }

    /**
     * Reads a configuration value by key with a fallback default.
     *
     * @param key the property key
     * @param defaultValue the default fallback value
     * @return the resolved property value or defaultValue
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
    }

    /**
     * Returns base application URL (defaults to http://localhost:5173).
     */
    public static String getBaseUrl() {
        return get("base.url", "http://localhost:5173");
    }

    /**
     * Returns explicit wait timeout in seconds (defaults to 10).
     */
    public static int getTimeoutSeconds() {
        String timeout = get("timeout.seconds", "10");
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    /**
     * Returns configured browser name (defaults to chrome).
     */
    public static String getBrowser() {
        return get("browser", "chrome");
    }

    /**
     * Checks if browser should run in headless mode (defaults to false).
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }
}
