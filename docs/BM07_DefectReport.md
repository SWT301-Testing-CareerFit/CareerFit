# BM07 – DEFECT REPORT (BÁO CÁO QUẢN LÝ LỖI & SỰ CỐ KIỂM THỬ)

**Dự án:** CareerFit – Hệ thống Kết nối Tuyển dụng Thông minh dành cho Sinh viên & Fresher  
**Môn học:** SWT301 – Software Testing (Đại học FPT - Fall 2026)  
**Chuẩn tham chiếu:** ISTQB CTFL v4.0 (Chương 5.5 Quản lý lỗi) & ISO/IEC/IEEE 29119-3  
**Người phụ trách (Defect Manager):** Lã Trọng Huy (`TrongHuy317` - SV4)  
**Phê duyệt (Test Lead):** Nguyễn Lâm Thi (`nguyenlamthi` - SV1)  
**Phiên bản:** 2.0 | **Ngày lập báo cáo:** 01/11/2026  
**Đợt chạy kiểm thử:** RUN-01 (Toàn diện 59 tests) $\to$ RUN-02 (Xuất báo cáo Surefire HTML)

---

## 1. MỤC TIÊU & QUY TRÌNH QUẢN LÝ DEFECT (ISTQB CTFL 5.5)

### 1.1. Mục tiêu tài liệu
Tài liệu này ghi nhận, phân loại và theo dõi toàn bộ các sự cố (Incidents / Defects / Bugs) phát hiện được trong quá trình chạy kiểm thử tự động của nhóm kiểm thử CareerFit (Selenium 4 + JUnit 5 + POM).  
Báo cáo đảm bảo tính minh bạch, khách quan và truy vết hai chiều (Bidirectional Traceability) giữa:
$$\text{Business Rules (BM02)} \longleftrightarrow \text{Test Cases (BM03)} \longleftrightarrow \text{Defect Report (BM07)} \longleftrightarrow \text{GitHub Issues}$$

### 1.2. Vòng đời của Defect (Defect Life Cycle)
Nhóm áp dụng vòng đời xử lý lỗi chuẩn ISTQB:

```
[New] ──(Defect Manager phân loại)──> [Open] ──(Dev sửa code)──> [Fixed] ──(Chạy RUN-02 Re-test)──> [Closed]
  │                                                                 │
  └──(Lỗi Script/Môi trường)──> [Rejected / test-script]           └──(Re-test thất bại)──> [Reopen]
```

* **New:** Lỗi mới được phát hiện từ lần chạy tự động (RUN-01).
* **Open:** Defect Manager xác định là lỗi hệ thống thật (SUT Bug), tạo GitHub Issue và đưa vào BM07.
* **Fixed:** Đội ngũ phát triển đã cập nhật mã nguồn để sửa lỗi.
* **Closed:** Kiểm thử tự động chạy lại (RUN-02) xác nhận lỗi đã được khắc phục hoàn toàn (Test PASS).
* **Rejected (`test-script`):** Lỗi kịch bản kiểm thử (sai locator, timeout, server chưa chạy) – **KHÔNG tạo bug report**.

### 1.3. Tiêu chí phân loại lỗi theo chuẩn ISTQB CTFL 5.5

| Phân loại | Khái niệm theo ISTQB | Hướng xử lý |
|---|---|---|
| **Lỗi Hệ thống (SUT Bug)** | Sự sai lệch (Deviation) giữa hành vi thực tế của website với yêu cầu nghiệp vụ trong BM02 / User Stories. | **Tạo GitHub Issue** theo mẫu `.github/ISSUE_TEMPLATE/bug_report.md`, gắn nhãn `bug`, `severity`, `priority` và ghi nhận vào BM07. |
| **Lỗi Kịch bản (Test Script Bug / Flaky)** | Kiểm thử thất bại do mã test (sai locator, thiếu Explicit Wait, server cục bộ chưa chạy, dữ liệu CSV sai cấu trúc). | **Yêu cầu tác giả sửa code test**, gắn nhãn `test-script`, **KHÔNG tạo Bug Issue** để tránh làm sai lệch số liệu chất lượng sản phẩm. |

