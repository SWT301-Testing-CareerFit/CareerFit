# BM07 – DEFECT REPORT (BÁO CÁO QUẢN LÝ LỖI & SỰ CỐ KIỂM THỬ)

**Dự án:** CareerFit – Hệ thống Kết nối Tuyển dụng Thông minh dành cho Sinh viên & Fresher  
**Môn học:** SWT301 – Software Testing (Đại học FPT - Fall 2026)  
**Chuẩn tham chiếu:** ISTQB CTFL v4.0 (Chương 5.5 Quản lý lỗi) & ISO/IEC/IEEE 29119-3  
**Người phụ trách (Defect Manager):** Lã Trọng Huy (`TrongHuy317` - SV4)  
**Phê duyệt (Test Lead):** Nguyễn Lâm Thi (`nguyenlamthi` - SV1)  
**Phiên bản:** 1.0 | **Ngày lập báo cáo:** 01/11/2026  
**Đợt chạy kiểm thử:** RUN-01 (Phát hiện & Ghi nhận) $\to$ RUN-02 (Khắc phục & Kiểm thử hồi quy)

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

### 1.4. Định nghĩa Mức độ nghiêm trọng (Severity) & Mức độ ưu tiên (Priority)

* **Severity (Mức độ nghiêm trọng đối với hệ thống):**
  * `Critical`: Làm sập hệ thống (Crash), hỏng cơ sở dữ liệu, chặn đứng luồng chính không thể tiếp tục kiểm thử.
  * `Major`: Sai lệch nghiệp vụ nghiêm trọng, chặn một tính năng chính nhưng có thể có giải pháp tạm thời.
  * `Minor`: Sai thông báo hiển thị, sai validation nhẹ, không chặn luồng nghiệp vụ.
  * `Trivial`: Lỗi giao diện nhỏ, lệch màu sắc hoặc lỗi chính tả.
* **Priority (Mức độ ưu tiên xử lý):**
  * `High`: Phải khắc phục ngay lập tức trước khi chạy đợt kiểm thử tiếp theo.
  * `Medium`: Khắc phục trong sprint/đợt kiểm thử hiện tại.
  * `Low`: Khắc phục khi có thời gian rảnh hoặc chuyển sang giai đoạn sau.

---

## 2. BẢNG TỔNG HỢP & SÀNG LỌC NGUYÊN NHÂN LỖI (RUN-01 VS RUN-02)

| Mã lỗi | Tên lỗi phát hiện | Module | Test Case liên quan | Kết quả RUN-01 | Phân loại ISTQB CTFL 5.5 | Trạng thái sau RUN-02 |
|:---:|---|:---:|:---:|:---:|:---:|:---:|
| **BUG-01** | Xung đột gộp danh sách tài khoản trong AppContext gây lỗi đăng nhập tài khoản Ứng viên (Offline Fallback) | M1 / M4 | `TC-LOGIN-001`<br>`TC-APP-005` | **FAIL** | **SUT Bug** (Mã nguồn AppContext) | ✅ **CLOSED** (Re-test PASS) |
| **BUG-02** | Mất trạng thái phiên đăng nhập của ứng viên khi truy cập URL trực tiếp hoặc F5 (Thiếu đồng bộ localStorage) | M4 / M1 | `TC-APP-007`<br>`TC-APP-014` | **FAIL** | **SUT Bug** (Mã nguồn AppContext) | ✅ **CLOSED** (Re-test PASS) |
| **BUG-03** | Đăng nhập thành công ở chế độ Offline Fallback không tự động chuyển hướng màn hình (Stuck at Login) | M1 / M4 | `TC-LOGIN-001`<br>`TC-APP-E2E` | **FAIL** | **SUT Bug** (Mã nguồn LoginPage.jsx) | ✅ **CLOSED** (Re-test PASS) |
| **BUG-04** | Khách vãng lai bấm "Ứng tuyển ngay" chuyển hướng sang Login nhưng mất ngữ cảnh tin tuyển dụng | M4 | `TC-APP-001` | **FAIL** | **SUT Bug** (UI/UX Routing) | ⏳ **OPEN** (Đã ghi nhận Issue) |
| **SCR-01** | Lỗi WebDriver `net::ERR_CONNECTION_REFUSED` do chưa khởi động Vite dev server | Framework | Toàn bộ Test Suite | **FAIL** | **Test Script / Environment** | 🛠️ Đã sửa quy trình (Bật `npm run dev`) |
| **SCR-02** | Lỗi Flaky Timeout khi chờ thông báo Toast biến mất do thiếu Explicit Wait | M4 | `TC-APP-005` | **FAIL** | **Test Script Bug** | 🛠️ Đã sửa code test (`waitVisible`) |

---

## 3. DANH SÁCH DEFECT REPORT CHI TIẾT (SUT BUGS)

### 📌 DEFECT ID: `BUG-01`
* **Tiêu đề GitHub Issue:** `[BUG] Xung đột danh sách tài khoản trong AppContext gây lỗi không thể đăng nhập tài khoản Ứng viên (Offline Fallback) #24`
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
* **Bằng chứng kiểm thử (Evidence):** Ảnh chụp màn hình lỗi lưu tại `docs/test_fail_center.png`.
* **Trạng thái:** **CLOSED**

---

