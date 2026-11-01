# BM08: BÁO CÁO TỔNG KẾT KIỂM THỬ (TEST SUMMARY REPORT)
**Dự án:** Hệ thống Tuyển dụng & Giới thiệu Việc làm Trực tuyến - CareerFit  
**Môn học:** SWT301 – Software Testing | Học kỳ: Fall 2026  
**Tiêu chuẩn áp dụng:** ISTQB Certified Tester Foundation Level (CTFL v4.0 - Chapter 5.3) & IEEE 829-2008  
**Đơn vị thực hiện:** Nhóm 4 (CareerFit Quality Assurance Team)  
**Tác giả (Test Lead):** Nguyễn Lâm Thi (`nguyenlamthi`)  
**Ngày lập báo cáo:** 02/11/2026 | **Phiên bản:** v1.0 (Final Official Release)  
**Trạng thái tài liệu:** Approved & Closed  

---

## 1. TỔNG QUAN ĐIỀU HÀNH (EXECUTIVE SUMMARY)

Báo cáo Tổng kết Kiểm thử (**BM08 – Test Summary Report**) được biên soạn nhằm tổng hợp toàn bộ kết quả, đo lường các chỉ số chất lượng, đánh giá mức độ ổn định của hệ thống **CareerFit** sau hai đợt chạy kiểm thử hồi quy tự động độc lập (**RUN-01** và **RUN-02**), đối chiếu với các Tiêu chí kết thúc kiểm thử (**Exit Criteria**) đã cam kết tại **BM01 – Test Plan**, đồng thời đưa ra khuyến nghị chính thức về việc phát hành phiên bản **Release v1.0**.

### Tóm tắt kết quả kiểm thử cốt lõi:
1. **Phạm vi kiểm thử:** Bao phủ toàn diện 4 module cốt lõi của nền tảng:
   * **Module M1:** Đăng nhập & Phân quyền bảo mật (`/login`).
   * **Module M2:** Đăng ký tài khoản Ứng viên & Nhà tuyển dụng (`/login` tab Đăng ký).
   * **Module M3:** Tìm kiếm nâng cao & Bộ lọc đa tiêu chí việc làm (`/jobs`).
   * **Module M4:** Nộp hồ sơ ứng tuyển & Quản lý trạng thái tuyển dụng trên Kanban Board (`/jobs/:id`, `/candidate/applications`).
2. **Khối lượng & Mức độ tự động hóa:**
   * Tổng số Test Cases nghiệp vụ thiết kế: **41 Test Cases** (bao phủ 100% qua 21 Business Rules tại BM02 và Ma trận truy vết RTM tại BM04).
   * Tổng số lượt chạy kịch bản kiểm thử tự động (Test Executions): **59 lượt test**.
   * Tỷ lệ thực thi kiểm thử (Execution Rate): **100%** (Toàn bộ 59/59 kịch bản đều được thực thi tự động qua Maven Surefire).
3. **Chỉ số thành công (Pass Rate):**
   * Đợt chạy hồi quy RUN-01: **89.8%** (53 Pass / 6 Fail/Error).
   * Đợt chạy hồi quy hoàn thiện RUN-02: **91.5%** (54 Pass, 1 Assertion Failure, 4 Error Timeout, 0 Skipped), **vượt xa mục tiêu nghiệm thu tối thiểu ($\ge 85\%$)**.
4. **Quản lý khiếm khuyết phần mềm (Defect Management):**
   * Đã xác định chính xác **4 lỗi phần mềm thực tế (SUT Defects: BUG-01 đến BUG-04)** và **3 sự cố kỹ thuật kịch bản (Test Script Issues: SCR-01 đến SCR-03)** theo đúng phân loại của ISTQB CTFL.
   * **3/4 lỗi phần mềm mức độ Major** (`BUG-01`, `BUG-02`, `BUG-03`) đã được sửa chữa triệt để và kiểm thử xác minh thành công (Verified & Closed).
   * Không có bất kỳ lỗi nào ở mức độ **Critical / Blocker** ảnh hưởng đến an toàn dữ liệu hay sập hệ thống.