---

## 2. BẢNG TỔNG HỢP & SÀNG LỌC NGUYÊN NHÂN LỖI (RUN-01 VS RUN-02)

| Mã lỗi | Tên lỗi phát hiện | Module | Test Case liên quan | Kết quả RUN-01 | Phân loại ISTQB CTFL 5.5 | Trạng thái sau RUN-02 |
|:---:|---|:---:|:---:|:---:|:---:|:---:|
| **BUG-01** | Xung đột gộp danh sách tài khoản trong AppContext gây lỗi đăng nhập tài khoản Ứng viên (Offline Fallback) | M1 / M4 | `TC-LOGIN-001`<br>`TC-APP-005` | **FAIL** | **SUT Bug** (Mã nguồn AppContext) | ✅ **CLOSED** (Re-test PASS) |
| **BUG-02** | Mất trạng thái phiên đăng nhập của ứng viên khi truy cập URL trực tiếp hoặc F5 (Thiếu đồng bộ localStorage) | M4 / M1 | `TC-APP-007`<br>`TC-APP-014` | **FAIL** | **SUT Bug** (Mã nguồn AppContext) | ✅ **CLOSED** (Re-test PASS) |
| **BUG-03** | Đăng nhập thành công ở chế độ Offline Fallback không tự động chuyển hướng màn hình (Stuck at Login) | M1 / M4 | `TC-LOGIN-001`<br>`TC-APP-E2E` | **FAIL** | **SUT Bug** (Mã nguồn LoginPage.jsx) | ✅ **CLOSED** (Re-test PASS) |
| **BUG-04** | Khách vãng lai bấm "Ứng tuyển ngay" chuyển hướng sang Login nhưng mất ngữ cảnh tin tuyển dụng | M4 | `TC-APP-001` | **FAIL** | **SUT Bug** (UI/UX Routing) | ⏳ **OPEN** (Đã ghi nhận Issue) |
| **BUG-05** | Form Đăng ký tài khoản (Ứng viên & Doanh nghiệp) văng lỗi "Failed to fetch" khi backend ngắt kết nối | M2 | `TC_REG_001` $\to$ `006`<br>`SmokeLicense` | **FAIL** (6 TC) | **SUT Bug** (Thiếu Fallback / Error Handling) | ⏳ **OPEN** (Đã ghi nhận Issue) |
| **SCR-01** | Lỗi WebDriver `net::ERR_CONNECTION_REFUSED` do chưa khởi động Vite dev server | Framework | Toàn bộ Test Suite | **FAIL** | **Test Script / Environment** | 🛠️ Đã sửa quy trình (Bật `npm run dev`) |
| **SCR-02** | Lỗi Flaky Timeout khi chờ thông báo Toast biến mất do thiếu Explicit Wait | M4 | `TC-APP-005` | **FAIL** | **Test Script Bug** | 🛠️ Đã sửa code test (`waitVisible`) |
| **SCR-03** | NullPointerException trong `SampleFrameworkTest` khi mock ExtensionContext trả về store null | Framework | `SampleFrameworkTest` | **ERROR** (1 TC) | **Test Script Bug** | 🛠️ Đã sửa null check (`BUILD SUCCESS`) |

---

## 3. DANH SÁCH DEFECT REPORT CHI TIẾT (SUT BUGS)

### 📌 DEFECT ID: `BUG-01`
* **Tiêu đề GitHub Issue:** `[BUG] Xung đột danh sách tài khoản trong AppContext gây lỗi không thể đăng nhập tài khoản Ứng viên (Offline Fallback)`
* **Module:** `M1 - Đăng nhập` & `M4 - Ứng tuyển việc làm`
* **Quy tắc nghiệp vụ liên quan:** `RULE-LOGIN-01`, `RULE-APP-01`
* **Test Case phát hiện:** `TC-LOGIN-001`, `TC-APP-005`
* **Severity:** `severity:major` | **Priority:** `priority:high`
* **Người phát hiện:** Lã Trọng Huy (`TrongHuy317`) | **Ngày phát hiện:** 01/11/2026
* **Môi trường kiểm thử:** Google Chrome (Headless & UI), Windows 11, URL: `http://localhost:5173/login`
* **Mô tả các bước tái hiện (Steps to Reproduce):**
  1. Mở trang đăng nhập: `http://localhost:5173/login`.
  2. Nhập Email: `candidate@gmail.com`.
  3. Nhập Mật khẩu: `Candidate@123`.
  4. Bấm nút "Đăng nhập".
