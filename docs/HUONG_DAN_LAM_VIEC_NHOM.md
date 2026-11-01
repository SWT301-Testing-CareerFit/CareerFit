# CẨM NANG HƯỚNG DẪN LÀM VIỆC NHÓM KIỂM THỬ TỰ ĐỘNG (SWT301)
## Dự án: CareerFit — Selenium 4 + JUnit 5 + POM + Dữ liệu CSV

> **Môn:** SWT301 — Software Testing | **Học kỳ:** Fall 2026 | **FPT University**  
> **Nhóm thực hiện:** 4 sinh viên  
> **Kho mã nguồn:** `https://github.com/SWT301-Testing-CareerFit/CareerFit`  
> **Mục tiêu:** Xây dựng bộ kiểm thử tự động Black-box theo chuẩn ISTQB CTFL v4.0 và ISO/IEC/IEEE 29119-3.

---

## 1. PHÂN CÔNG TRÁCH NHIỆM 4 THÀNH VIÊN (MA TRẬN RACI)

Nhóm áp dụng nguyên tắc **chia theo chiều dọc (theo module)**: Mỗi thành viên sở hữu trọn vẹn 1 Module từ A đến Z (Thiết kế Test Case, chuẩn bị file CSV, viết Page Object class, viết Test class, chạy test và báo cáo bug) kết hợp với 1 vai trò điều phối ngang:

| Thành viên | Module phụ trách (Dọc) | Vai trò điều phối (Ngang) | Trách nhiệm chính |
|---|---|---|---|
| **Nguyễn Lâm Thi** | **M1: Đăng nhập & Phân quyền** (`/login`) | **Test Lead** | - Viết Test Plan (BM01) & Test Summary Report (BM08).<br>- Điều phối GitHub Project, review và merge PR vào `main`.<br>- Tổng hợp và chạy Regression Test cuối kỳ. |
| **Võ Nguyên Khánh** | **M2: Đăng ký tài khoản** (`/login` tab Đăng ký) | **Framework Owner** | - Dựng khung Selenium ban đầu (`BaseTest`, `BasePage`, `ConfigReader`, `ScreenshotOnFailure`, `DataGenerator`).<br>- Cấu hình `pom.xml`, WebDriverManager, ChromeOptions. |
| **Nguyễn Trọng Tiến** | **M3: Tìm kiếm & Lọc việc làm** (`/jobs`) | **Documentation Owner** | - Quản lý và chuẩn hóa bộ tài liệu: BM02 (Business Rules), BM03 (Test Cases), BM04 (RTM - Ma trận truy vết).<br>- Kiểm tra tính nhất quán giữa tài liệu và code test. |
| **Lã Trọng Huy** | **M4: Ứng tuyển & Theo dõi đơn** (`/jobs/:id`, `/candidate/applications`) | **Defect Manager** | - Quản lý GitHub Issues, nhãn lỗi (`bug`, `severity`, `priority`).<br>- Kiểm duyệt bằng chứng lỗi (ảnh screenshot trong `target/screenshots/`).<br>- Theo dõi vòng đời defect (New → Open → Fixed → Closed). |

---

## 2. CẤU TRÚC THƯ MỤC AUTOMATION FRAMEWORK (POM)

Mọi code automation sẽ nằm tại thư mục `backend/src/test/` (hoặc module test) theo cấu trúc chuẩn:

```
src/test/
├── java/com/careerfit/automation/
│   ├── base/
│   │   ├── BaseTest.java             # Quản lý khởi tạo & đóng WebDriver Chrome (Framework Owner)
│   │   └── BasePage.java             # Chứa các hàm wait, click, type, getText dùng chung
│   ├── utils/
│   │   ├── ConfigReader.java         # Đọc cấu hình từ config.properties
│   │   ├── DataGenerator.java        # Sinh email, username ngẫu nhiên tránh trùng lặp
│   │   └── ScreenshotOnFailure.java  # JUnit 5 extension tự động chụp ảnh khi test fail
│   ├── pages/                        # PAGE OBJECT CLASSES
│   │   ├── LoginPage.java            # SV1 (Thi)
│   │   ├── RegisterPage.java         # SV2 (Khánh)
│   │   ├── JobSearchPage.java        # SV3 (Tiến)
│   │   └── JobDetailPage.java        # SV4 (Huy)
│   └── tests/                        # TEST SCRIPT CLASSES (Data-driven)
│       ├── LoginTest.java            # SV1 (Thi)
│       ├── RegisterTest.java         # SV2 (Khánh)
│       ├── JobSearchTest.java        # SV3 (Tiến)
│       └── ApplicationTest.java      # SV4 (Huy)
└── resources/
    ├── config.properties             # URL frontend (http://localhost:5173), timeout, headless...
    └── testdata/                     # DỮ LIỆU KIỂM THỬ CSV (Data-driven)
        ├── login_data.csv            # SV1 (Thi)
        ├── register_data.csv         # SV2 (Khánh)
        ├── search_data.csv           # SV3 (Tiến)
        └── apply_data.csv            # SV4 (Huy)
```