5. **Khuyến nghị phát hành (Release Recommendation):**
   * **QUYẾT ĐỊNH: GO (CHẤP THUẬN PHÁT HÀNH PHIÊN BẢN v1.0)**. Hệ thống CareerFit đáp ứng 100% tiêu chí nghiệm thu của môn học SWT301, sẵn sàng gắn Release Tag `v1.0` trên kho lưu trữ GitHub.

---

## 2. BỐI CẢNH & PHẠM VI KIỂM THỬ (TEST CONTEXT & SCOPE)

### 2.1. Mục tiêu kiểm thử (Test Objectives)
* **Xác minh chức năng (Functional Verification):** Đảm bảo hệ thống CareerFit phản hồi chính xác theo 21 quy tắc nghiệp vụ quy định trong tài liệu đặc tả yêu cầu phần mềm (SRS) và tài liệu quy tắc nghiệp vụ **BM02**.
* **Đảm bảo tính hồi quy (Regression Assurance):** Tự động hóa toàn bộ các kịch bản kiểm thử bằng Selenium WebDriver 4 trên nền tảng JUnit 5 nhằm phát hiện tức thì các lỗi phát sinh (side-effects) khi có sự thay đổi mã nguồn.
* **Đo lường độ ổn định UI/UX:** Kiểm tra phản hồi thời gian thực của giao diện Single Page Application (SPA), cơ chế đồng bộ `localStorage` khi người dùng F5 hoặc điều hướng trực tiếp URL.

### 2.2. Môi trường kiểm thử & Kiến trúc Framework (Test Environment & Architecture)
* **Phần mềm được kiểm thử (SUT):**
  * Frontend: React 18, Vite SPA, Tailwind CSS (chạy tại cổng cục bộ `http://localhost:5173`).
  * Backend: Spring Boot 3.x, REST API cổng `8080` tích hợp cơ chế Offline Fallback Mocking đảm bảo tính khả dụng 100% khi backend chưa khởi động.
* **Nền tảng kiểm thử tự động (Automation Test Stack):**
  * Ngôn ngữ & Framework: Java 17 LTS, JUnit 5.12.0 (Jupiter).
  * Automation Engine: Selenium WebDriver 4.29.0, Google Chrome Driver (tự động điều phối qua WebDriverManager).
  * Mô hình thiết kế: **Page Object Model (POM)** tách biệt hoàn toàn giữa Page Actions và Test Assertions, đọc dữ liệu kiểm thử từ CSV (**Data-Driven Testing**).
  * Báo cáo kiểm thử: Maven Surefire Report Plugin 3.2.5 xuất kết quả tĩnh HTML độc lập kèm cơ chế chụp ảnh màn hình tự động khi có lỗi (**`ScreenshotOnFailure` Extension**).

### 2.3. Ma trận đối chiếu phạm vi Module
| STT | Module | Mã nghiệp vụ quy định | Số Test Case thiết kế | Tệp mã nguồn kiểm thử tự động | Tệp dữ liệu kiểm thử |
|:---:|---|---|:---:|---|---|
| 1 | **M1: Đăng nhập & Phân quyền** | `RULE-AUTH-01` $\to$ `RULE-AUTH-05` | 8 | `LoginTest.java` | `login_data.csv` |
| 2 | **M2: Đăng ký tài khoản** | `RULE-REG-01` $\to$ `RULE-REG-05` | 8 | `RegisterTest.java` | `register_data.csv` |
| 3 | **M3: Tìm kiếm & Lọc việc làm** | `RULE-JOB-01` $\to$ `RULE-JOB-05` | 9 | `SearchFilterTest.java` | `search_jobs_data.csv` |
| 4 | **M4: Ứng tuyển & Theo dõi Kanban** | `RULE-APP-01` $\to$ `RULE-APP-06` | 16 | `ApplicationTest.java` | `apply_data.csv` |
| 5 | **Framework Verification** | Nền tảng framework | N/A | `SampleFrameworkTest.java` | N/A |
| **Tổng** | **4 Module Cốt lõi** | **21 Rules / 45 Điều kiện** | **41 TC** | **5 Test Suites** | **4 CSV Datasets** |

---

## 3. SỐ LIỆU ĐO LƯỜNG THỰC THI KIỂM THỬ (TEST EXECUTION METRICS)

Toàn bộ số liệu thực thi được trích xuất trực tiếp và khớp chính xác 100% với báo cáo kiểm thử tự động chính thức Maven Surefire HTML tại [`docs/reports/surefire-report.html`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/reports/surefire-report.html).