* **Expected Result (Kỳ vọng):** Đăng nhập thành công và điều hướng sang trang ứng viên.
* **Actual Result (Thực tế):** Hệ thống báo lỗi: *"Vui lòng lựa chọn loại tài khoản/email..."*, chặn hoàn toàn việc đăng nhập của tài khoản ứng viên mẫu.
* **Phân tích nguyên nhân gốc rễ (Root Cause Analysis):**  
  Trong `frontend/src/context/AppContext.jsx`, biến danh sách người dùng được gộp bằng cú pháp:  
  `const allUsers = [...usersDB, ...INITIAL_USERS_DB];`  
  Do tài khoản `candidate@gmail.com` vừa có trong `INITIAL_USERS_DB` vừa có trong `usersDB`, mảng `allUsers` bị trùng lặp 2 bản ghi cho cùng 1 email. Hàm tìm kiếm `candidateList` trả về độ dài $> 1$, kích hoạt điều kiện cảnh báo trùng lặp tài khoản.
* **Giải pháp khắc phục:** Sử dụng cấu trúc `Map` để deduplicate danh sách người dùng theo `(u.id || u.email).toLowerCase()` trước khi lọc.
* **Kết quả kiểm thử lại (RUN-02 Re-testing):** **PASS** (100% các kịch bản đăng nhập ứng viên chạy thành công).
* **Bằng chứng kiểm thử (Evidence):** Ảnh chụp màn hình lỗi lưu tại `docs/reports/test_fail_center.png`.
* **Trạng thái:** **CLOSED**

---

### 📌 DEFECT ID: `BUG-02`
* **Tiêu đề GitHub Issue:** `[BUG] Mất trạng thái phiên đăng nhập của ứng viên khi truy cập URL trực tiếp hoặc làm mới trang (F5)`
* **Module:** `M4 - Ứng tuyển & Theo dõi đơn trên Kanban`
* **Quy tắc nghiệp vụ liên quan:** `RULE-APP-06`
* **Test Case phát hiện:** `TC-APP-007`, `TC-APP-014`
* **Severity:** `severity:major` | **Priority:** `priority:high`
* **Người phát hiện:** Lã Trọng Huy (`TrongHuy317`) | **Ngày phát hiện:** 01/11/2026
* **Môi trường kiểm thử:** Google Chrome, URL: `http://localhost:5173/candidate/applications`
* **Mô tả các bước tái hiện (Steps to Reproduce):**
  1. Đăng nhập tài khoản Candidate hợp lệ.
  2. Điều hướng trực tiếp thanh địa chỉ URL trình duyệt sang `http://localhost:5173/candidate/applications` (hoặc nhấn F5 làm mới trang).
* **Expected Result (Kỳ vọng):** Phiên đăng nhập được duy trì và hiển thị bảng theo dõi hồ sơ ứng tuyển dạng Kanban.
* **Actual Result (Thực tế):** Trình duyệt bị văng về trang chủ hoặc trang đăng nhập, thông tin phiên làm việc biến mất.
* **Phân tích nguyên nhân gốc rễ (Root Cause Analysis):**  
  Trong `frontend/src/context/AppContext.jsx`, hàm `setCurrentUser` chỉ cập nhật biến trạng thái React (`useState`, `userRef`) mà không thực hiện đồng bộ `localStorage.setItem('careerfit_current_user', JSON.stringify(next))`. Khi trình duyệt reload hoặc mở trang mới, state React bị reset về `null`.
