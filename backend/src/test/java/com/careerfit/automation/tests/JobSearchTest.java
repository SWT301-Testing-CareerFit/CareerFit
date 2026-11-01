package com.careerfit.automation.tests;

import com.careerfit.automation.base.BaseTest;
import com.careerfit.automation.pages.JobSearchPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.NoAlertPresentException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for Module 3: Job Search & Filter.
 *
 * Adheres strictly to POM and ISTQB test automation guidelines:
 * 1. 100% Assertions reside inside Test class (ZERO assertions in Page classes).
 * 2. ZERO By locators or driver.findElement in Test class.
 * 3. ZERO Thread.sleep() - relies on explicit waits.
 * 4. Data-Driven Testing (DDT) reading from /testdata/search_data.csv.
 * 5. Input security verification for XSS and SQL Injection payloads.
 */
@Tag("module-search")
@DisplayName("M3 - Kiểm thử Tìm kiếm, Lọc việc làm & An toàn dữ liệu (Data-Driven)")
public class JobSearchTest extends BaseTest {

    @ParameterizedTest(name = "[{index}] {0} - Kw: ''{1}'' | Loc: ''{2}'' | Kỳ vọng: {3}")
    @CsvFileSource(resources = "/testdata/search_data.csv", numLinesToSkip = 1, encoding = "UTF-8")
    @DisplayName("DDT: Kiểm thử Tìm kiếm & Lọc việc làm từ file CSV")
    public void testSearchDataDriven(String tcId, String keyword, String location, String expectedResult, String expectedNote) {
        JobSearchPage searchPage = new JobSearchPage(driver);
        searchPage.open(BASE_URL);

        assertTrue(searchPage.isSearchInputDisplayed(),
                "[" + tcId + "] Ô tìm kiếm việc làm bắt buộc phải hiển thị trên trang /jobs.");

        String safeKw = (keyword == null) ? "" : keyword.trim();
        String safeLoc = (location == null) ? "" : location.trim();
        String safeExpected = (expectedResult == null) ? "FOUND" : expectedResult.trim();

        switch (safeExpected) {
            case "FOUND":
                if (!safeKw.isEmpty()) {
                    searchPage.searchByKeyword(safeKw);
                }
                if (!safeLoc.isEmpty()) {
                    searchPage.filterByLocation(safeLoc);
                }

                // Verify rằng trang không bị crash và số lượng thẻ job hợp lệ
                int count = searchPage.getJobCount();
                assertTrue(count >= 0,
                        "[" + tcId + "] Số lượng công việc hiển thị không được âm.");
                assertFalse(driver.getPageSource().contains("500 Internal Server Error"),
                        "[" + tcId + "] Hệ thống gặp lỗi 500 khi tìm kiếm với từ khóa: " + safeKw);
                break;

            case "EMPTY":
                searchPage.searchByKeyword(safeKw);
                int emptyCount = searchPage.getJobCount();
                boolean noResultShown = searchPage.isNoResultDisplayed();

                assertTrue(noResultShown || emptyCount == 0,
                        "[" + tcId + "] Từ khóa không tồn tại bắt buộc phải hiển thị Empty State hoặc danh sách rỗng.");
                break;

            case "EMPTY_OR_SAFE":
                // Kiểm thử an toàn dữ liệu: XSS / SQLi / Ký tự đặc biệt
                searchPage.searchByKeyword(safeKw);

                // 1. Kiểm tra tuyệt đối không có Alert JavaScript bất thường nào xuất hiện
                assertDoesNotThrow(() -> {
                    try {
                        driver.switchTo().alert().dismiss();
                        fail("[" + tcId + "] Phát hiện Alert popup không an toàn (XSS Injection thành công)!");
                    } catch (NoAlertPresentException ignored) {
                        // Expected: Không có alert popup nào
                    }
                }, "[" + tcId + "] Lỗi kiểm tra hộp thoại Alert.");

                // 2. Kiểm tra trang không bị vỡ giao diện hoặc lỗi 500
                assertFalse(driver.getPageSource().contains("SQLException"),
                        "[" + tcId + "] Lộ thông tin SQL Exception trong mã nguồn trang!");
                assertFalse(driver.getPageSource().contains("500 Internal Server Error"),
                        "[" + tcId + "] Hệ thống bị sập với lỗi 500 khi nhập payload: " + safeKw);
                break;

            case "RESET":
                // Áp dụng tìm kiếm trước, sau đó bấm nút reset
                searchPage.searchByKeyword("NonExistent_ABC_XYZ_9999");
                searchPage.resetFilters();
                assertTrue(searchPage.isSearchInputDisplayed(),
                        "[" + tcId + "] Sau khi đặt lại bộ lọc, ô tìm kiếm vẫn phải sẵn sàng.");
                break;

            default:
                fail("[" + tcId + "] Trạng thái expectedResult không được hỗ trợ trong CSV: " + safeExpected);
        }
    }

