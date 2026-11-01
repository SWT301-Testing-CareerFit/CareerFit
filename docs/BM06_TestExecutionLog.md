# BM06 – NHẬT KÝ THỰC THI KIỂM THỬ (TEST EXECUTION LOG)
## Dự án: CareerFit – Nền tảng Tuyển dụng IT & Định hướng Kỹ năng Thông minh
### Môn học: SWT301 – Software Testing | Học kỳ: Fall 2026 | FPT University

---

| Thông tin tài liệu | Chi tiết |
|---|---|
| **Mã tài liệu** | `BM06_TestExecutionLog` |
| **Tiêu chuẩn áp dụng** | ISO/IEC/IEEE 29119-3 (Test Execution Log) & ISTQB CTFL v4.0 |
| **Phiên bản** | 1.0 (Báo cáo kết quả kiểm thử hồi quy 4 Module M1–M4) |
| **Ngày thực thi** | 01/11/2026 |
| **Người thực hiện chính** | **Nguyễn Lâm Thi** (Test Lead) |
| **Người phối hợp & đối soát** | Võ Nguyên Khánh (Framework Owner), Nguyễn Trọng Tiến (Documentation Owner), Lã Trọng Huy (Defect Manager) |
| **Trạng thái** | Completed & Verified |

---

## 1. MỤC TIÊU THỰC THI (EXECUTION OBJECTIVE)

Nhật ký thực thi kiểm thử nhằm ghi nhận chi tiết, minh bạch toàn bộ quá trình chạy kiểm thử hồi quy tự động (Automated Regression Testing) cho toàn bộ hệ thống CareerFit bao gồm 4 Module nghiệp vụ cốt lõi (M1: Đăng nhập, M2: Đăng ký, M3: Tìm kiếm việc làm, M4: Ứng tuyển & Theo dõi đơn) cùng bộ tiện ích kiểm thử nền tảng.

Mục tiêu chính:
1. Đánh giá tính ổn định của hệ thống qua tối thiểu **2 chu kỳ kiểm thử độc lập (RUN-01 và RUN-02)**.
2. Phân loại rạch ròi giữa **Lỗi kịch bản kiểm thử (Test Script Flakiness)** và **Lỗi nghiệp vụ thực tế của phần mềm (SUT Defects)**.
3. Xuất và lưu trữ báo cáo kiểm thử tự động HTML (**Maven Surefire Report**) kèm bằng chứng ảnh chụp màn hình tự động (`Failure Screenshots`) phục vụ đối soát và nghiệm thu.

---

## 2. MÔI TRƯỜNG KIỂM THỬ (TEST ENVIRONMENT SPECIFICATIONS)

Toàn bộ các chu kỳ kiểm thử được thực thi trong môi trường chuẩn hóa:

| Thành phần | Cấu hình thực tế | Ghi chú |
|---|---|---|
| **Hệ điều hành (OS)** | Microsoft Windows 10 Pro (x64) | Build 10.0.18363 |
| **Nền tảng Java (JDK)** | OpenJDK 21.0.11 Hotspot 64-bit | Runtime compile & test Java 17+ |
| **Trình duyệt & WebDriver** | Google Chrome v156 (win64) / ChromeDriver 156.0.8078.80 | Tự động phân giải bởi `WebDriverManager` |
| **Thư viện Automation** | Selenium WebDriver 4.21.0, JUnit Jupiter 5.10.3 | Áp dụng mô hình POM & Data-Driven CSV |
| **Backend SUT URL** | `http://localhost:8080` (Spring Boot 3.3.4, Hibernate 6) | Cổng REST API & Quản lý phiên JWT |
| **Frontend SUT URL** | `http://localhost:5173` (React 18 + Vite SPA) | Giao diện tương tác người dùng |
| **Cơ sở dữ liệu** | Microsoft SQL Server (`careerfit_db`) | 13 bảng thực thể + Seed Data mẫu |
| **Bảng mã ký tự** | UTF-8 (`-Dfile.encoding=UTF-8`, `chcp 65001`) | Hỗ trợ tiếng Việt có dấu đầy đủ |

---

## 3. TỔNG HỢP SO SÁNH 2 LƯỢT CHẠY KIỂM THỬ HỒI QUY (RUN-01 VS RUN-02)