* **Giải pháp khắc phục:** Bổ sung cơ chế ghi và xóa `careerfit_current_user` trong `localStorage` mỗi khi `setCurrentUser` được kích hoạt.
* **Kết quả kiểm thử lại (RUN-02 Re-testing):** **PASS** (Kịch bản điều hướng trực tiếp giữa các trang trong `ApplicationTest` hoạt động trơn tru).
* **Bằng chứng kiểm thử (Evidence):** Ảnh chụp màn hình lưu tại `docs/reports/m4_test_result.png`.
* **Trạng thái:** **CLOSED**

---

### 📌 DEFECT ID: `BUG-03`
* **Tiêu đề GitHub Issue:** `[BUG] Đăng nhập thành công ở chế độ Offline Fallback không tự động chuyển hướng màn hình (Stuck at Login)`
* **Module:** `M1 - Đăng nhập` / `M4 - Ứng tuyển`
* **Quy tắc nghiệp vụ liên quan:** `RULE-LOGIN-01`, `RULE-APP-01`
* **Test Case phát hiện:** `TC-LOGIN-001`, `TC-APP-E2E`
* **Severity:** `severity:major` | **Priority:** `priority:high`
* **Người phát hiện:** Lã Trọng Huy (`TrongHuy317`) | **Ngày phát hiện:** 01/11/2026
* **Môi trường kiểm thử:** Google Chrome, URL: `http://localhost:5173/login`
* **Mô tả các bước tái hiện (Steps to Reproduce):**
  1. Ngắt kết nối backend Spring Boot (chạy ở chế độ local fallback).
  2. Đăng nhập tài khoản Candidate với thông tin chính xác.
  3. Bấm "Đăng nhập".
* **Expected Result (Kỳ vọng):** Hiển thị thông báo "Đăng nhập thành công!" và tự động điều hướng sang Dashboard.
* **Actual Result (Thực tế):** Có Toast thông báo thành công nhưng màn hình vẫn đứng im tại form `/login`, URL không thay đổi.
* **Phân tích nguyên nhân gốc rễ (Root Cause Analysis):**  
  Trong `frontend/src/components/auth/LoginPage.jsx`, tại khối xử lý lỗi `catch (err)` của chế độ fallback:  
  `if (fallback && fallback.success) { showToast('Đăng nhập thành công!', 'success'); }`  
  Khối này thiếu lệnh gọi `setActivePage(targetPage)` để cập nhật màn hình tương ứng với vai trò của user.
* **Giải pháp khắc phục:** Bổ sung việc xác định `targetPage` theo `user.role` và gọi hàm `setActivePage(targetPage)`.
* **Kết quả kiểm thử lại (RUN-02 Re-testing):** **PASS** (Kịch bản kiểm tra `waitUntilRedirectedFromLogin` vượt qua kiểm tra).
* **Trạng thái:** **CLOSED**

---

### 📌 DEFECT ID: `BUG-04`
* **Tiêu đề GitHub Issue:** `[BUG] Khách vãng lai bấm "Ứng tuyển ngay" chuyển hướng sang Login nhưng mất ngữ cảnh tin tuyển dụng`
* **Module:** `M4 - Chi tiết việc làm & Ứng tuyển`
* **Quy tắc nghiệp vụ liên quan:** `RULE-APP-01`
* **Test Case phát hiện:** `TC-APP-001`
* **Severity:** `severity:minor` | **Priority:** `priority:medium`
* **Người phát hiện:** Lã Trọng Huy (`TrongHuy317`) | **Ngày phát hiện:** 01/11/2026
* **Môi trường kiểm thử:** Google Chrome, URL: `http://localhost:5173/jobs/job-01`
* **Mô tả các bước tái hiện (Steps to Reproduce):**
  1. Chưa đăng nhập (Guest), truy cập vào trang chi tiết một công việc: `/jobs/job-01`.
  2. Bấm nút "Ứng tuyển ngay".
  3. Hệ thống chuyển sang màn hình `/login`. Tiến hành đăng nhập thành công.