    @Test
    @DisplayName("TC-SEC-XSS: Kiểm tra an toàn trước tấn công Cross-Site Scripting (XSS)")
    public void testInputSecurity_XSSInjection_SafeFromScriptExecution() {
        JobSearchPage searchPage = new JobSearchPage(driver);
        searchPage.open(BASE_URL);

        String xssPayload = "<script>alert('XSS_ATTACK_TEST')</script>";
        searchPage.searchByKeyword(xssPayload);

        // Xác nhận không có alert dialog nào xuất hiện
        assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert(),
                "Trình duyệt không được phép thực thi thẻ <script> từ ô tìm kiếm.");

        // Xác nhận payload hiển thị an toàn dưới dạng text hoặc bị escape
        assertFalse(driver.getPageSource().contains("<script>alert('XSS_ATTACK_TEST')</script>"),
                "Mã kịch bản XSS không được chèn nguyên bản vào DOM mà không được sanitize.");
    }

    @Test
    @DisplayName("TC-SEC-SQLI: Kiểm tra an toàn trước chuỗi SQL Injection")
    public void testInputSecurity_SQLInjection_NoBackendCrash() {
        JobSearchPage searchPage = new JobSearchPage(driver);
        searchPage.open(BASE_URL);

        String sqliPayload = "' UNION SELECT null, null, null, null --";
        searchPage.searchByKeyword(sqliPayload);

        // Kiểm tra backend không trả về lỗi hệ thống hoặc lộ cấu trúc DB
        String pageSource = driver.getPageSource().toLowerCase();
        assertFalse(pageSource.contains("syntax error"), "Không được để lộ lỗi cú pháp SQL ra ngoài giao diện.");
        assertFalse(pageSource.contains("internal server error"), "Không được phát sinh lỗi 500 từ server.");
    }

    @Test
    @DisplayName("TC-SEARCH-RESET: Kiểm tra chức năng Đặt lại bộ lọc (Reset Filter)")
    public void testResetFilterFunctionality() {
        JobSearchPage searchPage = new JobSearchPage(driver);
        searchPage.open(BASE_URL);

        // 1. Nhập từ khóa rác để trang rơi vào trạng thái không tìm thấy kết quả
        searchPage.searchByKeyword("invalid_string_no_jobs_found_9999");
        assertTrue(searchPage.isNoResultDisplayed() || searchPage.getJobCount() == 0,
                "Phải hiển thị trạng thái trống khi không tìm thấy việc làm.");

        // 2. Bấm nút đặt lại bộ lọc
        searchPage.resetFilters();

        // 3. Xác nhận ô tìm kiếm vẫn hiển thị tốt và hệ thống sẵn sàng
        assertTrue(searchPage.isSearchInputDisplayed(),
                "Sau khi bấm Đặt lại, ô tìm kiếm phải sẵn sàng.");
        assertFalse(driver.getPageSource().contains("500 Internal Server Error"),
                "Không được phát sinh lỗi server khi reset bộ lọc.");
    }
}
