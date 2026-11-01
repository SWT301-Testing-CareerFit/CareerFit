package com.careerfit.automation.utils;

import com.careerfit.automation.base.BaseTest;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * JUnit 5 Extension that automatically captures a browser screenshot
 * upon test assertion failure and saves it into target/screenshots/.
 * Implements AfterTestExecutionCallback to ensure screenshot is captured
 * BEFORE @AfterEach tearDown() calls driver.quit().
 */
public class ScreenshotOnFailure implements TestWatcher, AfterTestExecutionCallback {

    private static final String SCREENSHOT_DIR = "target/screenshots";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            captureScreenshot(context);
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // Fallback in case afterTestExecution was not invoked
        captureScreenshot(context);
    }

    private void captureScreenshot(ExtensionContext context) {
        WebDriver driver = resolveDriver(context);

        if (driver == null) {
            return;
        }

        if (!(driver instanceof TakesScreenshot)) {
            return;
        }

        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File tempScreenshot = takesScreenshot.getScreenshotAs(OutputType.FILE);

            Path destinationDir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(destinationDir);

            String className = context.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
            String methodName = context.getTestMethod().map(java.lang.reflect.Method::getName).orElse("unknownMethod");
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);

            String fileName = String.format("%s_%s_%s.png", className, methodName, timestamp);
            Path targetFile = destinationDir.resolve(fileName);

            Files.copy(tempScreenshot.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[ScreenshotOnFailure] Test failed! Screenshot saved to: " + targetFile.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[ScreenshotOnFailure] Failed to write screenshot to disk: " + e.getMessage());
        } catch (Exception e) {
            // Ignore if driver session is already closed
        }
    }

    /**
     * Resolves the WebDriver instance from the current test context.
     */
    private WebDriver resolveDriver(ExtensionContext context) {
        Optional<Object> testInstanceOpt = context.getTestInstance();
        if (testInstanceOpt.isEmpty()) {
            return null;
        }

        Object testInstance = testInstanceOpt.get();

        // 1. Check if test instance extends BaseTest
        if (testInstance instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testInstance).getDriver();
            if (driver != null) {
                return driver;
            }
        }

        // 2. Fallback: Search for 'driver' field via reflection in hierarchy
        Class<?> clazz = testInstance.getClass();
        while (clazz != null && clazz != Object.class) {
            try {
                Field driverField = clazz.getDeclaredField("driver");
                driverField.setAccessible(true);
                Object val = driverField.get(testInstance);
                if (val instanceof WebDriver) {
                    return (WebDriver) val;
                }
            } catch (NoSuchFieldException ignored) {
                // Continue searching up class hierarchy
            } catch (Exception e) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        return null;
    }
}
