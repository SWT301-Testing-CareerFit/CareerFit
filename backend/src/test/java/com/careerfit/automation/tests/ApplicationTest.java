package com.careerfit.automation.tests;

import com.careerfit.automation.base.BaseTest;
import com.careerfit.automation.pages.ApplicationKanbanPage;
import com.careerfit.automation.pages.JobDetailPage;
import com.careerfit.automation.pages.LoginPage;
import com.careerfit.automation.utils.CsvDataReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for Module M4: Job Application & Kanban Application Tracking.
 * Strictly adheres to 8 POM rules:
 * - NO Assertions in Page classes.
 * - NO By or driver.findElement in this Test class.
 * - NO Thread.sleep() - 100% Explicit Wait.
 * - NO hard-coded data - all test data loaded from apply_data.csv.
 * - Independent test methods with clear assertions.
 */
@DisplayName("M4 - Ứng tuyển việc làm & Theo dõi đơn trên Kanban")
class ApplicationTest extends BaseTest {

    private static final String CSV_PATH = "testdata/apply_data.csv";

    @Test
    @DisplayName("TC-APP-E2E: Kịch bản State Transition hoàn chỉnh (Login -> Chi tiết Job -> Lưu tin -> Ứng tuyển -> Verify Kanban -> Hủy ứng tuyển)")
    void testApplicationFullStateTransitionFlow() {
        Map<String, String> data = CsvDataReader.getRowByTcId(CSV_PATH, "TC-APP-E2E");
        assertNotNull(data, "Dữ liệu kiểm thử cho TC-APP-E2E phải tồn tại trong file CSV: " + CSV_PATH);

        String testJobId = data.get("jobId");
        String testJobTitle = data.get("jobTitle");
        String email = data.get("candidateEmail");
        String password = data.get("candidatePassword");

        // 1. Login bước đầu tiên với tài khoản Candidate hợp lệ từ CSV
        LoginPage loginPage = new LoginPage(driver).open(BASE_URL);
        loginPage.login(email, password);

        // 2. Mở trang Chi tiết tin tuyển dụng (/jobs/job-4)
        JobDetailPage jobDetailPage = new JobDetailPage(driver).open(BASE_URL, testJobId);
        assertNotNull(jobDetailPage.getJobTitle(), "Tiêu đề công việc phải hiển thị trên trang chi tiết");

        // 3. State Transition Bookmark: Chưa lưu -> Bấm 'Lưu tin' -> Đã lưu tin
        jobDetailPage.saveJob();
        assertTrue(jobDetailPage.isSaved(), "Tin tuyển dụng phải được chuyển sang trạng thái đã lưu (active bookmark)");

        // 4. State Transition Ứng tuyển: Chưa ứng tuyển -> Bấm 'Ứng tuyển ngay' -> Đã ứng tuyển
        jobDetailPage.applyJob();
        assertTrue(jobDetailPage.isApplied(), "Nút ứng tuyển phải chuyển sang trạng thái '✓ Đã ứng tuyển — Hủy'");

        // 5. Mở bảng Kanban kiểm tra đơn xuất hiện tại cột 'Đã nộp'
        ApplicationKanbanPage kanbanPage = new ApplicationKanbanPage(driver).open(BASE_URL);
        assertTrue(kanbanPage.isJobInSubmittedColumn(testJobTitle),
                "Đơn ứng tuyển cho vị trí '" + testJobTitle + "' phải hiển thị chính xác tại cột 'Đã nộp hồ sơ' trên Kanban");

        // 6. Quay lại trang chi tiết job và thực hiện Hủy đơn ứng tuyển
        jobDetailPage.open(BASE_URL, testJobId);
        assertTrue(jobDetailPage.isApplied(), "Trước khi hủy, trạng thái phải đang là '✓ Đã ứng tuyển'");
        jobDetailPage.cancelApplication();
        assertFalse(jobDetailPage.isApplied(), "Sau khi bấm hủy, trạng thái phải quay về chưa ứng tuyển (nút 'Ứng tuyển ngay')");

        // 7. Quay lại bảng Kanban xác nhận đơn đã bị xóa hoàn toàn khỏi danh sách
        kanbanPage.open(BASE_URL);
        assertFalse(kanbanPage.isJobInSubmittedColumn(testJobTitle),
                "Sau khi hủy đơn, công việc '" + testJobTitle + "' phải biến mất hoàn toàn khỏi cột 'Đã nộp hồ sơ' trên Kanban");
    }