---

## 3. 8 NGUYÊN TẮC VÀNG VỀ PAGE OBJECT MODEL (POM) — BẮT BUỘC TUÂN THỦ

Khi chấm bài, giảng viên sẽ soi code rất kỹ các tiêu chí này. **Vi phạm sẽ bị trừ điểm trực tiếp:**

1. ❌ **Cấm Assertion trong Page class:** Page chỉ làm nhiệm vụ thao tác và trả về dữ liệu (ví dụ: `getText()`, `isDisplayed()`). Mọi hàm `assertEquals`, `assertTrue` **phải nằm ở Test class**.
2. ❌ **Cấm By / driver.findElement trong Test class:** Mọi `By.id(...)`, `By.cssSelector(...)` phải nằm hoàn toàn trong Page class.
3. ❌ **Cấm Thread.sleep():** Tuyệt đối không dùng `Thread.sleep()`. 100% sử dụng **Explicit Wait** (`WebDriverWait` + `ExpectedConditions`).
4. ❌ **Cấm XPath tuyệt đối:** Không dùng `/html/body/div[2]/...`. Ưu tiên locator theo thứ tự: `data-testid` > `id` (duy nhất) > `name` > CSS Selector > XPath tương đối.
5. ❌ **Cấm hard-code dữ liệu trong code test:** Mọi email, mật khẩu, từ khóa tìm kiếm, kết quả mong đợi phải đọc từ file **CSV** qua `@CsvFileSource`.
6. ❌ **Cấm sửa kết quả mong đợi trong CSV để né bug:** Nếu trang web bị lỗi nghiệp vụ, test case phải để **Fail** và mở **Defect Report (BM07)** để lấy điểm phát hiện bug.
7. ✅ **Mỗi test case phải độc lập:** Test này không được phụ thuộc vào kết quả của test kia.
8. ✅ **Tự động chụp màn hình khi fail:** Ảnh chụp màn hình khi fail sẽ tự động lưu vào `target/screenshots/` làm bằng chứng cho Defect Report.

---

## 4. HƯỚNG DẪN CHI TIẾT CÁCH VIẾT CODE CHO MỖI THÀNH VIÊN

### Bước 1: Viết Page Object (`src/test/java/.../pages/XPage.java`)
Page class đại diện cho một trang hoặc một form trên giao diện:

```java
package com.careerfit.automation.pages;

import com.careerfit.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    // 1. Khai báo Locators rõ ràng, ưu tiên data-testid
    private final By emailInput    = By.cssSelector("[data-testid='login-email']");
    private final By passwordInput = By.cssSelector("[data-testid='login-password']");
    private final By submitButton  = By.cssSelector("[data-testid='login-submit']");
    private final By errorMessage  = By.cssSelector("[data-testid='auth-error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // 2. Hàm mở trang
    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        waitVisible(emailInput);
        return this;
    }

    // 3. Hàm hành động nghiệp vụ
    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(submitButton);
    }

    // 4. Hàm lấy dữ liệu kiểm tra (không assert ở đây!)
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
}
```

---

### Bước 2: Thiết kế file CSV (`src/test/resources/testdata/x_data.csv`)
File CSV chứa các bộ dữ liệu tương ứng với Test Cases đã thiết kế (BM03):
* Mã hóa file dạng **UTF-8**.
* Cột đầu tiên luôn là `tcId`.
* Cột cuối cùng là `expectedResult` và `expectedMessage`.

```csv
tcId,email,password,expectedResult,expectedMessage
TC-LOGIN-001,candidate@gmail.com,Candidate@123,SUCCESS,
TC-LOGIN-002,employer@gmail.com,Employer@123,SUCCESS,
TC-LOGIN-003,candidate@gmail.com,WrongPass,FAIL,Email hoặc mật khẩu không chính xác.
TC-LOGIN-004,notexist@gmail.com,Candidate@123,FAIL,Email hoặc mật khẩu không chính xác.
TC-LOGIN-005,locked@gmail.com,123456,LOCKED,Tài khoản của bạn đã bị khóa
TC-LOGIN-006,,Candidate@123,REQUIRED,
```