* **Expected Result (Kỳ vọng):** Sau khi đăng nhập, hệ thống tự động đưa ứng viên quay lại trang `/jobs/job-01` để tiếp tục nộp đơn (Deep-link state preservation).
* **Actual Result (Thực tế):** Hệ thống đưa ứng viên về `candidate-dashboard`, ứng viên phải tự tìm lại công việc vừa xem.
* **Phân tích nguyên nhân gốc rễ (Root Cause Analysis):** Lệnh chuyển hướng của nút "Ứng tuyển ngay" không gửi kèm tham số truy vấn (Query Parameter) dạng `?redirect=/jobs/job-01`.
* **Trạng thái:** **OPEN** (Đã tạo Defect Issue và đề xuất đưa vào backlog cải tiến).

---

### 📌 DEFECT ID: `BUG-05`
* **Tiêu đề GitHub Issue:** `[BUG] Form Đăng ký tài khoản (Ứng viên & Doanh nghiệp) hiển thị lỗi thô "Failed to fetch" khi mất kết nối backend`
* **Module:** `M2 - Đăng ký tài khoản`
* **Quy tắc nghiệp vụ liên quan:** `RULE-REG-01`, `RULE-REG-02`
* **Test Case phát hiện:** `TC_REG_001`, `TC_REG_002`, `TC_REG_004`, `TC_REG_005`, `TC_REG_006`, `testEmployerRegistrationWithLicenseUploadSmoke`
* **Severity:** `severity:major` | **Priority:** `priority:high`
* **Người phát hiện:** Lã Trọng Huy (`TrongHuy317`) | **Ngày phát hiện:** 01/11/2026 (Trong đợt chạy RUN-01)
* **Môi trường kiểm thử:** Google Chrome, URL: `http://localhost:5173/login` (Tab Đăng ký)
* **Mô tả các bước tái hiện (Steps to Reproduce):**
  1. Mở trang đăng ký tài khoản.
  2. Nhập đầy đủ thông tin hợp lệ (Họ tên, email mới, mật khẩu hợp lệ, số điện thoại).
  3. Bấm "Đăng ký tài khoản".
* **Expected Result (Kỳ vọng):** Đăng ký thành công hoặc hiển thị thông báo lỗi mạng thân thiện với người dùng.
* **Actual Result (Thực tế):** Giao diện văng thông báo lỗi kỹ thuật: *"Failed to fetch"*, ngăn chặn toàn bộ người dùng mới tham gia nền tảng khi backend port 8080 chưa sẵn sàng.
* **Phân tích nguyên nhân gốc rễ (Root Cause Analysis):**  
  Trong `frontend/src/components/auth/LoginPage.jsx`, hàm `handleManualRegister` gửi `fetch('/api/v1/auth/register')` nhưng khối `catch (err)` không có cơ chế fallback lưu trữ cục bộ (`localStorage` / `usersDB`) như luồng Đăng nhập, đồng thời không chuyển đổi ngoại lệ mạng thành thông báo tiếng Việt rõ nghĩa.
* **Bằng chứng kiểm thử (Evidence):** Ảnh chụp màn hình lỗi lưu tại `docs/reports/RegisterTest_*.png`.
* **Trạng thái:** **OPEN** (Đã phân loại và chuyển giao cho Framework Owner Võ Nguyên Khánh xử lý).

---

## 4. BẢNG XỬ LÝ LỖI KỊCH BẢN KIỂM THỬ (TEST SCRIPT ISSUES - KHÔNG TẠO BUG REPORT SUT)

Tuân thủ nghiêm ngặt chuẩn **ISTQB CTFL 5.5**, các lỗi phát sinh do môi trường hoặc mã kịch bản kiểm thử không được tính là lỗi của phần mềm:

| Script Issue ID | Tên sự cố kịch bản | Nguyên nhân chi tiết | Cách khắc phục | Nhãn gắn trên Git |
|:---:|---|---|---|:---:|
| `SCR-01` | `net::ERR_CONNECTION_REFUSED` | Kịch bản tự động thực thi khi server Frontend Vite (port 5173) chưa được khởi động trước. | Khởi chạy `npm run dev` trong terminal riêng trước khi chạy `mvn clean test`. | `test-script` |
| `SCR-02` | `NoSuchElementException` hoặc `TimeoutException` khi kiểm tra Toast | Thời gian hiển thị Toast animation quá nhanh hoặc có độ trễ kết xuất, test script dùng kiểm tra tức thì dẫn đến Flaky Test. | Chuyển toàn bộ kiểm tra sang **Explicit Wait** (`waitVisible`, `waitClickable`), loại bỏ 100% `Thread.sleep()`. | `test-script` |
| `SCR-03` | NullPointerException trong `SampleFrameworkTest` | Hàm unit test `testScreenshotOnFailureGeneration` sử dụng Mockito mock `ExtensionContext` nhưng chưa mock `getStore()`, trả về null. | Bổ sung kiểm tra null safety trong `ScreenshotOnFailure.java` (`try { var store = context.getStore(...); if (store != null) ... }`). | `test-script` |

---

## 5. MA TRẬN TRUY VẾT DEFECT (DEFECT TRACEABILITY MATRIX)

| Defect ID | Module | Quy tắc BM02 | Test Case BM03 | Kịch bản Java thực thi | Severity | Priority | Trạng thái |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| `BUG-01` | M1 / M4 | `RULE-LOGIN-01`<br>`RULE-APP-01` | `TC-LOGIN-001`<br>`TC-APP-005` | `ApplicationTest#testApplicationFullStateTransitionFlow` | Major | High | **CLOSED** |
| `BUG-02` | M4 | `RULE-APP-06` | `TC-APP-007`<br>`TC-APP-014` | `ApplicationTest#testKanbanViewSwitchingAndSearch` | Major | High | **CLOSED** |
| `BUG-03` | M1 / M4 | `RULE-LOGIN-01` | `TC-LOGIN-001` | `LoginTest#testLoginDataDriven` | Major | High | **CLOSED** |
| `BUG-04` | M4 | `RULE-APP-01` | `TC-APP-001` | `ApplicationTest#testApplyDataDriven` | Minor | Medium | **OPEN** |
| `BUG-05` | M2 | `RULE-REG-01`<br>`RULE-REG-02` | `TC_REG_001` $\to$ `006` | `RegisterTest#testRegisterDataDriven` | Major | High | **OPEN** |

---

## 6. THỐNG KÊ & ĐÁNH GIÁ CHẤT LƯỢNG (METRICS & QUALITY ASSESSMENT)

### 6.1. Phân bổ Defect theo Mức độ nghiêm trọng (Severity)
* `Critical`: 0 (0%) — Hệ thống không có lỗi nghiêm trọng gây sập (crash) hoặc rò rỉ dữ liệu.
* `Major`: 4 (80%) — Các lỗi logic xác thực, đăng ký và đồng bộ phiên làm việc.
* `Minor`: 1 (20%) — Lỗi trải nghiệm chuyển hướng người dùng chưa tối ưu.
* `Trivial`: 0 (0%).
* **Tổng số lỗi SUT:** **5 Defects**.

### 6.2. Phân bổ Defect theo Mức độ ưu tiên (Priority)
* `High`: 4 (80%) — Đã được xử lý 3 lỗi, 1 lỗi M2 đang mở.
* `Medium`: 1 (20%) — Ghi nhận backlog.
* `Low`: 0 (0%).

### 6.3. Hiệu quả khắc phục lỗi (Fix Rate & Re-test)
* **Số lỗi đã sửa và đóng (Closed):** 3 / 5 (**60%**).
* **Tỷ lệ Re-test thành công trong RUN-02 cho Module M4:** **100% (9/9 tests của ApplicationTest đều PASS)**.
* **Kết luận nghiệm thu của Defect Manager:**  
  Sau đợt chạy RUN-02, toàn bộ các lỗi mức độ `Major` của Module M4 đã được xử lý triệt để. Báo cáo Surefire HTML và ảnh chụp lỗi đã được lưu trữ đầy đủ làm bằng chứng nghiệm thu tại `docs/reports/`.

---

*Báo cáo được lập bởi **Lã Trọng Huy (Defect Manager - SV4)** và lưu trữ chính thức tại kho mã nguồn dự án: `docs/BM07_DefectReport.md`.*