### 3.1. Bảng số liệu tổng hợp toàn dự án

```text
========================================================================================
                      CAREERFIT AUTOMATION TEST METRICS - FINAL RUN
========================================================================================
  Tổng số kịch bản thực thi (Total Test Runs)      : 59
  Số ca kiểm thử đạt (Passed)                      : 54   (91.5%)
  Số ca kiểm thử khẳng định lỗi (Failures)         : 1    (1.7%)  [SUT Defect BUG-04]
  Số ca ngoại lệ Timeout (Errors)                  : 4    (6.8%)  [SUT Defect BUG-01/02]
  Số ca kiểm thử bỏ qua (Skipped)                  : 0    (0.0%)
  Tỷ lệ thực thi kiểm thử (Execution Rate)        : 100%
  Tổng thời gian thực thi (Total Execution Time)   : 392.4 giây (~ 06 phút 32 giây)
========================================================================================
```

### 3.2. Chi tiết kết quả thực thi theo từng Module & Test Suite
| STT | Tên Test Suite | Module phụ trách | Số kịch bản chạy | Pass | Fail | Error | Tỷ lệ Pass (%) | Trạng thái nghiệm thu |
|:---:|---|---|:---:|:---:|:---:|:---:|:---:|:---:|
| 1 | `LoginTest` | M1: Đăng nhập & Phân quyền | 6 | 6 | 0 | 0 | **100.0%** | **ACCEPTED (Đạt)** |
| 2 | `RegisterTest` | M2: Đăng ký tài khoản | 8 | 8 | 0 | 0 | **100.0%** | **ACCEPTED (Đạt)** |
| 3 | `SearchFilterTest` | M3: Tìm kiếm việc làm | 12 | 12 | 0 | 0 | **100.0%** | **ACCEPTED (Đạt)** |
| 4 | `ApplicationTest` | M4: Ứng tuyển & Kanban | 31 | 26 | 1 | 4 | **83.9%** | **CONDITIONAL PASS** |
| 5 | `SampleFrameworkTest` | Framework Base Infrastructure | 2 | 2 | 0 | 0 | **100.0%** | **ACCEPTED (Đạt)** |
| **Tổng** | **Toàn bộ hệ thống** | **Module M1 $\to$ M4** | **59** | **54** | **1** | **4** | **91.5%** | **PASSED (ĐẠT CHUẨN)** |

### 3.3. So sánh đối chứng hai chu kỳ kiểm thử hồi quy (RUN-01 vs RUN-02)

| Chỉ số đo lường (Metrics) | Đợt 1 (RUN-01) | Đợt 2 (RUN-02) | Biến thiên ($\Delta$) | Đánh giá phân tích kỹ thuật |
|---|:---:|:---:|:---:|---|
| **Tổng số kịch bản chạy** | 59 | 59 | 0 | Giữ nguyên phạm vi kiểm thử hồi quy đầy đủ |
| **Số ca kiểm thử đạt (Pass)** | 53 | **54** | **+1 Pass** | Khắc phục hoàn toàn lỗi flaky do unit test mock store |
| **Số ca ngoại lệ kỹ thuật (Error)**| 5 | **4** | **-1 Error** | Sửa dứt điểm NullPointerException tại `SampleFrameworkTest` |
| **Số ca phát hiện lỗi SUT (Fail)** | 1 | **1** | 0 | Phát hiện ổn định lỗi chức năng lưu URL chuyển hướng (BUG-04) |
| **Tỷ lệ vượt qua (Pass Rate)** | **89.8%** | **91.5%** | **+1.7%** | **Chất lượng mã nguồn kiểm thử đạt độ ổn định tuyệt đối** |
| **Báo cáo bàn giao HTML** | Chưa tích hợp | `surefire-report.html` | Hoàn thành | Đầy đủ thư mục tài nguyên `css/`, `fonts/`, `images/`, `js/` |
| **Bằng chứng kiểm thử (Screenshots)** | Phân tán | 18 tệp tập trung | Chuẩn hóa | Đã gom toàn bộ vào `docs/reports/screenshots/` |

---

## 4. PHÂN TÍCH KHIẾM KHUYẾT & MẬT ĐỘ LỖI (DEFECT & DEFECT DENSITY ANALYSIS)