---

### Bước 3: Viết Test Class Data-Driven (`src/test/java/.../tests/XTest.java`)
Test class đọc dữ liệu từ CSV và kiểm tra kết quả bằng Assertions:

```java
package com.careerfit.automation.tests;

import com.careerfit.automation.base.BaseTest;
import com.careerfit.automation.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("M1 - Đăng nhập & Phân quyền")
class LoginTest extends BaseTest {

    @ParameterizedTest(name = "{0}: email={1}")
    @CsvFileSource(resources = "/testdata/login_data.csv", numLinesToSkip = 1, encoding = "UTF-8")
    void testLoginDataDriven(String tcId, String email, String password, String expectedResult, String expectedMessage) {
        LoginPage loginPage = new LoginPage(driver).open(BASE_URL);
        loginPage.login(email, password);

        switch (expectedResult) {
            case "SUCCESS" -> {
                // Kiểm tra đã rời trang login và chuyển đến dashboard
                assertFalse(driver.getCurrentUrl().endsWith("/login"), tcId + ": Phải rời trang login");
            }
            case "FAIL", "LOCKED" -> {
                assertTrue(loginPage.isErrorDisplayed(), tcId + ": Phải hiển thị thông báo lỗi");
                assertTrue(loginPage.getErrorMessage().contains(expectedMessage), tcId + ": Thông báo lỗi không khớp");
            }
            case "REQUIRED" -> {
                assertTrue(driver.getCurrentUrl().contains("/login"), tcId + ": Không được submit khi thiếu trường");
            }
            default -> fail("Giá trị expectedResult không hợp lệ trong CSV: " + expectedResult);
        }
    }
}
```

---

## 5. QUY TRÌNH LÀM VIỆC TRÊN GIT & GITHUB (GITHUB FLOW)

Để tránh xung đột code (conflict) và được điểm tối đa phần làm việc nhóm:

### 1. Luôn cập nhật code mới nhất từ nhánh `main` trước khi làm:
```bash
git checkout main
git pull origin main
```

### 2. Tạo nhánh riêng cho tính năng/nhiệm vụ của mình:
* Cú pháp đặt tên nhánh: `feature/<module>-<mô tả ngắn>`
* Ví dụ:
  * Thi: `git checkout -b feature/M1-login-tests`
  * Khánh: `git checkout -b feature/M2-register-tests`
  * Tiến: `git checkout -b feature/M3-search-tests`
  * Huy: `git checkout -b feature/M4-application-tests`

### 3. Commit code nhỏ, thường xuyên theo chuẩn Conventional Commits:
* Cú pháp: `<type>(<scope>): <mô tả ngắn> (#số-issue)`
* Ví dụ:
  * `test(login): add LoginPage object and locators (#07)`
  * `test(register): add CSV test data and BVA cases (#08)`
  * `fix(search): update explicit wait for job card list (#09)`

### 4. Kiểm tra trước khi đẩy code lên:
Trước khi mở PR, hãy chạy test trên máy mình để đảm bảo code không bị lỗi:
```bash
mvn clean test -Dtest=LoginTest
```

### 5. Đẩy nhánh và tạo Pull Request (PR):
```bash
git push -u origin feature/<tên-nhánh-của-bạn>
```
* Vào giao diện GitHub, bấm **Compare & pull request**.
* Chọn **Reviewers**: Tag tên 1 bạn khác trong nhóm để review duyệt (ví dụ: Khánh review cho Thi, Thi review cho Tiến, Tiến review cho Huy, Huy review cho Khánh).
* Sau khi bạn kia duyệt (`Approve`), Test Lead (Thi) sẽ bấm **Squash and merge** vào `main`.

---

## 6. CHECKLIST TỰ KIỂM TRA TRƯỚC KHI NỘP BÀI (DEFINITION OF DONE)

- [ ] Đã chạy `mvn clean test` trên máy cá nhân và không bị crash.
- [ ] Không có bất kỳ dòng nào dùng `Thread.sleep()`.
- [ ] Không có assertion nào nằm trong thư mục `pages/`.
- [ ] Dữ liệu test 100% nằm trong file CSV (mã hóa UTF-8).
- [ ] Mọi Test Case bị Fail do lỗi website đều đã được mở **Defect Issue** trên GitHub kèm ảnh chụp màn hình.
- [ ] Mỗi thành viên đều có ít nhất 2–3 Pull Request được merge vào nhánh `main`.