| Chỉ số đo lường (Metrics) | Lần chạy 1 (RUN-01) | Lần chạy 2 (RUN-02) | Chênh lệch & Đánh giá |
|---|:---:|:---:|:---|
| **Lệnh thực thi** | `mvn clean test` | `mvn test surefire-report:report` | Kèm plugin xuất báo cáo HTML |
| **Tổng số Test Cases** | **59** | **59** | Đầy đủ 100% test suite 4 module + Framework |
| **Số ca Kiểm thử ĐẠT (Pass)** | **53** | **54** | **+1 Test Pass** (Khắc phục xong lỗi script mock) |
| **Số ca Kiểm thử LỖI (Failures)** | **1** | **1** | `TC-APP-006` (Lỗi SUT: Trạng thái đơn trùng lặp) |
| **Số ca BỊ LỖI NGOẠI LỆ (Errors)**| **5** | **4** | **-1 Error** (Khắc phục triệt để NPE ở `SampleFrameworkTest`) |
| **Số ca Bị bỏ qua (Skipped)** | **0** | **0** | 100% test case đều được thực thi |
| **Tỷ lệ thành công (Pass Rate)** | **89.8%** | **91.5%** | **Tăng +1.7%**, hệ thống đạt độ ổn định cao |
| **Thời gian thực thi tổng** | 06:26 min (386s) | 07:04 min (424s) | Thời gian bao gồm khởi chạy trình duyệt thật |
| **Số lượng ảnh chụp lỗi sinh ra** | 7 ảnh PNG | 5 ảnh PNG | Lưu trữ tại `docs/reports/screenshots/` |
| **Báo cáo HTML sinh ra** | Chưa xuất | `surefire-report.html` (67.3 KB) | Lưu trữ tại `docs/reports/surefire-report.html` |

> [!NOTE]
> Sự khác biệt giữa **RUN-01** và **RUN-02** nằm ở việc đội ngũ kiểm thử đã xác định và khắc phục ngay một lỗi kỹ thuật (flaky script) trong `ScreenshotOnFailure.java` (lỗi NullPointerException khi gọi ExtensionContext giả lập). Bước vào **RUN-02**, 100% các ca Fail/Error còn lại đều là **Lỗi thực tế của phần mềm (SUT Defects)** đã được định danh và đối chiếu với GitHub Issues.

---

## 4. CHI TIẾT THỰC THI THEO TỪNG MODULE NGHIỆP VỤ (MODULE BREAKDOWN)

### 4.1. Module M1: Đăng nhập & Phân quyền (`LoginTest.java`)
* **Tổng số test:** 12 test cases (Đọc từ `login_data.csv`).
* **Kết quả:** **12/12 PASS (100%)**.
* **Đánh giá:** Xác thực thành công các luồng đăng nhập hợp lệ (Candidate, Employer, Admin), kiểm tra tính năng bắt buộc trường (HTML5 validation), kiểm tra thông báo lỗi khi sai mật khẩu hoặc tài khoản không tồn tại.

### 4.2. Module M2: Đăng ký tài khoản (`RegisterTest.java`)
* **Tổng số test:** 15 test cases (Đọc từ `register_data.csv` + Unit tests).
* **Kết quả:** **15/15 PASS (100%)**.
* **Đánh giá:** Hoạt động xuất sắc trên cả 2 vai trò Ứng viên và Doanh nghiệp. Kiểm tra nghiêm ngặt quy tắc độ dài mật khẩu (BVA: 5, 6, 8, 32, 33 ký tự), định dạng email, xác nhận mật khẩu không khớp.

### 4.3. Module M3: Tìm kiếm & Lọc việc làm (`JobSearchTest.java`)
* **Tổng số test:** 14 test cases (Đọc từ `search_data.csv` + Filter tests).
* **Kết quả:** **14/14 PASS (100%)**.
* **Đánh giá:** Tìm kiếm từ khóa chính xác, tìm kiếm không phân biệt hoa thường, lọc theo địa điểm (Hà Nội, TP.HCM, Đà Nẵng), lọc theo cấp bậc (Intern, Fresher, Junior) và lọc theo khoảng lương.

### 4.4. Module M4: Ứng tuyển & Quản lý đơn Kanban (`ApplicationTest.java`)
* **Tổng số test:** 9 test cases (Đọc từ `apply_data.csv` + State Transition tests).
* **Kết quả:** **4 PASS / 5 FAIL (44.4% Pass)**.
* **Chi tiết các ca không đạt:**
  1. `testApplicationFullStateTransitionFlow`: Timeout 10s tại bước bấm nộp đơn `applyJob()`.
  2. `testKanbanViewSwitchingAndSearch`: Timeout 10s do bị chuyển hướng ngược về trang đăng nhập.
  3. `testApplyDataDriven[2]` (TC-APP-005): Timeout 10s tại nút ứng tuyển.
  4. `testApplyDataDriven[3]` (TC-APP-008): Timeout 10s tại nút hủy ứng tuyển.
  5. `testApplyDataDriven[6]` (TC-APP-006): `AssertionFailedError: expected <true> but was <false>`.
* **Đánh giá:** 5 ca thất bại này **hoàn toàn không phải do lỗi script**, mà do hành vi thực tế của hệ thống web bị kẹt phiên đăng nhập và mất session (SUT Defects).

### 4.5. Bộ Framework Utilities & Kiểm thử nền tảng
* `ConfigReaderTest`: **3/3 PASS (100%)**
* `DataGeneratorTest`: **4/4 PASS (100%)**
* `SampleFrameworkTest`: **2/2 PASS (100%)** *(Đã sửa lỗi mock store sau RUN-01)*

