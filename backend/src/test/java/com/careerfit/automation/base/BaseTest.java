package com.careerfit.automation.base;

import com.careerfit.automation.utils.ConfigReader;
import com.careerfit.automation.utils.ScreenshotOnFailure;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Base test class managing WebDriver lifecycle and JUnit 5 extensions.
 * Registers ScreenshotOnFailure extension to automatically capture
 * failure evidence to target/screenshots/.
 */
@ExtendWith(ScreenshotOnFailure.class)
public abstract class BaseTest {

    protected WebDriver driver;
    protected String BASE_URL;

    @BeforeEach
    public void setUp() {
        BASE_URL = ConfigReader.getBaseUrl();

        // Setup ChromeDriver binary using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Configure ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1366,768");
        options.addArguments("--remote-allow-origins=*");

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }

        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Exposes the WebDriver instance for JUnit extensions (e.g. ScreenshotOnFailure).
     *
     * @return active WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
}
