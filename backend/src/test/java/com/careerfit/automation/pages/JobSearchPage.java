package com.careerfit.automation.pages;

import com.careerfit.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for Job Search Page (/jobs).
 *
 * Adheres strictly to POM rules:
 * 1. Encapsulates all WebElements / By locators related to job searching, filtering and discovery.
 * 2. Contains ZERO Assertions (assertions belong strictly in Test classes).
 * 3. Contains ZERO Thread.sleep() - uses 100% Explicit Wait (WebDriverWait).
 */
public class JobSearchPage extends BasePage {

    // Locators
    private final By keywordInput = By.cssSelector("input[type='search'], .discovery-keyword input, input[placeholder*='Java, React']");
    private final By searchSubmitButton = By.cssSelector("button.discovery-search-submit, form.discovery-search button[type='submit']");
    private final By clearKeywordButton = By.cssSelector("button.discovery-search-clear, [aria-label='Xóa từ khóa']");
    private final By locationInput = By.cssSelector(".discovery-location input, input[aria-label='Địa điểm làm việc'], input[placeholder='Địa điểm']");
    private final By categorySelect = By.id("discovery-category");
    private final By jobTypeSelect = By.id("discovery-job-type");
    private final By minSalaryInput = By.id("discovery-min-salary");
    private final By maxSalaryInput = By.id("discovery-max-salary");
    private final By filterResetButton = By.cssSelector("button.filter-reset, .discovery-filter-heading button.filter-reset");
    private final By emptyStateResetButton = By.cssSelector(".discovery-empty button");
    private final By jobCards = By.cssSelector(".discovery-job-card, .job-card");
    private final By jobTitles = By.cssSelector(".discovery-job-title, .job-title");
    private final By companyNames = By.cssSelector(".discovery-company-name, .company-name");
    private final By emptyStateContainer = By.cssSelector(".discovery-empty");
    private final By resultsHeadingCount = By.cssSelector(".discovery-results-heading span, .discovery-page-heading p");
    private final By activeFilterChips = By.cssSelector(".discovery-active-filters button");

    public JobSearchPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the /jobs page and waits until the search input is visible.
     *
     * @param baseUrl Application base URL (e.g. http://localhost:5173)
     * @return current JobSearchPage instance
     */
    public JobSearchPage open(String baseUrl) {
        driver.get(baseUrl + "/jobs");
        waitVisible(keywordInput);
        return this;
    }

    /**
     * Types a search keyword and executes the search.
     *
     * @param keyword the text keyword to search (Job title, technology tag or company)
     * @return current JobSearchPage instance
     */
    public JobSearchPage searchByKeyword(String keyword) {
        WebElement input = waitVisible(keywordInput);
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        if (keyword != null && !keyword.isEmpty()) {
            input.sendKeys(keyword);
        }
        
        List<WebElement> submitBtns = driver.findElements(searchSubmitButton);
        if (!submitBtns.isEmpty() && submitBtns.get(0).isDisplayed()) {
            try {
                submitBtns.get(0).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtns.get(0));
            }
        } else {
            input.sendKeys(Keys.ENTER);
        }

        // Wait briefly for DOM to update
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(jobCards),
                    ExpectedConditions.visibilityOfElementLocated(emptyStateContainer)
            ));
        } catch (Exception ignored) {}

        return this;
    }

    /**
     * Filters jobs by location input.
     *
     * @param location location name (e.g. "Hà Nội", "TP.HCM", "Đà Nẵng")
     * @return current JobSearchPage instance
     */
    public JobSearchPage filterByLocation(String location) {
        WebElement input = waitVisible(locationInput);
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        if (location != null && !location.isEmpty()) {
            input.sendKeys(location);
        }
        
        List<WebElement> submitBtns = driver.findElements(searchSubmitButton);
        if (!submitBtns.isEmpty() && submitBtns.get(0).isDisplayed()) {
            try {
                submitBtns.get(0).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtns.get(0));
            }
        } else {
            input.sendKeys(Keys.ENTER);
        }

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(jobCards),
                    ExpectedConditions.visibilityOfElementLocated(emptyStateContainer)
            ));
        } catch (Exception ignored) {}

        return this;
    }

    /**
     * Selects a job category from dropdown if present.
     *
     * @param category category/specialization name
     * @return current JobSearchPage instance
     */
    public JobSearchPage filterByCategory(String category) {
        if (isDisplayed(categorySelect)) {
            Select select = new Select(waitVisible(categorySelect));
            select.selectByVisibleText(category);
        }
        return this;
    }

    /**
     * Selects a job type from dropdown if present.
     *
     * @param jobType job type (Full-time, Internship, etc.)
     * @return current JobSearchPage instance
     */
    public JobSearchPage filterByJobType(String jobType) {
        if (isDisplayed(jobTypeSelect)) {
            Select select = new Select(waitVisible(jobTypeSelect));
            select.selectByVisibleText(jobType);
        }
        return this;
    }

    /**
     * Safely clicks an element using scrollIntoView and JavaScript fallback.
     */
    public void clickSafe(By locator) {
        try {
            WebElement el = waitVisible(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", el);
            el.click();
        } catch (Exception e) {
            WebElement el = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    /**
     * Resets all search filters back to default.
     *
     * @return current JobSearchPage instance
     */
    public JobSearchPage resetFilters() {
        if (isDisplayed(emptyStateResetButton)) {
            clickSafe(emptyStateResetButton);
        } else if (isDisplayed(filterResetButton)) {
            clickSafe(filterResetButton);
        } else if (isDisplayed(clearKeywordButton)) {
            clickSafe(clearKeywordButton);
        }

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(jobCards),
                    ExpectedConditions.invisibilityOfElementLocated(emptyStateContainer)
            ));
        } catch (Exception ignored) {}

        return this;
    }

    /**
     * Returns the count of currently displayed job cards.
     *
     * @return number of job cards found in the DOM
     */
    public int getJobCount() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(jobCards),
                    ExpectedConditions.visibilityOfElementLocated(emptyStateContainer)
            ));
        } catch (Exception ignored) {}

        List<WebElement> cards = driver.findElements(jobCards);
        return cards.size();
    }

    /**
     * Checks if the Empty State / "No results found" container is displayed.
     *
     * @return true if no results message is visible
     */
    public boolean isNoResultDisplayed() {
        return isDisplayed(emptyStateContainer);
    }

    /**
     * Retrieves the text from the Empty State container.
     *
     * @return trimmed message text or empty string
     */
    public String getNoResultText() {
        if (isNoResultDisplayed()) {
            return getText(emptyStateContainer);
        }
        return "";
    }

    /**
     * Collects and returns visible job card titles.
     *
     * @return list of trimmed job title strings
     */
    public List<String> getJobTitles() {
        List<String> titles = new ArrayList<>();
        List<WebElement> elements = driver.findElements(jobTitles);
        for (WebElement el : elements) {
            if (el.isDisplayed()) {
                titles.add(el.getText().trim());
            }
        }
        return titles;
    }

    /**
     * Checks whether the search input element is active and visible.
     *
     * @return true if visible
     */
    public boolean isSearchInputDisplayed() {
        return isDisplayed(keywordInput);
    }
}
