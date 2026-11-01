package com.careerfit.automation.tests;

import com.careerfit.automation.base.BaseTest;
import com.careerfit.automation.pages.RegisterPage;
import com.careerfit.automation.utils.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for Module 2: Candidate & Employer Registration with Business License Upload.
 *
 * Implements strict POM architecture and Data-Driven Testing (DDT):
 * - Reads test vectors from /testdata/register_data.csv.
 * - Test class contains 100% of Assertions and ZERO By locators.
 * - 100% Explicit Wait, zero Thread.sleep().
 * - Dynamic email generation using {RANDOM} placeholder and DataGenerator.
 */
@Tag("module-register")
@DisplayName("M2 - Kiểm thử Đăng ký tài khoản Ứng viên & Doanh nghiệp (POM + Data-Driven)")
public class RegisterTest extends BaseTest {

    @ParameterizedTest(name = "[{index}] {0} - Vai trò: {1} | Email: ''{5}'' | Kỳ vọng: {9}")
    @CsvFileSource(resources = "/testdata/register_data.csv", numLinesToSkip = 1, encoding = "UTF-8")
    @DisplayName("DDT: Kiểm thử Đăng ký tài khoản theo bộ dữ liệu CSV")
    public void testRegisterDataDriven(String tcId,
                                       String role,
                                       String fullName,
                                       String company,
                                       String licenseFile,
                                       String email,
                                       String phone,
                                       String password,
                                       String confirmPassword,
                                       String expectedResult,
                                       String expectedMessage) {
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.open(BASE_URL);

        // Sinh email duy nhất nếu có placeholder {RANDOM} để tránh trùng lặp tài khoản
        String safeEmail = (email == null) ? "" : email.trim();
        if (safeEmail.contains("{RANDOM}")) {
            safeEmail = safeEmail.replace("{RANDOM}", DataGenerator.getTimestamp() + "_" + UUID.randomUUID().toString().substring(0, 4));
        }

        // Xác định đường dẫn tuyệt đối cho file GPKD nếu có khai báo
        String safeLicensePath = "";
        if (licenseFile != null && !licenseFile.trim().isEmpty()) {
            Path filePath = Paths.get("src/test/resources/testdata", licenseFile.trim());
            if (Files.exists(filePath)) {
                safeLicensePath = filePath.toAbsolutePath().toString();
            } else {
                Path targetPath = Paths.get("target/test-classes/testdata", licenseFile.trim());
                if (Files.exists(targetPath)) {
                    safeLicensePath = targetPath.toAbsolutePath().toString();
                } else {
                    safeLicensePath = filePath.toAbsolutePath().toString();
                }
            }
        }

        String safeRole = (role == null) ? "CANDIDATE" : role.trim().toUpperCase();
        String safeFullName = (fullName == null) ? "" : fullName.trim();
        String safeCompany = (company == null) ? "" : company.trim();
        String safePhone = (phone == null) ? "" : phone.trim();
        String safePassword = (password == null) ? "" : password;
        String safeConfirmPassword = (confirmPassword == null) ? "" : confirmPassword;

        // Thực hiện đăng ký dựa trên vai trò
        if ("EMPLOYER".equals(safeRole)) {
            registerPage.registerEmployer(
                    safeFullName,
                    safeCompany,
                    safeLicensePath,
                    safeEmail,
                    safePhone,
                    safePassword,
                    safeConfirmPassword
            );
        } else {
            registerPage.registerCandidate(
                    safeFullName,
                    safeEmail,
                    safePhone,
                    safePassword,
                    safeConfirmPassword
            );
        }

        // Kiểm tra kết quả mong đợi
        switch (expectedResult) {
            case "SUCCESS":
                boolean hasSuccess = registerPage.waitUntilSuccessDisplayed();
                String actualErr = (!hasSuccess && registerPage.isErrorDisplayed()) ? registerPage.getErrorMessage() : "";
                assertTrue(hasSuccess,
                        "[" + tcId + "] Đăng ký thành công bắt buộc phải hiển thị thông báo thành công (auth-success). Lỗi hiển thị: " + actualErr);

                if (expectedMessage != null && !expectedMessage.trim().isEmpty()) {
                    String actualSuccess = registerPage.getSuccessMessage().toLowerCase();
                    String expectedKeyword = expectedMessage.trim().toLowerCase();
                    assertTrue(actualSuccess.contains(expectedKeyword),
                            "[" + tcId + "] Thông báo thành công thực tế '" + actualSuccess + "' không chứa từ khóa mong đợi '" + expectedKeyword + "'.");
                }
                break;

            case "FAILURE":
                boolean hasError = registerPage.waitUntilErrorDisplayed();
                assertTrue(hasError,
                        "[" + tcId + "] Đăng ký không hợp lệ bắt buộc phải hiển thị thông báo lỗi (auth-error).");

                if (expectedMessage != null && !expectedMessage.trim().isEmpty()) {
                    String actualError = registerPage.getErrorMessage().toLowerCase();
                    String expectedKeyword = expectedMessage.trim().toLowerCase();
                    assertTrue(actualError.contains(expectedKeyword),
                            "[" + tcId + "] Thông báo lỗi thực tế '" + actualError + "' không chứa từ khóa mong đợi '" + expectedKeyword + "'.");
                }
                break;

            default:
                fail("[" + tcId + "] Giá trị expectedResult không được hỗ trợ trong CSV: " + expectedResult);
        }
    }

