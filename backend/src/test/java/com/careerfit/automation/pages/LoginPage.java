package com.careerfit.automation.pages;

import com.careerfit.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object representing the Login Page (/login).
 * Strictly complies with POM rules:
 * - NO Assertions inside Page classes.
 * - Encapsulates all locators and user interactions.
 */
public class LoginPage extends BasePage {

    private final By emailInput    = By.cssSelector("[data-testid='login-email']");
    private final By passwordInput = By.cssSelector("[data-testid='login-password']");
    private final By submitButton  = By.cssSelector("[data-testid='login-submit']");
    private final By authError     = By.cssSelector("[data-testid='auth-error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the /login page and waits for email input to be visible.
     *
     * @param baseUrl application base URL
     * @return LoginPage instance (fluent pattern)
     */
    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        waitVisible(emailInput);
        return this;
    }

    /**
     * Performs user login with given credentials.
     *
     * @param email user email
     * @param password user password
     */
    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(submitButton);
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        } catch (Exception ignored) {
            // In case of login error, remains on /login
        }
    }

    /**
     * Checks if authentication error alert is displayed.
     *
     * @return true if visible, false otherwise
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(authError);
    }

    /**
     * Retrieves visible error message text.
     *
     * @return trimmed error message
     */
    public String getErrorMessage() {
        return getText(authError);
    }
}
