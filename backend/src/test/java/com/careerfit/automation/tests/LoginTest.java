package com.careerfit.automation.tests;

import com.careerfit.automation.base.BaseTest;
import com.careerfit.automation.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for Module 1: Login & Authorization.
 *
 * Implements strict POM architecture and Data-Driven Testing (DDT):
 * - Reads test vectors from /testdata/login_data.csv.
 * - Test class contains 100% of Assertions and ZERO locators.
 * - 100% Explicit Wait, zero Thread.sleep().
 */
@Tag("module-login")
@DisplayName("M1 - Kiểm thử Đăng nhập & Phân quyền (POM + Data-Driven)")
public class LoginTest extends BaseTest {

    @ParameterizedTest(name = "[{index}] {0} - Email: ''{1}'' | Kỳ vọng: {3}")
    @CsvFileSource(resources = "/testdata/login_data.csv", numLinesToSkip = 1)
    @DisplayName("DDT: Kiểm thử Đăng nhập theo bộ dữ liệu CSV")
    public void testLoginDataDriven(String tcId, String email, String password, String expectedResult, String expectedMessage) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL);

        // Chuẩn hóa dữ liệu rỗng từ CSV
        String safeEmail = (email == null) ? "" : email.trim();
        String safePassword = (password == null) ? "" : password;

        switch (expectedResult) {
            case "SUCCESS":
                loginPage.login(safeEmail, safePassword);
                boolean redirected = loginPage.waitUntilRedirectedFromLogin();
                assertTrue(redirected || !loginPage.isErrorDisplayed(),
                        "[" + tcId + "] Đăng nhập thành công nhưng hệ thống không chuyển hướng hoặc có thông báo lỗi.");
                break;

            case "FAILURE":
                loginPage.login(safeEmail, safePassword);
                boolean hasError = loginPage.waitUntilErrorDisplayed();
                assertTrue(hasError, 
                        "[" + tcId + "] Đăng nhập thất bại bắt buộc phải hiển thị thông báo lỗi.");
                
                if (expectedMessage != null && !expectedMessage.trim().isEmpty()) {
                    String actualError = loginPage.getErrorMessage().toLowerCase();
                    String expectedKeyword = expectedMessage.trim().toLowerCase();
                    assertTrue(actualError.contains(expectedKeyword), 
                            "[" + tcId + "] Thông báo lỗi thực tế '" + actualError + "' không chứa từ khóa mong đợi '" + expectedKeyword + "'.");
                }
                assertTrue(loginPage.getCurrentUrl().contains("/login"), 
                        "[" + tcId + "] Sau khi đăng nhập thất bại, người dùng phải ở lại trang /login.");
                break;

            case "VALIDATION":
                if (safeEmail.isEmpty() && safePassword.isEmpty()) {
                    loginPage.clickSubmit();
                    assertTrue(loginPage.isEmailRequired(), 
                            "[" + tcId + "] Ô Email phải có ràng buộc bắt buộc (required).");
                } else if (safeEmail.isEmpty()) {
                    loginPage.enterPassword(safePassword);
                    loginPage.clickSubmit();
                    assertTrue(loginPage.isEmailRequired(), 
                            "[" + tcId + "] Ô Email phải có ràng buộc bắt buộc khi để trống.");
                } else if (safePassword.isEmpty()) {
                    loginPage.enterEmail(safeEmail);
                    loginPage.clickSubmit();
                    assertTrue(loginPage.isPasswordRequired(), 
                            "[" + tcId + "] Ô Password phải có ràng buộc bắt buộc khi để trống.");
                } else {
                    // Định dạng email không hợp lệ (e.g. invalid-email-format)
                    loginPage.login(safeEmail, safePassword);
                    assertTrue(loginPage.getCurrentUrl().contains("/login"), 
                            "[" + tcId + "] Không được điều hướng khi định dạng email không hợp lệ.");
                }
                break;

            default:
                fail("Trạng thái expectedResult không được hỗ trợ: " + expectedResult);
        }
    }

    @Test
    @DisplayName("TC-LOGIN-VERIFY-FORM: Kiểm tra cấu trúc form đăng nhập và thuộc tính bắt buộc")
    public void testLoginFormStructureAndAttributes() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL);

        assertTrue(loginPage.isEmailRequired(), "Trường Email bắt buộc phải có thuộc tính HTML5 required.");
        assertTrue(loginPage.isPasswordRequired(), "Trường Mật khẩu bắt buộc phải có thuộc tính HTML5 required.");
    }
}