    @Test
    @DisplayName("TC-REG-FORM-STRUCTURE: Kiểm tra chuyển đổi vai trò và hiển thị trường động theo vai trò")
    public void testRegisterRoleSwitchingAndFields() {
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.open(BASE_URL);

        // Mặc định chọn vai trò Ứng viên: kiểm tra các thuộc tính bắt buộc
        assertTrue(registerPage.isFullNameRequired(), "Trường Họ và tên phải có thuộc tính required.");
        assertTrue(registerPage.isEmailRequired(), "Trường Email phải có thuộc tính required.");
        assertTrue(registerPage.isPhoneRequired(), "Trường Số điện thoại phải có thuộc tính required.");
        assertTrue(registerPage.isPasswordRequired(), "Trường Mật khẩu phải có thuộc tính required.");
        assertTrue(registerPage.isConfirmPasswordRequired(), "Trường Xác nhận mật khẩu phải có thuộc tính required.");
        assertFalse(registerPage.isCompanyDisplayed(), "Trường Công ty không được hiển thị khi đang chọn vai trò Ứng viên.");
        assertFalse(registerPage.isLicenseDisplayed(), "Trường Upload GPKD không được hiển thị khi đang chọn vai trò Ứng viên.");

        // Chuyển sang vai trò Doanh nghiệp: hiển thị trường Tên công ty và Upload GPKD
        registerPage.selectRole("EMPLOYER");
        assertTrue(registerPage.isCompanyDisplayed(), "Trường Tên công ty phải hiển thị khi chọn vai trò Doanh nghiệp.");
        assertTrue(registerPage.isLicenseDisplayed(), "Trường Upload GPKD phải hiển thị khi chọn vai trò Doanh nghiệp.");

        // Chuyển lại vai trò Ứng viên: ẩn trường Công ty và GPKD
        registerPage.selectRole("CANDIDATE");
        assertFalse(registerPage.isCompanyDisplayed(), "Trường Công ty phải ẩn đi khi chuyển lại vai trò Ứng viên.");
        assertFalse(registerPage.isLicenseDisplayed(), "Trường Upload GPKD phải ẩn đi khi chuyển lại vai trò Ứng viên.");
    }

    @Test
    @DisplayName("TC-REG-SMOKE-EMPLOYER-UPLOAD: Kiểm thử trực tiếp luồng Doanh nghiệp upload file GPKD")
    public void testEmployerRegistrationWithLicenseUploadSmoke() {
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.open(BASE_URL);

        String randomEmail = DataGenerator.randomEmail("emp_smoke");
        Path licensePath = Paths.get("src/test/resources/testdata/sample_license.pdf");

        registerPage.registerEmployer(
                "Trần Văn Giám Đốc",
                "Tập Đoàn Công Nghệ " + DataGenerator.getTimestamp(),
                licensePath.toAbsolutePath().toString(),
                randomEmail,
                "0988776655",
                "Employer@2026",
                "Employer@2026"
        );

        assertTrue(registerPage.waitUntilSuccessDisplayed(),
                "Đăng ký tài khoản Doanh nghiệp kèm GPKD phải hiển thị thông báo thành công.");
        assertTrue(registerPage.getSuccessMessage().toLowerCase().contains("thành công"),
                "Thông báo phải chứa từ khóa 'thành công'.");
    }
}
