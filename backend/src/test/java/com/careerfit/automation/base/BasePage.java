package com.careerfit.automation.base;

import com.careerfit.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base Page Object Model class.
 * Core rules:
 * - NO Thread.sleep() - 100% Explicit Wait (WebDriverWait).
 * - NO Assertions in Page classes.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        int timeoutSeconds = ConfigReader.getTimeoutSeconds();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public BasePage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    /**
     * Waits until the element located by locator is visible on the page.
     *
     * @param locator the element locator
     * @return the visible WebElement
     */
    public WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is clickable and performs a click.
     *
     * @param locator the element locator
     */
    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Waits until the element is visible, clears any existing text,
     * and types the specified text.
     *
     * @param locator the element locator
     * @param text the text to enter
     */
    public void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        element.clear();
        if (text != null) {
            element.sendKeys(text);
        }
    }

    /**
     * Waits until the element is visible and retrieves its visible text.
     *
     * @param locator the element locator
     * @return visible text trimmed
     */
    public String getText(By locator) {
        return waitVisible(locator).getText().trim();
    }

    /**
     * Checks if the element is displayed using explicit wait.
     *
     * @param locator the element locator
     * @return true if visible within timeout, false otherwise
     */
    public boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
