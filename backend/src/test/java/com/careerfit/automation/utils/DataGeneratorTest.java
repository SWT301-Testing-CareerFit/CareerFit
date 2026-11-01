package com.careerfit.automation.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DataGenerator Unit Tests")
class DataGeneratorTest {

    @Test
    @DisplayName("Should generate valid email with timestamp")
    void testRandomEmail() {
        String email1 = DataGenerator.randomEmail();
        String email2 = DataGenerator.randomEmail();

        assertTrue(email1.startsWith("test_"), "Email should start with test_");
        assertTrue(email1.endsWith("@gmail.com"), "Email should end with @gmail.com");
        assertTrue(email1.matches("^test_\\d+@gmail.com$"), "Email format mismatch");
    }

    @Test
    @DisplayName("Should generate email with custom prefix")
    void testRandomEmailWithPrefix() {
        String email = DataGenerator.randomEmail("candidate");
        assertTrue(email.startsWith("candidate_"));
        assertTrue(email.endsWith("@gmail.com"));
    }

    @Test
    @DisplayName("Should generate random username with timestamp")
    void testRandomUsername() {
        String username = DataGenerator.randomUsername();
        assertTrue(username.startsWith("user_"));
        assertTrue(username.matches("^user_\\d+$"));
    }

    @Test
    @DisplayName("Should generate valid Vietnamese phone number")
    void testRandomPhoneNumber() {
        String phone = DataGenerator.randomPhoneNumber();
        assertEquals(10, phone.length());
        assertTrue(phone.startsWith("09"));
        assertTrue(phone.matches("^09\\d{8}$"));
    }
}