---

## 5. MA TRẬN TRUY VẾT LỖI TỰ ĐỘNG (AUTOMATED DEFECT TRACEABILITY MATRIX)

Toàn bộ các ca thất bại trong kiểm thử tự động đã được chuyển giao cho **Defect Manager (Lã Trọng Huy)** phân loại và truy vết trực tiếp tới các **Defect Reports trên GitHub Issues**:

| STT | Mã Test Case | Hiện tượng phát hiện khi Test tự động | Phân loại lỗi | Mã Defect (BM07) | GitHub Issue liên kết | Bằng chứng ảnh chụp màn hình |
|:---:|:---|:---|:---:|:---:|:---:|:---|
| 1 | `TC-APP-E2E` | Đăng nhập tài khoản ứng viên thành công nhưng màn hình bị kẹt lại ở `/login`, không tự chuyển hướng | **SUT Defect** | `BUG-03` | [Issue #28](https://github.com/SWT301-Testing-CareerFit/CareerFit/issues/28) | `ApplicationTest_testApplicationFullStateTransitionFlow_*.png` |
| 2 | `TC-APP-005` | Tài khoản ứng viên trong danh sách fallback bị lỗi quyền truy cập nút "Ứng tuyển ngay" | **SUT Defect** | `BUG-01` | [Issue #26](https://github.com/SWT301-Testing-CareerFit/CareerFit/issues/26) | `ApplicationTest_testApplyDataDriven_*.png` |
| 3 | `TC-APP-007`<br>`TC-APP-014` | Mở trực tiếp URL Kanban `/candidate/applications` bị mất phiên đăng nhập, văng về trang Login | **SUT Defect** | `BUG-02` | [Issue #27](https://github.com/SWT301-Testing-CareerFit/CareerFit/issues/27) | `ApplicationTest_testKanbanViewSwitchingAndSearch_*.png` |
| 4 | `TC-APP-001` | Khách vãng lai bấm ứng tuyển chuyển về login nhưng mất vị trí tin tuyển dụng đang xem | **SUT Defect** | `BUG-04` | [Issue #29](https://github.com/SWT301-Testing-CareerFit/CareerFit/issues/29) | `docs/reports/screenshots/` |
| 5 | `SampleFrameworkTest` | NullPointerException tại `context.getStore` khi chạy Unit test giả lập ExtensionContext | **Test Script Bug** | *Flaky Fixed* | Sửa trong PR #11 | Đã giải quyết triệt để tại RUN-02 |

---

## 6. LƯU TRỮ VÀ DẪN XUẤT BÁO CÁO (ARTIFACT REPOSITORY)

Tất cả các tệp minh chứng kết quả kiểm thử hồi quy được đóng gói và lưu trữ chính thức trong kho lưu trữ mã nguồn tại:

* 📄 **Báo cáo Surefire Report HTML đầy đủ:**  
  `docs/reports/surefire-report.html` *(kèm toàn bộ thư mục tài nguyên `css/`, `fonts/`, `images/`, `js/` độc lập, có thể mở trực tiếp trên mọi trình duyệt mà không cần kết nối mạng).*
* 🖼️ **Thư mục ảnh chụp màn hình bằng chứng lỗi tự động:**  
  `docs/reports/screenshots/`
* 📝 **File log thô chi tiết từ Maven Surefire XML:**  
  `backend/target/surefire-reports/`

---

## 7. KẾT LUẬN & ĐÁNH GIÁ ĐỦ ĐIỀU KIỆN NGHIỆM THU (EXIT CRITERIA EVALUATION)

1. **Tính ổn định của kịch bản kiểm thử tự động (Framework Stability):** Đạt **100%**. Bộ kịch bản Selenium 4 tuân thủ nghiêm ngặt 8 nguyên tắc vàng của POM: không chứa assertion trong Page Object, không sử dụng `Thread.sleep()`, không hard-code dữ liệu (đọc 100% từ CSV), xử lý wait rõ ràng.
2. **Độ bao phủ kiểm thử (Test Coverage):** Toàn bộ 41 test cases nghiệp vụ quy định trong BM03 và BM05 đã được thực thi đầy đủ qua 2 chu kỳ độc lập.
3. **Phát hiện lỗi phần mềm (Defect Detection):** Bộ test tự động đã phát hiện thành công **4 lỗi phần mềm nghiêm trọng (BUG-01 đến BUG-04)** liên quan đến quản lý trạng thái phiên làm việc và điều hướng SPA.
4. **Kết luận:** Bộ kiểm thử hồi quy đáp ứng đầy đủ tiêu chí hoàn thành (**Definition of Done**) của **Issue #11**, sẵn sàng bàn giao kết quả cho báo cáo tổng kết **BM08 – Test Summary Report** (Issue #14).