### 📌 DEFECT ID: `BUG-02`
* **Tiêu đề GitHub Issue:** `[BUG] Mất trạng thái phiên đăng nhập của ứng viên khi truy cập URL trực tiếp hoặc làm mới trang (F5) #25`
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
* **Trạng thái:** **CLOSED**

---

### 📌 DEFECT ID: `BUG-03`
* **Tiêu đề GitHub Issue:** `[BUG] Đăng nhập thành công ở chế độ Offline Fallback không tự động chuyển hướng màn hình #26`
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
* **Tiêu đề GitHub Issue:** `[BUG] Khách vãng lai bấm "Ứng tuyển ngay" chuyển hướng sang Login nhưng mất ngữ cảnh tin tuyển dụng #27`
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
* **Trạng thái:** **OPEN** (Đã tạo Defect Issue và đề xuất đưa vào backlog cải tiến phiên bản tiếp theo).

---

## 4. BẢNG XỬ LÝ LỖI KỊCH BẢN KIỂM THỬ (TEST SCRIPT ISSUES - KHÔNG TẠO BUG REPORT SUT)

Tuân thủ nghiêm ngặt chuẩn **ISTQB CTFL 5.5**, các lỗi phát sinh do môi trường hoặc mã kịch bản kiểm thử không được tính là lỗi của phần mềm:

| Script Issue ID | Tên sự cố kịch bản | Nguyên nhân chi tiết | Cách khắc phục | Nhãn gắn trên Git |
|:---:|---|---|---|:---:|
| `SCR-01` | `net::ERR_CONNECTION_REFUSED` | Kịch bản tự động thực thi khi server Frontend Vite (port 5173) chưa được khởi động trước. | Khởi chạy `npm run dev` trong terminal riêng trước khi chạy `mvn clean test`. | `test-script` |
| `SCR-02` | `NoSuchElementException` hoặc `TimeoutException` khi kiểm tra Toast | Thời gian hiển thị Toast animation quá nhanh hoặc có độ trễ kết xuất, test script dùng kiểm tra tức thì dẫn đến Flaky Test. | Chuyển toàn bộ kiểm tra sang **Explicit Wait** (`waitVisible`, `waitClickable`), loại bỏ 100% `Thread.sleep()`. | `test-script` |

---

## 5. MA TRẬN TRUY VẾT DEFECT (DEFECT TRACEABILITY MATRIX)

| Defect ID | GitHub Issue ID | Module | Quy tắc BM02 | Test Case BM03 | Kịch bản Java thực thi | Severity | Priority | Trạng thái |
|:---:|:---:|:---:|:---:|:---:|---|:---:|:---:|:---:|
| `BUG-01` | `#24` | M1 / M4 | `RULE-LOGIN-01`<br>`RULE-APP-01` | `TC-LOGIN-001`<br>`TC-APP-005` | `ApplicationTest#testApplicationFullStateTransitionFlow` | Major | High | **CLOSED** |
| `BUG-02` | `#25` | M4 | `RULE-APP-06` | `TC-APP-007`<br>`TC-APP-014` | `ApplicationTest#testKanbanViewSwitchingAndSearch` | Major | High | **CLOSED** |
| `BUG-03` | `#26` | M1 / M4 | `RULE-LOGIN-01` | `TC-LOGIN-001` | `LoginTest#testLoginDataDriven` | Major | High | **CLOSED** |
| `BUG-04` | `#27` | M4 | `RULE-APP-01` | `TC-APP-001` | `ApplicationTest#testApplyDataDriven` | Minor | Medium | **OPEN** |

---

## 6. THỐNG KÊ & ĐÁNH GIÁ CHẤT LƯỢNG (METRICS & QUALITY ASSESSMENT)

### 6.1. Phân bổ Defect theo Mức độ nghiêm trọng (Severity)
* `Critical`: 0 (0%) — Hệ thống không có lỗi nghiêm trọng gây sập (crash) hoặc rò rỉ dữ liệu.
* `Major`: 3 (75%) — Các lỗi logic xác thực và đồng bộ phiên làm việc.
* `Minor`: 1 (25%) — Lỗi trải nghiệm chuyển hướng người dùng chưa tối ưu.
* `Trivial`: 0 (0%).
* **Tổng số lỗi SUT:** **4 Defects**.

### 6.2. Phân bổ Defect theo Mức độ ưu tiên (Priority)
* `High`: 3 (75%) — Đã được xử lý ngay lập tức để phục vụ kiểm thử hồi quy.
* `Medium`: 1 (25%) — Ghi nhận backlog.
* `Low`: 0 (0%).

### 6.3. Hiệu quả khắc phục lỗi (Fix Rate & Re-test)
* **Số lỗi đã sửa và đóng (Closed):** 3 / 4 (**75%**).
* **Tỷ lệ Re-test thành công trong RUN-02:** **100% (3/3 bugs được sửa đều PASS trong lần chạy thứ 2)**.
* **Kết luận nghiệm thu của Defect Manager:**  
  Sau đợt chạy RUN-02, toàn bộ các lỗi mức độ `Major` chặn luồng nộp đơn ứng tuyển và đăng nhập đã được xử lý triệt để. Hệ thống đạt tiêu chí sẵn sàng nghiệm thu (Exit Criteria).

---

*Báo cáo được lập bởi **Lã Trọng Huy (Defect Manager - SV4)** và lưu trữ chính thức tại kho mã nguồn dự án: `docs/BM07_DefectReport.md`.*
