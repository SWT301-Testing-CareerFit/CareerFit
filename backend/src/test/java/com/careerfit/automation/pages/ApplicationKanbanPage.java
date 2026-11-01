package com.careerfit.automation.pages;

import com.careerfit.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object representing the Candidate Application Tracking Page (/candidate/applications).
 * Fulfills all requirements for Module M4:
 * - Locators: Submitted column ("Đã nộp"), application cards/rows list, view switchers.
 * - Method: isJobInSubmittedColumn(String jobTitle).
 * - NO Assertions in Page class (Strict POM principle).
 */
public class ApplicationKanbanPage extends BasePage {

    // Locators
    private final By heroHeading        = By.cssSelector(".applications-hero h1");
    private final By submittedColumn    = By.xpath("//section[contains(@class,'applications-column') and .//h3[contains(.,'Đã nộp')]]");
    private final By boardCards         = By.cssSelector(".applications-board-card");
    private final By listRows           = By.cssSelector(".applications-row");
    private final By boardViewButton    = By.xpath("//div[contains(@class,'applications-view-switch')]//button[contains(.,'Bảng')]");
    private final By listViewButton     = By.xpath("//div[contains(@class,'applications-view-switch')]//button[contains(.,'Danh sách')]");
    private final By searchInput        = By.cssSelector("input[aria-label='Tìm đơn ứng tuyển']");
    private final By totalAppsMetaBadge = By.cssSelector(".applications-hero-meta span");

    public ApplicationKanbanPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the Candidate Application Tracking page.
     *
     * @param baseUrl application base URL
     * @return ApplicationKanbanPage instance (fluent pattern)
     */
    public ApplicationKanbanPage open(String baseUrl) {
        driver.get(baseUrl + "/candidate/applications");
        waitVisible(heroHeading);
        return this;
    }

    /**
     * Switches view to Kanban Board mode (5 columns).
     *
     * @return ApplicationKanbanPage instance
     */
    public ApplicationKanbanPage switchToBoardView() {
        if (isDisplayed(boardViewButton)) {
            click(boardViewButton);
        }
        return this;
    }

    /**
     * Switches view to List mode.
     *
     * @return ApplicationKanbanPage instance
     */
    public ApplicationKanbanPage switchToListView() {
        if (isDisplayed(listViewButton)) {
            click(listViewButton);
        }
        return this;
    }

    /**
     * Checks if a job with the specified jobTitle is present in the "Đã nộp" (Submitted) stage.
     * Checks both Board view column and List view status row.
     *
     * @param jobTitle title of the job to verify
     * @return true if visible in Submitted column/status, false otherwise
     */
    public boolean isJobInSubmittedColumn(String jobTitle) {
        // 1. Check in Kanban Board view: within column 'Đã nộp'
        By boardCardTitleLocator = By.xpath(
                "//section[contains(@class,'applications-column') and .//h3[contains(.,'Đã nộp')]]" +
                "//button[contains(@class,'applications-board-card')]//strong[contains(text(),'" + jobTitle + "')]"
        );
        if (isDisplayed(boardCardTitleLocator, java.time.Duration.ofSeconds(2))) {
            return true;
        }

        // 2. Check in List view: row with jobTitle and status containing 'Đã nộp'
        By listRowStatusLocator = By.xpath(
                "//div[contains(@class,'applications-list')]//button[contains(@class,'applications-row') and .//strong[contains(text(),'" + jobTitle + "')]]" +
                "//span[contains(@class,'applications-status') and contains(.,'Đã nộp')]"
        );
        return isDisplayed(listRowStatusLocator, java.time.Duration.ofSeconds(2));
    }

    /**
     * Checks if a job with the specified jobTitle exists anywhere on the applications page.
     *
     * @param jobTitle title of the job to check
     * @return true if found, false otherwise
     */
    public boolean isJobPresent(String jobTitle) {
        By jobLocator = By.xpath(
                "//*[contains(@class,'applications-row') or contains(@class,'applications-board-card')]" +
                "//*[contains(text(),'" + jobTitle + "')]"
        );
        return isDisplayed(jobLocator);
    }

    /**
     * Filters applications by typing a search keyword into the search bar.
     *
     * @param keyword search keyword
     * @return ApplicationKanbanPage instance
     */
    public ApplicationKanbanPage searchApplication(String keyword) {
        type(searchInput, keyword);
        return this;
    }
}
