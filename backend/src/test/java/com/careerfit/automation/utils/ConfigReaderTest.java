package com.careerfit.automation.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConfigReader Unit Tests")
class ConfigReaderTest {

    @Test
    @DisplayName("Should read default values from config.properties")
    void testReadDefaults() {
        assertEquals("http://localhost:5173", ConfigReader.getBaseUrl());
        assertEquals(10, ConfigReader.getTimeoutSeconds());
        assertEquals("chrome", ConfigReader.getBrowser());
        String expectedHeadless = System.getProperty("headless", "false");
        assertEquals(Boolean.parseBoolean(expectedHeadless), ConfigReader.isHeadless());
    }

    @Test
    @DisplayName("System properties should take precedence over config.properties")
    void testSystemPropertyPrecedence() {
        String originalProperty = System.getProperty("base.url");
        try {
            System.setProperty("base.url", "http://test-server:3000");
            assertEquals("http://test-server:3000", ConfigReader.getBaseUrl());
        } finally {
            if (originalProperty != null) {
                System.setProperty("base.url", originalProperty);
            } else {
                System.clearProperty("base.url");
            }
        }
    }

    @Test
    @DisplayName("Fallback default value when key does not exist")
    void testFallbackDefault() {
        String nonExistent = ConfigReader.get("non.existent.key", "fallbackValue");
        assertEquals("fallbackValue", nonExistent);
    }
}
