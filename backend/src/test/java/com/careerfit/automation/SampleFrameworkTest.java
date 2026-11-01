package com.careerfit.automation;

import com.careerfit.automation.base.BasePage;
import com.careerfit.automation.base.BaseTest;
import com.careerfit.automation.utils.ScreenshotOnFailure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Framework Base Setup Smoke Test")
class SampleFrameworkTest extends BaseTest {

    // Concrete Page Object for testing BasePage methods
    static class SamplePage extends BasePage {
        private final By header = By.tagName("h1");
        private final By message = By.id("msg");
        private final By nonExistent = By.id("does-not-exist");

        public SamplePage(WebDriver driver) {
            super(driver);
        }

        public String getHeaderText() {
            return getText(header);
        }

        public String getMessageText() {
            return getText(message);
        }

        public boolean isHeaderDisplayed() {
            return isDisplayed(header);
        }

        public boolean isNonExistentDisplayed() {
            return isDisplayed(nonExistent);
        }
    }

    @Test
    @DisplayName("Sample test: Launch Chrome, test BasePage explicit waits, and shut down cleanly")
    void testLaunchAndTearDownChrome() {
        assertNotNull(driver, "Driver should be initialized by BaseTest @BeforeEach");

        // Load an inline HTML data URI to test browser operations without requiring live backend
        driver.get("data:text/html,<html><head><title>CareerFit Automation</title></head><body><h1>CareerFit Framework Ready</h1><p id='msg'>Selenium 4 POM setup successfully.</p></body></html>");

        assertEquals("CareerFit Automation", driver.getTitle());

        SamplePage samplePage = new SamplePage(driver);
        assertTrue(samplePage.isHeaderDisplayed(), "Header should be visible");
        assertEquals("CareerFit Framework Ready", samplePage.getHeaderText());
        assertEquals("Selenium 4 POM setup successfully.", samplePage.getMessageText());
        assertFalse(samplePage.isNonExistentDisplayed(), "Non-existent element should return false");
    }

    @Test
    @DisplayName("Verify ScreenshotOnFailure extension generates screenshot file in target/screenshots/")
    void testScreenshotOnFailureGeneration() throws Exception {
        driver.get("data:text/html,<html><body><h1>Screenshot Test</h1></body></html>");

        ExtensionContext mockContext = Mockito.mock(ExtensionContext.class);
        Mockito.when(mockContext.getTestInstance()).thenReturn(Optional.of(this));
        Mockito.doReturn(Optional.of(SampleFrameworkTest.class)).when(mockContext).getTestClass();
        Mockito.doReturn(Optional.of(SampleFrameworkTest.class.getDeclaredMethod("testScreenshotOnFailureGeneration"))).when(mockContext).getTestMethod();

        ScreenshotOnFailure extension = new ScreenshotOnFailure();
        extension.testFailed(mockContext, new AssertionError("Simulated assertion failure for screenshot testing"));

        Path screenshotsDir = Paths.get("target", "screenshots");
        assertTrue(Files.exists(screenshotsDir), "target/screenshots directory must exist");

        try (Stream<Path> stream = Files.list(screenshotsDir)) {
            boolean found = stream.anyMatch(path -> path.getFileName().toString().startsWith("SampleFrameworkTest_testScreenshotOnFailureGeneration"));
            assertTrue(found, "Screenshot file should be generated in target/screenshots/");
        }
    }
}
