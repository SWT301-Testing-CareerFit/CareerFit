# BM02 – TÀI LIỆU BUSINESS RULES & TEST CONDITIONS (ĐẶC TẢ NGHIỆP VỤ & ĐIỀU KIỆN KIỂM THỬ)

**Dự án:** CareerFit – Nền tảng Tuyển dụng IT & Định hướng Kỹ năng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Phiên bản:** 2.1 (Cập nhật chuyên sâu Module M3: `RULE-SEARCH-01` đến `RULE-SEARCH-07` theo Issue #5)  
**Ngày cập nhật:** 01/11/2026  
**Chịu trách nhiệm tài liệu:** Nguyễn Trọng Tiến (`tiendeptroai` - Documentation Owner) & Nguyễn Lâm Thi (`nguyenlamthi` - Test Lead)  

---

## 1. TỔNG QUAN & PHƯƠNG PHÁP XÂY DỰNG

Tài liệu này xác định toàn bộ các quy tắc nghiệp vụ (**Business Rules - BR**) và các điều kiện kiểm thử (**Test Conditions - TCOND**) của hệ thống CareerFit, được suy biến trực tiếp từ tài liệu đặc tả yêu cầu nghiệp vụ (SRS).

Mọi điều kiện kiểm thử đều được phân tích dựa trên các kỹ thuật kiểm thử hộp đen chuẩn **ISTQB CTFL v4.0**:
* **EP (Equivalence Partitioning):** Phân vùng tương đương (phân tách dữ liệu hợp lệ và không hợp lệ).
* **BVA (Boundary Value Analysis):** Phân tích giá trị biên (kiểm tra độ dài ký tự, mức lương).
* **DT (Decision Table):** Bảng quyết định (tổ hợp logic đa điều kiện).
* **ST (State Transition):** Kiểm thử chuyển trạng thái hệ thống.
* **EG (Error Guessing):** Đoán lỗi ngoại lệ, an toàn dữ liệu (XSS, SQL Injection).

---

## 2. MODULE 1 (M1): ĐĂNG NHẬP & PHÂN QUYỀN (LOGIN & AUTHORIZATION)
* **Người phụ trách:** Nguyễn Lâm Thi (`nguyenlamthi`)
* **Endpoint / Giao diện kiểm thử:** `/login`, `/jobs`, `/employer/dashboard`, `/admin/dashboard`
* **Testable UI Scope:** Kiểm thử form đăng nhập nội bộ (Email + Password), phân quyền sau đăng nhập theo vai trò (Role), xử lý lỗi và đăng xuất.

### 2.1. Bảng Business Rules (M1)

| BR ID | Mô tả Quy tắc nghiệp vụ (Business Rule) | Test Condition ID | Kỹ thuật kiểm thử | Automation? | Ghi chú |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **RULE-LOGIN-01** | Đăng nhập thành công với thông tin hợp lệ (Email & Password đúng, tài khoản đang Active). Hệ thống cấp phiên làm việc (Session/Token) và điều hướng đúng giao diện tương ứng với Role của người dùng. | `TCOND-LOGIN-01`, `TCOND-LOGIN-02`, `TCOND-LOGIN-03` | Decision Table | ✅ Yes | - CANDIDATE -> `/jobs`<br>- EMPLOYER -> `/employer`<br>- ADMIN -> `/admin` |
| **RULE-LOGIN-02** | Đăng nhập thất bại khi thông tin không khớp (Sai Email hoặc Sai Mật khẩu). Hệ thống hiển thị thông báo lỗi chung *"Email hoặc mật khẩu không chính xác"*, **tuyệt đối không tiết lộ** trường nào bị sai để đảm bảo an toàn bảo mật, người dùng vẫn ở lại trang `/login`. | `TCOND-LOGIN-04`, `TCOND-LOGIN-05` | Decision Table / Error Guessing | ✅ Yes | Bảo mật chống Enumeration Attack |
| **RULE-LOGIN-03** | Ràng buộc bắt buộc (Required Fields) và Định dạng (Format): Email và Password là 2 trường bắt buộc. Nếu để trống hoặc sai định dạng email, hệ thống chặn submit và hiển thị validation message tương ứng. | `TCOND-LOGIN-06`, `TCOND-LOGIN-07`, `TCOND-LOGIN-08`, `TCOND-LOGIN-09` | Equivalence Partitioning | ✅ Yes | Validation client-side & form attribute |
| **RULE-LOGIN-04** | Mật khẩu có phân biệt chữ hoa và chữ thường (Case-sensitivity). Chuỗi mật khẩu nhập vào phải khớp chính xác từng ký tự hoa/thường với mật khẩu đã lưu. | `TCOND-LOGIN-10` | Equivalence Partitioning | ✅ Yes | Đảm bảo tính toàn vẹn hash password |
| **RULE-LOGIN-05** | Tài khoản bị khóa (LOCKED) hoặc chưa kích hoạt (INACTIVE): Dù nhập đúng email và password, hệ thống vẫn từ chối đăng nhập và hiển thị thông báo trạng thái tài khoản bị khóa/chưa kích hoạt. | `TCOND-LOGIN-11` | Decision Table / State Transition | ✅ Yes | Kiểm tra cờ trạng thái tài khoản |
| **RULE-LOGIN-06** | Đăng xuất (Logout) và Hủy phiên làm việc: Khi người dùng bấm Đăng xuất, hệ thống hủy bỏ Session/Token lưu trữ, chuyển hướng về trang công khai và xóa thông tin phiên khỏi Navbar. | `TCOND-LOGIN-12` | State Transition | ✅ Yes | Kiểm tra dọn dẹp state và localStorage |

---

## 3. MODULE 2 (M2): ĐĂNG KÝ TÀI KHOẢN (USER REGISTRATION)
* **Người phụ trách:** Võ Nguyên Khánh (`richnk123a-cyber`)
* **Endpoint / Giao diện kiểm thử:** `/login` (Tab "Đăng ký"), Form đăng ký Ứng viên (Candidate) và Nhà tuyển dụng (Employer).

### 3.1. Bảng Business Rules (M2)

| BR ID | Mô tả Quy tắc nghiệp vụ (Business Rule) | Test Condition ID | Kỹ thuật kiểm thử | Automation? | Ghi chú |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **RULE-REG-01** | Đăng ký tài khoản Ứng viên (CANDIDATE) thành công: Người dùng nhập đầy đủ thông tin hợp lệ (Họ tên, Email chưa đăng ký, Mật khẩu đạt chuẩn, Mật khẩu xác nhận khớp). Tài khoản được tạo với `role = CANDIDATE`, `status = ACTIVE`. | `TCOND-REG-01`, `TCOND-REG-02` | Equivalence Partitioning / Decision Table | ✅ Yes | Dữ liệu email được sinh ngẫu nhiên qua `DataGenerator` |
| **RULE-REG-02** | Đăng ký tài khoản Doanh nghiệp (EMPLOYER) kèm Giấy phép kinh doanh: Doanh nghiệp đăng ký cần nhập thông tin công ty và tải file Giấy phép kinh doanh (PDF/Ảnh). Mặc định tài khoản có trạng thái xác minh `verification_status = UNVERIFIED` (`BR-01`). | `TCOND-REG-03` | Decision Table / State Transition | ✅ Yes | Tuân thủ luật nghiệp vụ `BR-01` |
| **RULE-REG-03** | Ràng buộc tính duy nhất của Email (Unique Constraint): Mỗi email chỉ được đăng ký một tài khoản duy nhất trong toàn hệ thống. Nếu nhập email đã tồn tại trong database, hệ thống chặn submit và báo lỗi *"Email đã được sử dụng"*. | `TCOND-REG-04` | Equivalence Partitioning / Error Guessing | ✅ Yes | Ngăn chặn trùng lặp tài khoản |
| **RULE-REG-04** | Ràng buộc độ phức tạp của Mật khẩu & Đối khớp mật khẩu: Mật khẩu tối thiểu 8 ký tự, phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt. Mật khẩu xác nhận phải khớp 100%. | `TCOND-REG-05`, `TCOND-REG-06`, `TCOND-REG-07`, `TCOND-REG-08` | Boundary Value Analysis / Equivalence Partitioning | ✅ Yes | BVA tại biên 7, 8 ký tự |
| **RULE-REG-05** | Ràng buộc trường bắt buộc và định dạng đầu vào: Tất cả các trường đánh dấu `*` không được để trống hoặc chỉ chứa khoảng trắng. Email phải đúng định dạng chuẩn RFC. | `TCOND-REG-09`, `TCOND-REG-10`, `TCOND-REG-11` | Equivalence Partitioning / BVA | ✅ Yes | Kiểm tra client-side validation |

---

## 4. MODULE 3 (M3): TÌM KIẾM & LỌC VIỆC LÀM (JOB SEARCH & FILTER)
* **Người phụ trách:** Nguyễn Trọng Tiến (`tiendeptroai` - Documentation Owner)
* **Endpoint / Giao diện kiểm thử:** `/jobs`, Thanh tìm kiếm từ khóa, Bộ lọc đa tiêu chí (Ngành nghề, Địa điểm, Mức lương, Loại hình công việc).
* **Testable UI Scope:** Kiểm tra chức năng tìm kiếm toàn văn theo từ khóa, cơ chế lọc kết hợp đa điều kiện (AND logic), giao diện trạng thái trống khi không có kết quả (Empty State), nút đặt lại bộ lọc, và kiểm tra an toàn dữ liệu đầu vào (XSS & SQLi).

### 4.1. Bảng Business Rules (M3) – Tuân thủ yêu cầu Issue #5 (`RULE-SEARCH-01` đến `RULE-SEARCH-07`)

| BR ID | Tên Quy tắc nghiệp vụ | Mô tả chi tiết (Business Rule Specification) | Test Condition ID | Kỹ thuật kiểm thử | Automation? | Ghi chú nghiệp vụ |
| :--- | :--- | :--- | :--- | :--- | :---: | :--- |
| **RULE-SEARCH-01** | Tìm kiếm theo từ khóa hợp lệ | Người dùng nhập từ khóa tìm kiếm hợp lệ (chức danh công việc, kỹ năng công nghệ như "Java", "React", "Tester", hoặc tên công ty). Hệ thống thực hiện tìm kiếm toàn văn, không phân biệt hoa thường, hỗ trợ tiếng Việt có dấu/không dấu, và trả về danh sách công việc phù hợp có tiêu đề, mô tả hoặc tag kỹ năng khớp với từ khóa. | `TCOND-SEARCH-01`<br>`TCOND-SEARCH-02`<br>`TCOND-SEARCH-03` | Equivalence Partitioning (EP) | ✅ Yes | Khớp với `TC-SEARCH-001`, `TC-SEARCH-002`, `TC-SEARCH-003`, `TC-SEARCH-011` |
| **RULE-SEARCH-02** | Xử lý khi từ khóa không tồn tại | Khi người dùng nhập từ khóa không khớp với bất kỳ công việc nào trong hệ thống (ví dụ: `xyz999_non_existent`), giao diện hiển thị Empty State thân thiện *"Chưa tìm thấy vị trí phù hợp / Không tìm thấy việc làm phù hợp"* kèm nút bấm hỗ trợ "Đặt lại bộ lọc / Xóa bộ lọc". | `TCOND-SEARCH-04` | Equivalence Partitioning (EP) | ✅ Yes | Khớp với `TC-SEARCH-005` |
| **RULE-SEARCH-03** | Xử lý từ khóa rỗng & khoảng trắng | Khi ô tìm kiếm để trống hoặc chỉ nhập toàn khoảng trắng (whitespace-only) rồi nhấn tìm kiếm, hệ thống xem như không áp dụng bộ lọc từ khóa và hiển thị toàn bộ danh sách các việc làm đang khả dụng trong hệ thống. | `TCOND-SEARCH-05` | Equivalence Partitioning (EP) | ✅ Yes | Khớp với `TC-SEARCH-006` |
| **RULE-SEARCH-04** | Lọc theo Địa điểm làm việc | Cho phép người dùng lọc danh sách việc làm theo Địa điểm (Location: Hà Nội, TP.HCM, Đà Nẵng, Remote). Kết quả trả về phải chính xác 100% công việc thuộc địa điểm đã chọn. | `TCOND-SEARCH-06` | Equivalence Partitioning (EP) | ✅ Yes | Khớp với `TC-SEARCH-004` |
| **RULE-SEARCH-05** | Lọc theo Loại hình & Kết hợp đa tiêu chí | Cho phép lọc theo Loại hình công việc (Job Type: Full-time, Internship/Thực tập) và hỗ trợ kết hợp đồng thời nhiều tiêu chí (Từ khóa + Địa điểm + Loại hình). Kết quả trả về là giao (AND logic) của các tập kết quả thỏa mãn đồng thời tất cả tiêu chí. Đồng thời hỗ trợ nút "Đặt lại bộ lọc" để hoàn nguyên danh sách ban đầu. | `TCOND-SEARCH-07`<br>`TCOND-SEARCH-08` | Decision Table (DT) / State Transition | ✅ Yes | Khớp với `TC-SEARCH-005` (Job Type) và `TC-SEARCH-006` (Multi-criteria), `TC-SEARCH-009` (Reset) |
| **RULE-SEARCH-06** | Kiểm tra an toàn dữ liệu: Chống XSS Injection | Khi người dùng nhập các chuỗi mã kịch bản độc hại XSS (ví dụ: `<script>alert(1)</script>`) hoặc chuỗi ký tự đặc biệt (`@#$%^&*()`) vào ô tìm kiếm, hệ thống phải thực hiện HTML entity encoding / sanitization. Trình duyệt **tuyệt đối không được thực thi mã kịch bản (không có Alert popup)**, không làm vỡ layout giao diện và hiển thị thông báo an toàn. | `TCOND-SEARCH-09` | Error Guessing (EG) / Security | ✅ Yes | Khớp với `TC-SEARCH-007` & `TC-SEC-XSS` |
| **RULE-SEARCH-07** | Kiểm tra an toàn dữ liệu: Chống SQL Injection | Khi người dùng nhập các chuỗi tấn công SQL Injection (ví dụ: `' OR 1=1 --`, `' UNION SELECT null, null --`) vào ô tìm kiếm, hệ thống sử dụng Prepared Statements / Parameterized Queries để xử lý an toàn. Hệ thống **tuyệt đối không bị crash, không lộ lỗi cú pháp SQL (SQL Exception) và không phát sinh lỗi máy chủ 500**. | `TCOND-SEARCH-10` | Error Guessing (EG) / Security | ✅ Yes | Khớp với `TC-SEARCH-008` & `TC-SEC-SQLI` |

### 4.2. Bảng phân tích kỹ thuật kiểm thử Module M3

| Nhóm kỹ thuật kiểm thử | Quy tắc áp dụng | Mục tiêu kiểm tra |
|---|---|---|
| **Equivalence Partitioning (EP)** | `RULE-SEARCH-01`<br>`RULE-SEARCH-02`<br>`RULE-SEARCH-03`<br>`RULE-SEARCH-04` | - Phân vùng từ khóa hợp lệ vs không hợp lệ.<br>- Phân vùng địa điểm và từ khóa rỗng. |
| **Decision Table (DT)** | `RULE-SEARCH-05` | - Tổ hợp điều kiện: Keyword x Location x Job Type (AND logic). |
| **Error Guessing (EG)** | `RULE-SEARCH-06`<br>`RULE-SEARCH-07` | - Đoán lỗi bảo mật: XSS Injection (`<script>alert(1)</script>`).<br>- Đoán lỗi injection: SQL Injection (`' OR 1=1 --`). |

---

## 5. MODULE 4 (M4): ỨNG TUYỂN & THEO DÕI HỒ SƠ (APPLICATION & TRACKING)
* **Người phụ trách:** Lã Trọng Huy (`TrongHuy317`)
* **Endpoint / Giao diện kiểm thử:** `/jobs/:id` (Modal ứng tuyển), `/candidate/applications` (Kanban list).

### 5.1. Bảng Business Rules (M4)

| BR ID | Mô tả Quy tắc nghiệp vụ (Business Rule) | Test Condition ID | Kỹ thuật kiểm thử | Automation? | Ghi chú |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **RULE-APPLY-01** | Ứng tuyển thành công với hồ sơ/CV đã có; Cảnh báo chặn nộp nếu ứng viên chưa tải CV lên hồ sơ cá nhân (`BR-02`). | `TCOND-APPLY-01`, `TCOND-APPLY-02` | State Transition / Decision Table | ✅ Yes | Tuân thủ yêu cầu có CV theo `BR-02` |
| **RULE-APPLY-02** | Chặn ứng tuyển trùng lặp: Một ứng viên chỉ được nộp hồ sơ 1 lần duy nhất vào cùng một tin tuyển dụng (nút đổi sang "Đã ứng tuyển" và bị disabled). | `TCOND-APPLY-03` | State Transition / Decision Table | ✅ Yes | Tránh spam đơn tuyển dụng |
| **RULE-APPLY-03** | Ràng buộc phân quyền: Chỉ tài khoản `CANDIDATE` mới được nộp đơn. Khách vãng lai bị chuyển hướng về `/login`; Employer bị chặn ứng tuyển. | `TCOND-APPLY-04`, `TCOND-APPLY-05` | Decision Table | ✅ Yes | Đảm bảo tính toàn vẹn phân quyền |
| **RULE-APPLY-04** | Theo dõi trạng thái ứng tuyển dạng Grouped List (Kanban Tracking 5 cột): [Đã nộp] -> [Đã xem] -> [Phỏng vấn] -> [Offer] -> [Từ chối]. Giao diện Read-only (QĐ #8). | `TCOND-APPLY-06`, `TCOND-APPLY-07`, `TCOND-APPLY-08` | State Transition / Decision Table | ✅ Yes | Quyết định thiết kế #8 |
| **RULE-APPLY-05** | Nhận thông báo In-app khi cập nhật trạng thái phỏng vấn kèm ghi chú thời gian và link họp trực tuyến (`BR-09`, `BR-10`). | `TCOND-APPLY-09`, `TCOND-APPLY-10`, `TCOND-APPLY-11` | State Transition / Decision Table | ✅ Yes | Tuân thủ `BR-09` & `BR-10` |

---

## 6. BẢNG TỔNG HỢP TOÀN BỘ BUSINESS RULES HỆ THỐNG

| Module | Tên Module | Mã tiền tố | Số lượng BRs | Số lượng TCONDs | Tỷ lệ Tự động hóa |
|:---:|---|:---:|:---:|:---:|:---:|
| **M1** | Đăng nhập & Phân quyền | `RULE-LOGIN-xx` | 6 | 12 | 100% |
| **M2** | Đăng ký tài khoản | `RULE-REG-xx` | 5 | 11 | 100% |
| **M3** | **Tìm kiếm & Lọc việc làm (Issue #5)** | `RULE-SEARCH-01` đến `07` | **7** | **10** | **100%** |
| **M4** | Ứng tuyển & Theo dõi hồ sơ | `RULE-APPLY-xx` | 5 | 11 | 100% |
| **TỔNG**| **Toàn bộ 4 Module** | | **23 Rules** | **44 Conditions** | **100% (44/44)** |

---
*Tài liệu BM02 được chuẩn hóa theo định dạng ISO/IEC/IEEE 29119-3 và làm căn cứ gốc cho việc nghiệm thu Issue #5.*