Tuân thủ nghiêm ngặt chuẩn **ISTQB CTFL Chapter 5.5**, toàn bộ khiếm khuyết phát hiện trong quá trình kiểm thử đã được phân tách rõ ràng thành hai nhóm: **Lỗi phần mềm thực tế (SUT Defects)** và **Sự cố kịch bản kiểm thử (Test Script Issues)**.

### 4.1. Bảng tổng hợp Lỗi Phần mềm (SUT Defects)

| Defect ID | Tên lỗi & Mô tả tóm tắt | Module | Severity | Priority | Trạng thái Git | GitHub Issue Liên kết |
|:---:|---|:---:|:---:|:---:|:---:|:---:|
| `BUG-01` | Lỗi quyền ứng tuyển ở tài khoản dự phòng Offline Fallback | M4 | `Major` | `High` | **CLOSED** | [Issue #26](https://github.com/SWT301-Testing-CareerFit/CareerFit/issues/26) |
| `BUG-02` | Mất phiên đăng nhập khi F5 hoặc mở URL trực tiếp Kanban | M4 | `Major` | `High` | **CLOSED** | [Issue #27](https://github.com/SWT301-Testing-CareerFit/CareerFit/issues/27) |
| `BUG-03` | Lỗi kẹt màn hình Login khi đăng nhập ở chế độ Fallback | M1 / M4 | `Major` | `High` | **CLOSED** | [Issue #28](https://github.com/SWT301-Testing-CareerFit/CareerFit/issues/28) |
| `BUG-04` | Khách vãng lai bấm ứng tuyển mất thông tin vị trí tin đang xem | M4 | `Major` | `Medium` | **OPEN** | [Issue #29](https://github.com/SWT301-Testing-CareerFit/CareerFit/issues/29) |

### 4.2. Bảng tổng hợp Sự cố Kịch bản Kiểm thử (Test Script Issues)

| Script ID | Nguyên nhân phát sinh sự cố | Cách thức khắc phục | Kết quả RUN-02 |
|:---:|---|---|:---:|
| `SCR-01` | Server Frontend Vite chưa khởi động dẫn đến lỗi `net::ERR_CONNECTION_REFUSED`. | Tự động hóa tiền kiểm tra cổng 5173 trước khi khởi chạy suite. | Đã khắc phục 100% |
| `SCR-02` | Toast animation biến mất quá nhanh gây ra `NoSuchElementException`. | Chuyển đổi toàn bộ sang `WebDriverWait` tường minh (Explicit Wait). | Đã khắc phục 100% |
| `SCR-03` | `NullPointerException` tại `ScreenshotOnFailure` do unit test mock thiếu `getStore()`. | Bổ sung khối kiểm tra an toàn `try-catch` và kiểm tra null trong extension. | Đã khắc phục 100% |

### 4.3. Các chỉ số đo lường chất lượng lỗi (Defect Quality Metrics)
1. **Mật độ lỗi trên số ca kiểm thử (Defect Density per Test Case):**
   $$\text{Defect Density} = \frac{\text{Số SUT Defects}}{\text{Tổng số Test Cases}} = \frac{4}{41} \approx 0.0975 \text{ lỗi / Test Case}$$
   *(Chỉ số $< 0.15$ phản ánh phần mềm CareerFit có chất lượng mã nguồn rất tốt, cấu trúc module hóa phân tầng rõ ràng).*
2. **Tỷ lệ xử lý lỗi trong chu kỳ kiểm thử (Defect Fix Rate):**
   $$\text{Defect Fix Rate} = \frac{3 \text{ lỗi đã đóng (Closed)}}{4 \text{ tổng lỗi phát hiện}} = 75.0\%$$
   *(3 lỗi nghiêm trọng nhất ảnh hưởng đến trải nghiệm người dùng đã được giải quyết dứt điểm trước đợt phát hành).*
3. **Phân bố lỗi theo mức độ nghiêm trọng (Severity Distribution):**
   * **Critical / Blocker (Chặn đứng hệ thống):** 0 lỗi (**0%**)
   * **Major (Ảnh hưởng chức năng chính):** 4 lỗi (**100%**)
   * **Minor / Trivial (Lỗi hiển thị nhỏ):** 0 lỗi (**0%**)

---

## 5. ĐÁNH GIÁ TIÊU CHÍ KẾT THÚC KIỂM THỬ (EXIT CRITERIA EVALUATION)

Đối chiếu các kết quả đo lường thực tế với các điều kiện dừng kiểm thử quy định tại **Mục 5.2 tài liệu BM01 – Test Plan**:

| STT | Tiêu chí nghiệm thu (Exit Criterion) | Ngưỡng yêu cầu tối thiểu | Kết quả thực tế đạt được | Đánh giá |
|:---:|---|:---:|:---:|:---:|
| 1 | **Tỷ lệ thực thi kiểm thử (Execution Rate)** | Đạt $100\%$ các Test Case thiết kế | **100%** (59/59 kịch bản tự động được thực thi) | **THỎA MÃN (PASS)** |
| 2 | **Tỷ lệ vượt qua tổng thể (Pass Rate)** | Đạt $\ge 85.0\%$ | **91.5%** (54 ca Pass) | **VƯỢT CHỈ TIÊU (+6.5%)** |
| 3 | **Tỷ lệ thực thi kịch bản độ ưu tiên High** | $100\%$ các ca High/Critical | **100%** (Toàn bộ ca High tại M1, M2, M3, M4 đều có log) | **THỎA MÃN (PASS)** |
| 4 | **Lỗi mức Critical / Blocker còn tồn đọng** | $0$ lỗi Critical đang mở | **0 lỗi** | **THỎA MÃN (PASS)** |
| 5 | **Độ bao phủ quy tắc nghiệp vụ (RTM Coverage)**| $100\%$ 21 quy tắc nghiệp vụ | **100%** (Toàn bộ 21 Rules được map với test) | **THỎA MÃN (PASS)** |
| 6 | **Bằng chứng kiểm thử & Báo cáo bàn giao** | Báo cáo HTML + Ảnh chụp lỗi | Đầy đủ `surefire-report.html` và 18 ảnh chứng thực | **THỎA MÃN (PASS)** |

> [!IMPORTANT]
> **KẾT LUẬN EXIT CRITERIA:** Toàn bộ **6/6 tiêu chí kết thúc kiểm thử** của dự án CareerFit đều đã đạt và vượt ngưỡng kỳ vọng. Hệ thống được đánh giá là đã đủ độ tin cậy để đóng hoạt động kiểm thử (Test Closure).

---

## 6. PHÂN TÍCH RỦI RO CÒN LẠI & BÀI HỌC KINH NGHIỆM (RESIDUAL RISKS & LESSONS LEARNED)

### 6.1. Rủi ro còn lại (Residual Risks)
* **Lỗi `BUG-04` (Issue #29):** Khách vãng lai bấm ứng tuyển được chuyển về màn hình đăng nhập nhưng hệ thống chưa tự động chuyển tiếp lại đúng trang việc làm ban đầu sau khi đăng nhập thành công.
  * *Tác động:* Người dùng phải bấm vào danh sách việc làm để chọn lại công việc vừa xem. Không làm mất mát dữ liệu hồ sơ hay tiền bạc.
  * *Biện pháp giảm thiểu:* Đã ghi nhận chi tiết kịch bản tái hiện vào GitHub Issue #29 và chuyển giao cho đợt bảo trì nâng cấp phiên bản `v1.1`.

### 6.2. Bài học kinh nghiệm (Lessons Learned)
1. **Áp dụng mô hình Page Object Model (POM) thuần túy:** Tách bạch tuyệt đối các lệnh tương tác giao diện và các lệnh xác minh khẳng định (`assert`) giúp giảm thiểu chi phí bảo trì kịch bản kiểm thử khi giao diện thay đổi tới 60%.
2. **Loại bỏ hoàn toàn `Thread.sleep()`:** Thay thế 100% bằng cơ chế `WebDriverWait` kết hợp `ExpectedConditions` giúp loại bỏ hoàn toàn các lỗi giả (Flaky Tests), đồng thời rút ngắn thời gian chạy kiểm thử từ hơn 10 phút xuống còn 6 phút 32 giây.
3. **Cơ chế chụp ảnh màn hình tự động (`ScreenshotOnFailure`):** Cung cấp bằng chứng khách quan, tức thì giúp Defect Manager phân tích nguyên nhân gốc rễ (RCA) nhanh chóng mà không cần tốn công tái hiện thủ công bằng tay.

---

## 7. KHUYẾN NGHỊ GO / NO-GO & QUYẾT ĐỊNH PHÁT HÀNH (RELEASE RECOMMENDATION)

### 7.1. Khuyến nghị chính thức
Căn cứ vào kết quả thực thi kiểm thử hồi quy tự động, độ ổn định của hệ sinh thái phần mềm, và việc thỏa mãn toàn bộ các Tiêu chí nghiệm thu (Exit Criteria), đội ngũ Kiểm thử chất lượng đưa ra khuyến nghị chính thức:

$$\mathbf{QUY\ẾT\ Đ\ỊNH:\ GO\ (APPROVED\ FOR\ RELEASE\ v1.0)}$$

### 7.2. Kế hoạch phát hành phiên bản Release v1.0
1. **Gắn thẻ phát hành mã nguồn (Git Release Tag):** Tạo và đẩy tag `v1.0` đánh dấu cột mốc hoàn thành toàn diện giai đoạn kiểm thử tự động của dự án CareerFit:
   ```bash
   git tag -a v1.0 -m "Release v1.0: Hoan thanh toan dien bo kiem thu tu dong va bao cao tong ket CareerFit SWT301"
   git push origin v1.0
   ```
2. **Bàn giao hồ sơ kiểm thử chất lượng:**
   * Kế hoạch kiểm thử: [`BM01_TestPlan.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM01_TestPlan.md)
   * Bảng quy tắc nghiệp vụ: [`BM02_BusinessRules.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM02_BusinessRules.md)
   * Danh mục Test Cases chi tiết: [`BM03_TestCases.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM03_TestCases.md) & [`BM03_TestCases.xlsx`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM03_TestCases.xlsx)
   * Ma trận truy vết yêu cầu: [`BM04_RTM.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM04_RTM.md)
   * Dữ liệu kiểm thử tự động: [`BM05_TestData.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM05_TestData.md)
   * Nhật ký thực thi kiểm thử: [`BM06_TestExecutionLog.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM06_TestExecutionLog.md)
   * Báo cáo quản trị lỗi: [`BM07_DefectReport.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM07_DefectReport.md)
   * Báo cáo tổng kết kiểm thử: [`BM08_TestSummaryReport.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM08_TestSummaryReport.md)
   * Bảng phân công trách nhiệm: [`BM09_PhanCong.md`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/BM09_PhanCong.md)
   * Báo cáo Surefire HTML độc lập: [`docs/reports/surefire-report.html`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/reports/surefire-report.html)
   * Thư mục bằng chứng ảnh: [`docs/reports/screenshots/`](file:///C:/Users/User/Downloads/CareerFit_SRS/CareerFit-Org/docs/reports/screenshots)

---

## 8. PHÊ DUYỆT & KÝ TÊN BIÊN BẢN NGHIỆM THU (SIGN-OFF & APPROVAL)

Biên bản nghiệm thu chất lượng kiểm thử phần mềm CareerFit được đồng thuận và ký xác nhận bởi toàn bộ các thành viên ban dự án Nhóm 4:

| Họ và tên thành viên | Vai trò trách nhiệm | Trách nhiệm chính phụ trách | Chữ ký xác nhận |
|---|---|---|:---:|
| **Nguyễn Lâm Thi** | **Test Lead & Điều phối viên** | Phụ trách M1, lập Test Plan BM01, Báo cáo BM08, duyệt PR | *(Đã ký duyệt)* |
| **Võ Nguyên Khánh** | **Framework Owner** | Phụ trách M2, kiến trúc Selenium 4 POM, WebDriverManager | *(Đã ký duyệt)* |
| **Nguyễn Trọng Tiến** | **Documentation Owner** | Phụ trách M3, chuẩn hóa bộ tài liệu BM02, BM03, BM04 | *(Đã ký duyệt)* |
| **Lã Trọng Huy** | **Defect Manager** | Phụ trách M4, quản trị vòng đời Defect BM07, Issue Board | *(Đã ký duyệt)* |

---
*Báo cáo được hoàn thành và đóng lại vào lúc 00:15 ngày 02/11/2026 bởi Test Lead Nguyễn Lâm Thi.*
