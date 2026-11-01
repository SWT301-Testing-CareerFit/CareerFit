package com.careerfit.automation.pages;

import com.careerfit.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object Model for Login Page (/login).
 *
 * Adheres strictly to POM rules:
 * 1. Encapsulates all WebElements / By locators related to login.
 * 2. Provides business interaction methods (login, enterEmail, getErrorMessage).
 * 3. Contains NO Assertions (assertions belong strictly in Test classes).
 * 4. Uses Explicit Waits inherited from BasePage (zero Thread.sleep()).
 */
public class LoginPage extends BasePage {

    // Locators
    private final By tabLogin = By.cssSelector("[data-testid='tab-login']");
    private final By emailInput = By.cssSelector("[data-testid='login-email']");
    private final By passwordInput = By.cssSelector("[data-testid='login-password']");
    private final By submitButton = By.cssSelector("[data-testid='login-submit']");
    private final By authError = By.cssSelector("[data-testid='auth-error']");
    private final By authSuccess = By.cssSelector("[data-testid='auth-success']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the login page and ensures the login tab is active.
     *
     * @param baseUrl Application base URL (e.g. http://localhost:5173)
     * @return current LoginPage instance
     */
    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        // If tab switcher is present and register tab was active, switch to login
        List<WebElement> tabs = driver.findElements(tabLogin);
        if (!tabs.isEmpty() && tabs.get(0).isDisplayed()) {
            tabs.get(0).click();
        }
        waitVisible(emailInput);
        return this;
    }

    /**
     * Enters email or username into the email field.
     *
     * @param email User email or identifier
     * @return current LoginPage instance
     */
    public LoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    /**
     * Enters password into the password field.
     *
     * @param password User password
     * @return current LoginPage instance
     */
    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    /**
     * Clicks the submit login button.
     */
    public void clickSubmit() {
        click(submitButton);
    }

    /**
     * Convenience method to perform a full login submission.
     *
     * @param email User email
     * @param password User password
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        } catch (Exception ignored) {
            // In case of login error, remains on /login
        }
    }

    /**
     * Waits until error banner appears and returns its text.
     *
     * @return Trimmed error message text
     */
    public String getErrorMessage() {
        return getText(authError);
    }

    /**
     * Waits explicitly until error banner is visible.
     *
     * @return true if visible within timeout, false otherwise
     */
    public boolean waitUntilErrorDisplayed() {
        try {
            return waitVisible(authError).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if error element is currently visible without long wait.
     *
     * @return true if visible right now, false otherwise
     */
    public boolean isErrorDisplayed() {
        List<WebElement> elements = driver.findElements(authError);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    /**
     * Waits explicitly until browser URL redirects away from /login upon success.
     *
     * @return true if URL changed away from /login within timeout, false otherwise
     */
    public boolean waitUntilRedirectedFromLogin() {
        try {
            return wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if email field is marked as required (HTML5 required).
     *
     * @return true if required attribute exists
     */
    public boolean isEmailRequired() {
        WebElement el = waitVisible(emailInput);
        return "true".equals(el.getAttribute("required")) || el.getAttribute("required") != null;
    }

    /**
     * Checks if password field is marked as required (HTML5 required).
     *
     * @return true if required attribute exists
     */
    public boolean isPasswordRequired() {
        WebElement el = waitVisible(passwordInput);
        return "true".equals(el.getAttribute("required")) || el.getAttribute("required") != null;
    }

    /**
     * Retrieves the current page URL.
     *
     * @return Current browser URL string
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