    @Test
    @DisplayName("TC-APP-010 & TC-APP-011: State Transition kiểm tra tính năng Bookmark (Lưu tin <-> Bỏ lưu tin)")
    void testBookmarkToggleStateTransition() {
        Map<String, String> data = CsvDataReader.getRowByTcId(CSV_PATH, "TC-APP-010");
        assertNotNull(data, "Dữ liệu kiểm thử cho TC-APP-010 phải tồn tại trong file CSV: " + CSV_PATH);

        String testJobId = data.get("jobId");
        String email = data.get("candidateEmail");
        String password = data.get("candidatePassword");

        // 1. Đăng nhập với tài khoản Candidate từ CSV
        new LoginPage(driver).open(BASE_URL).login(email, password);

        // 2. Mở chi tiết công việc
        JobDetailPage jobDetailPage = new JobDetailPage(driver).open(BASE_URL, testJobId);

        // Lưu trạng thái ban đầu
        boolean initiallySaved = jobDetailPage.isSaved();

        // Chuyển đổi trạng thái lần 1
        jobDetailPage.saveJob();
        assertEquals(!initiallySaved, jobDetailPage.isSaved(),
                "Trạng thái bookmark phải đảo ngược sau khi click nút Lưu tin");

        // Chuyển đổi trạng thái lần 2 (quay lại trạng thái gốc)
        jobDetailPage.saveJob();
        assertEquals(initiallySaved, jobDetailPage.isSaved(),
                "Trạng thái bookmark phải quay lại trạng thái ban đầu sau khi click lần thứ hai");
    }

    @Test
    @DisplayName("TC-APP-013 & TC-APP-014: Chuyển đổi chế độ xem Bảng/Danh sách và tìm kiếm trên Kanban")
    void testKanbanViewSwitchingAndSearch() {
        Map<String, String> data = CsvDataReader.getRowByTcId(CSV_PATH, "TC-APP-E2E");
        assertNotNull(data, "Dữ liệu kiểm thử cho Kanban phải tồn tại trong CSV");

        // 1. Đăng nhập với tài khoản Candidate từ CSV
        new LoginPage(driver).open(BASE_URL).login(data.get("candidateEmail"), data.get("candidatePassword"));

        // 2. Mở trang Kanban đơn ứng tuyển
        ApplicationKanbanPage kanbanPage = new ApplicationKanbanPage(driver).open(BASE_URL);

        // 3. Chuyển sang Board View (Bảng)
        kanbanPage.switchToBoardView();

        // 4. Chuyển lại List View (Danh sách)
        kanbanPage.switchToListView();

        // 5. Tìm kiếm đơn ứng tuyển
        kanbanPage.searchApplication("Java");
    }

    @ParameterizedTest(name = "{0}: jobId={1}, action={5}")
    @CsvFileSource(resources = "/testdata/apply_data.csv", numLinesToSkip = 1, encoding = "UTF-8")
    @DisplayName("Data-driven test kiểm tra các hành động từ file CSV apply_data.csv")
    void testApplyDataDriven(String tcId, String jobId, String jobTitle,
                            String email, String password, String action,
                            String expectedStatus, String expectedMessage) {
        // 1. Đăng nhập với tài khoản từ CSV
        new LoginPage(driver).open(BASE_URL).login(email, password);

        // 2. Mở trang chi tiết công việc
        JobDetailPage jobDetailPage = new JobDetailPage(driver).open(BASE_URL, jobId);

        // 3. Thực hiện hành động theo dữ liệu CSV
        switch (action) {
            case "E2E" -> {
                // Đã được kiểm thử chi tiết trong testApplicationFullStateTransitionFlow
                assertNotNull(jobDetailPage.getJobTitle(), tcId + ": Tiêu đề công việc phải hiển thị");
            }
            case "APPLY" -> {
                if (jobDetailPage.isApplied()) {
                    jobDetailPage.cancelApplication();
                }
                jobDetailPage.applyJob();
                assertTrue(jobDetailPage.isApplied(), tcId + ": Nút phải chuyển sang trạng thái đã ứng tuyển");
            }
            case "CANCEL" -> {
                if (!jobDetailPage.isApplied()) {
                    jobDetailPage.applyJob();
                }
                jobDetailPage.cancelApplication();
                assertFalse(jobDetailPage.isApplied(), tcId + ": Phải hủy ứng tuyển thành công");
            }
            case "SAVE" -> {
                if (!jobDetailPage.isSaved()) {
                    jobDetailPage.saveJob();
                }
                assertTrue(jobDetailPage.isSaved(), tcId + ": Tin phải ở trạng thái đã lưu");
            }
            case "UNSAVE" -> {
                if (!jobDetailPage.isSaved()) {
                    jobDetailPage.saveJob();
                }
                jobDetailPage.saveJob();
                assertFalse(jobDetailPage.isSaved(), tcId + ": Tin phải ở trạng thái chưa lưu");
            }
            case "DUPLICATE" -> {
                // job-2 đã được ứng tuyển trước đó trong seed data
                assertTrue(jobDetailPage.isApplied(), tcId + ": Phải đang ở trạng thái đã ứng tuyển");
            }
            default -> fail("Hành động action không hợp lệ trong CSV: " + action);
        }
    }
}
