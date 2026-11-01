package com.careerfit.automation.pages;

import com.careerfit.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object representing Job Detail Page (/jobs/:id).
 * Fulfills all requirements for Module M4:
 * - Locators: Save button, Apply button, Applied badge, Cancel button, Toast alert.
 * - Methods: saveJob(), applyJob(), cancelApplication(), isApplied().
 * - NO Assertions in Page class (Strict POM principle).
 */
public class JobDetailPage extends BasePage {

    // Locators
    private final By saveButton          = By.cssSelector("button.job-detail-save");
    private final By savedButtonActive    = By.cssSelector("button.job-detail-save.is-saved");
    private final By saveButtonLabel     = By.cssSelector("button.job-detail-save span");
    private final By applyButton         = By.cssSelector("button.job-detail-primary");
    private final By appliedBadge        = By.xpath("//button[contains(@class,'job-detail-primary')]//span[contains(text(),'Đã ứng tuyển')]");
    private final By cancelApplyButton   = By.cssSelector("button.job-detail-primary .job-detail-cancel");
    private final By toastElement        = By.cssSelector(".careerfit-toast");
    private final By toastMessage        = By.cssSelector(".careerfit-toast span");
    private final By jobTitleHeading     = By.tagName("h1");

    public JobDetailPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to a specific job detail page: /jobs/{jobId}.
     *
     * @param baseUrl application base URL
     * @param jobId unique identifier of the job
     * @return JobDetailPage instance
     */
    public JobDetailPage open(String baseUrl, String jobId) {
        driver.get(baseUrl + "/jobs/" + jobId);
        waitVisible(saveButton);
        return this;
    }

    /**
     * Clicks the Bookmark / Save Job button and waits for state transition.
     *
     * @return JobDetailPage instance (fluent pattern)
     */
    public JobDetailPage saveJob() {
        boolean previous = isSaved();
        clickJS(saveButton);
        wait.until(d -> isSaved() != previous);
        return this;
    }

    /**
     * Checks if the job is marked as saved (active bookmark).
     *
     * @return true if saved, false otherwise
     */
    public boolean isSaved() {
        try {
            return driver.findElement(saveButton).getAttribute("class").contains("is-saved");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the text of the Save button ('Lưu tin' or 'Đã lưu tin').
     */
    public String getSaveButtonText() {
        return getText(saveButtonLabel);
    }

    /**
     * Clicks the Apply button ('Ứng tuyển ngay') and waits for state transition.
     *
     * @return JobDetailPage instance
     */
    public JobDetailPage applyJob() {
        clickJS(applyButton);
        wait.until(d -> isApplied());
        return this;
    }

    /**
     * Clicks the Cancel Application button ('— Hủy') and waits for state transition.
     *
     * @return JobDetailPage instance
     */
    public JobDetailPage cancelApplication() {
        if (isDisplayed(cancelApplyButton, java.time.Duration.ofSeconds(2))) {
            clickJS(cancelApplyButton);
        } else {
            clickJS(applyButton);
        }
        wait.until(d -> !isApplied());
        return this;
    }

    /**
     * Checks if the candidate has already applied to this job (button shows '✓ Đã ứng tuyển').
     *
     * @return true if applied, false otherwise
     */
    public boolean isApplied() {
        try {
            return driver.findElement(applyButton).getText().contains("Đã ứng tuyển");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the toast notification is displayed.
     */
    public boolean isToastDisplayed() {
        return isDisplayed(toastElement);
    }

    /**
     * Retrieves visible toast message text.
     */
    public String getToastMessage() {
        return getText(toastMessage);
    }

    /**
     * Returns the main job title heading on the page.
     */
    public String getJobTitle() {
        return getText(jobTitleHeading);
    }
}
