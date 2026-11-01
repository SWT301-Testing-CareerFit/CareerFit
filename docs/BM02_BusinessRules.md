# BM02 – TÀI LIỆU BUSINESS RULES & TEST CONDITIONS (ĐẶC TẢ NGHIỆP VỤ & ĐIỀU KIỆN KIỂM THỬ)

**Dự án:** CareerFit – Hệ thống Kết nối Tuyển dụng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Tác giả:** Nguyễn Lâm Thi (Test Lead - M1), Võ Nguyên Khánh (Framework Owner / Tester - M2)  
**Phiên bản:** 1.1  
**Ngày cập nhật:** 01/11/2026  

---

## 1. TỔNG QUAN

Tài liệu này xác định các quy tắc nghiệp vụ (Business Rules - BR) và các điều kiện kiểm thử (Test Conditions - TCOND) suy biến từ yêu cầu hệ thống (SRS/Acceptance Criteria).  
Các điều kiện kiểm thử được thiết kế dựa trên các kỹ thuật kiểm thử hộp đen chuẩn ISTQB CTFL v4.0:
* **EP (Equivalence Partitioning):** Phân vùng tương đương.
* **BVA (Boundary Value Analysis):** Phân tích giá trị biên.
* **DT (Decision Table):** Bảng quyết định tổ hợp điều kiện.
* **ST (State Transition):** Kiểm thử chuyển trạng thái.
* **EG (Error Guessing):** Đoán lỗi ngoại lệ.

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

### 2.2. Bảng quyết định kiểm thử (Decision Table - M1)

| Điều kiện (Conditions) | R1 | R2 | R3 | R4 | R5 | R6 | R7 | R8 | R9 |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **Email tồn tại trong hệ thống?** | Y | Y | Y | Y | N | Valid | Valid | Empty | Valid |
| **Password đúng tuyệt đối?** | Y | Y | Y | N | - | Case-Wrong | Empty | Valid | Empty |
| **Role người dùng** | CANDIDATE | EMPLOYER | ADMIN | - | - | - | - | - | - |
| **Trạng thái tài khoản** | ACTIVE | ACTIVE | ACTIVE | ACTIVE | - | ACTIVE | - | - | LOCKED |
| **Hành động (Actions)** | | | | | | | | | |
| Đăng nhập thành công | **X** | **X** | **X** | | | | | | |
| Điều hướng trang Candidate (`/jobs`) | **X** | | | | | | | | |
| Điều hướng trang Employer (`/employer`)| | **X** | | | | | | | |
| Điều hướng trang Admin (`/admin`) | | | **X** | | | | | | |
| Báo lỗi "Email/mật khẩu không đúng"| | | | **X** | **X** | **X** | | | |
| Báo lỗi validation required | | | | | | | **X** | **X** | |
| Báo lỗi "Tài khoản bị khóa/chưa kích hoạt"| | | | | | | | | **X** |

---

## 3. MODULE 2 (M2): ĐĂNG KÝ TÀI KHOẢN (REGISTRATION - CANDIDATE & EMPLOYER)
* **Người phụ trách:** Võ Nguyên Khánh (`richnk123a-cyber` / `VoNguyenKhanh`)
* **Vai trò:** Framework Owner / Tester phụ trách Module M2
* **Endpoint / Giao diện kiểm thử:** `/login` (Tab Đăng ký - `[data-testid='tab-register']`), API `/api/auth/register/candidate`, API `/api/auth/register/employer`
* **Testable UI Scope:** Kiểm thử chức năng Đăng ký tài khoản cho cả Ứng viên (CANDIDATE) và Nhà tuyển dụng / Doanh nghiệp (EMPLOYER) kèm tải lên Giấy phép kinh doanh (GPKD - PDF/PNG/JPG). Kiểm thử tương tác chuyển đổi tab, hiển thị động các trường dữ liệu theo vai trò, xác thực toàn bộ các trường (Họ tên, Tên công ty, Email, Số điện thoại VN, Mật khẩu theo biên [8, 32], Xác nhận mật khẩu, Định dạng và kích thước file GPKD <= 10MB) và phản hồi hệ thống.

### 3.1. Bảng Business Rules (RULE-REG-01 đến RULE-REG-08)

| BR ID | Mô tả Quy tắc nghiệp vụ (Business Rule) | Test Condition ID | Kỹ thuật kiểm thử | Automation? | Ghi chú |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **RULE-REG-01** | **Định dạng Email hợp lệ:** Email đăng ký phải tuân theo chuẩn `local-part@domain.tld`. Hệ thống chấp nhận các email hợp lệ (VD: `cand@gmail.com`, `user@fpt.edu.vn`), và chặn các email sai định dạng: thiếu ký tự `@`, thiếu domain, chứa khoảng trắng, hoặc có nhiều ký tự `@`. Thông báo lỗi: *"Email không đúng định dạng."* | `TCOND-REG-01`<br>`TCOND-REG-02` | Equivalence Partitioning (EP) | ✅ Yes | Validation Client-side Regex & Backend API |
| **RULE-REG-02** | **Tính duy nhất của Email (Anti-Duplicate):** Mỗi email chỉ được đăng ký một tài khoản duy nhất với phương thức xác thực nội bộ (LOCAL). Nếu email đã tồn tại trong cơ sở dữ liệu, hệ thống từ chối đăng ký và trả về thông báo lỗi: *"Email này đã được sử dụng để đăng ký tài khoản trực tiếp. Vui lòng chọn email khác hoặc Đăng nhập."* | `TCOND-REG-03`<br>`TCOND-REG-04` | Equivalence Partitioning (EP) | ✅ Yes | Kiểm tra xung đột dữ liệu (Conflict 409 / BadRequest) |
| **RULE-REG-03** | **Độ dài và Độ phức tạp của Mật khẩu (Password BVA & Complexity):**<br>- **Độ dài hợp lệ:** Nằm trong khoảng **[8, 32] ký tự** theo tài liệu đặc tả SRS.<br>- **Độ phức tạp:** Phải chứa ít nhất 1 chữ in hoa (A-Z), ít nhất 1 chữ in thường (a-z), ít nhất 1 chữ số (0-9) và ít nhất 1 ký tự đặc biệt (`!@#$%^&*`...).<br>- Báo lỗi tương ứng: *"Mật khẩu cần có ít nhất 8 ký tự"*, *"Mật khẩu cần có 1 chữ hoa"*, *"Mật khẩu cần có 1 chữ số"*, *"Mật khẩu cần có 1 ký tự đặc biệt"*. | `TCOND-REG-05`<br>`TCOND-REG-06`<br>`TCOND-REG-07`<br>`TCOND-REG-08`<br>`TCOND-REG-09`<br>`TCOND-REG-10`<br>`TCOND-REG-11` | Boundary Value Analysis (BVA) & Equivalence Partitioning (EP) | ✅ Yes | BVA trên các giá trị biên: 7, 8, 9, 31, 32, 33 ký tự |
| **RULE-REG-04** | **Khớp Xác nhận mật khẩu (Password Confirmation):** Giá trị nhập tại trường "Xác nhận mật khẩu" phải trùng khớp 100% từng ký tự với trường "Mật khẩu" (có phân biệt chữ hoa và chữ thường - Case-sensitive). Nếu không khớp, hệ thống chặn submit và hiển thị: *"Mật khẩu xác nhận không khớp."* | `TCOND-REG-12`<br>`TCOND-REG-13` | Equivalence Partitioning (EP) | ✅ Yes | Đảm bảo người dùng nhập chính xác mật khẩu mong muốn |
| **RULE-REG-05** | **Ràng buộc Họ và tên / Người đại diện tuyển dụng:** Trường Họ và tên là bắt buộc. Chiều dài hợp lệ từ 2 đến 100 ký tự; chấp nhận chữ cái tiếng Việt có dấu, chữ Latin, dấu cách và dấu gạch nối. Chặn để trống hoặc dữ liệu nguy hiểm XSS/HTML Injection. | `TCOND-REG-14`<br>`TCOND-REG-15` | Equivalence Partitioning (EP) & Error Guessing (EG) | ✅ Yes | Sanitize và validate dữ liệu cá nhân |
| **RULE-REG-06** | **Định dạng Số điện thoại di động Việt Nam:** Số điện thoại phải đúng cấu trúc mạng di động VN: gồm đúng **10 chữ số** và bắt đầu bằng các đầu mạng hợp lệ: `03`, `05`, `07`, `08`, `09`. Hệ thống từ chối các số có 9 chữ số, 11 chữ số, đầu số không hợp lệ (`01`, `02`, `04`, `06`...), hoặc chứa chữ cái. Thông báo lỗi: *"Số điện thoại không hợp lệ. Phải có 10 số, bắt đầu bằng 03/05/07/08/09."* | `TCOND-REG-16`<br>`TCOND-REG-17` | Equivalence Partitioning (EP) & BVA (Độ dài số) | ✅ Yes | Chuẩn hóa theo quy chuẩn Bộ Thông tin & Truyền thông |
| **RULE-REG-07** | **Ràng buộc Hồ sơ Doanh nghiệp & Tải lên Giấy phép kinh doanh (GPKD):**<br>- Khi chọn vai trò Doanh nghiệp (EMPLOYER): Trường **Tên công ty** và **Upload Giấy phép kinh doanh** là bắt buộc.<br>- **Định dạng tệp cho phép:** `.pdf`, `.png`, `.jpg`, `.jpeg`. Chặn các định dạng khác (`.exe`, `.txt`, `.docx`). Báo lỗi: *"Vui lòng chọn tệp định dạng PDF, PNG hoặc JPG."*<br>- **Dung lượng tối đa:** Cho phép tệp **≤ 10MB**. Nếu vượt quá 10MB -> Bị chặn và báo lỗi: *"Dung lượng tệp không được vượt quá 10 MB."*<br>- Nếu để trống -> Báo lỗi: *"Vui lòng nhập tên công ty."* hoặc *"Vui lòng tải lên Giấy phép kinh doanh..."* | `TCOND-REG-18`<br>`TCOND-REG-19`<br>`TCOND-REG-20`<br>`TCOND-REG-21`<br>`TCOND-REG-22` | Equivalence Partitioning (EP) & Boundary Value Analysis (BVA File Size) | ✅ Yes | BVA dung lượng tệp: <10MB, =10MB, >10MB |
| **RULE-REG-08** | **Xử lý Đăng ký thành công & Vòng đời kích hoạt tài khoản:**<br>- **Ứng viên:** Đăng ký thành công hiển thị thông báo *"Đăng ký tài khoản Ứng viên thành công! Vui lòng đăng nhập."* và tự động chuyển về tab Đăng nhập.<br>- **Doanh nghiệp:** Đăng ký thành công hiển thị thông báo *"Đăng ký tài khoản Nhà tuyển dụng thành công! Đang chờ kiểm duyệt."*, tài khoản ở trạng thái chờ duyệt hồ sơ GPKD từ phía Quản trị viên (Admin).<br>- Hệ thống gửi email kích hoạt tài khoản; liên kết kích hoạt có hiệu lực tối đa **24 giờ**. Quá 24 giờ liên kết sẽ hết hạn. | `TCOND-REG-23`<br>`TCOND-REG-24`<br>`TCOND-REG-25` | State Transition (ST) & Equivalence Partitioning (EP) | ✅ Yes (UI Flow) / ❌ Manual (Email 24h Timeout) | Kiểm thử luồng thành công và quản lý phiên |

---

### 3.2. Áp dụng Kỹ thuật kiểm thử BVA & EP cho Module M2

#### 3.2.1. Kỹ thuật Phân tích giá trị biên (Boundary Value Analysis - BVA)

##### A. BVA Độ dài mật khẩu trong khoảng hợp lệ [8, 32] ký tự (RULE-REG-03)
Khoảng hợp lệ theo tài liệu đặc tả SRS: $8 \le \text{length} \le 32$.  
Tập các điểm kiểm thử giá trị biên 2 phía bao gồm:
* Biên dưới: $min - 1 = 7$, $min = 8$, $min + 1 = 9$
* Biên trên: $max - 1 = 31$, $max = 32$, $max + 1 = 33$

| Điểm kiểm thử BVA | Giá trị độ dài | Dữ liệu kiểm thử mẫu | Kết quả mong đợi | Trạng thái | Mã Test Case |
| :--- | :---: | :--- | :--- | :---: | :--- |
| **min - 1 (Biên ngoài dưới)** | 7 ký tự | `Abc@123` | Báo lỗi: *"Mật khẩu cần có ít nhất 8 ký tự"* | ❌ Fail (Bị chặn) | `TC-REG-004` |
| **min (Biên trong dưới)** | **8 ký tự** | `Abcd@123` | Đăng ký thành công (Hợp lệ ngưỡng biên) | ✅ Pass (Chấp nhận) | `TC-REG-005` |
| **min + 1 (Cận biên dưới)** | 9 ký tự | `Abcde@123` | Đăng ký thành công (Hợp lệ danh nghĩa) | ✅ Pass (Chấp nhận) | `TC-REG-006` |
| **max - 1 (Cận biên trên)** | 31 ký tự | `Pass1@abcdefghijklmnopqrstuvwx` | Đăng ký thành công (Hợp lệ danh nghĩa) | ✅ Pass (Chấp nhận) | `TC-REG-007` |
| **max (Biên trong trên)** | **32 ký tự** | `Pass1@abcdefghijklmnopqrstuvwxy` | Đăng ký thành công (Hợp lệ ngưỡng biên) | ✅ Pass (Chấp nhận) | `TC-REG-008` |
| **max + 1 (Biên ngoài trên)** | 33 ký tự | `Pass1@abcdefghijklmnopqrstuvwxyz1` | Báo lỗi vượt quá độ dài tối đa 32 ký tự theo SRS *(Ghi nhận Defect/Deviation nếu SUT UI cho phép đến 100 ký tự)* | ❌ Fail / ⚠️ Deviation | `TC-REG-009` |

##### B. BVA Dung lượng tệp Giấy phép kinh doanh (GPKD) [0, 10MB] (RULE-REG-07)
Ngưỡng dung lượng tối đa cho phép tải lên hệ thống là 10 MB ($10 \times 1024 \times 1024 = 10,485,760$ Bytes).

| Điểm kiểm thử BVA | Dung lượng tệp | Tệp kiểm thử mẫu | Kết quả mong đợi | Trạng thái | Mã Test Case |
| :--- | :---: | :--- | :--- | :---: | :--- |
| **Hợp lệ thông thường** | ~500 KB | `sample_license.pdf` | Tệp được tải lên và hiển thị tên tệp + kích thước | ✅ Pass | `TC-REG-002` |
| **max (Ngưỡng biên trên)** | Đúng 10 MB | `license_exact_10mb.pdf` | Tệp được chấp nhận hợp lệ | ✅ Pass | `TC-REG-002` (mở rộng) |
| **max + $\Delta$ (Vượt biên)** | 11 MB (>10MB) | `license_11mb.pdf` | Báo lỗi: *"Dung lượng tệp không được vượt quá 10 MB."*, hủy chọn tệp | ❌ Fail (Bị chặn) | `TC-REG-016` |

---

#### 3.2.2. Kỹ thuật Phân vùng tương đương (Equivalence Partitioning - EP)

##### A. Phân vùng Định dạng Email (RULE-REG-01)
* **Lớp hợp lệ (Valid Partition):** Định dạng đúng `local@domain.tld` (VD: `cand_123@gmail.com`, `recruiter@fpt.com.vn`) -> Hợp lệ, chấp nhận submit.
* **Lớp không hợp lệ (Invalid Partitions):**
  * Thiếu ký tự `@`: `candidate.gmail.com` -> Báo lỗi định dạng email.
  * Thiếu phần tên miền (domain): `user@` hoặc `user@invalid` -> Báo lỗi định dạng email (`TC-REG-012`).
  * Chứa khoảng trắng: `user name@gmail.com` -> Báo lỗi định dạng email.
  * Trùng lặp ký tự `@`: `user@@gmail.com` -> Báo lỗi định dạng email.

##### B. Phân vùng Tính duy nhất của Email (RULE-REG-02)
* **Lớp hợp lệ (Valid):** Email mới hoàn toàn chưa có trong cơ sở dữ liệu (VD: `cand_{RANDOM}@gmail.com`) -> Cho phép đăng ký (`TC-REG-001`).
* **Lớp không hợp lệ (Invalid):** Email đã được đăng ký tài khoản trước đó (VD: `candidate@careerfit.vn`) -> Báo lỗi: *"Email này đã được sử dụng để đăng ký tài khoản trực tiếp..."* (`TC-REG-013`).

##### C. Phân vùng Định dạng Số điện thoại Việt Nam (RULE-REG-06)
* **Lớp hợp lệ (Valid):** Đúng 10 chữ số và bắt đầu bằng một trong các đầu số `03`, `05`, `07`, `08`, `09` (VD: `0912345678`, `0387654321`, `0765432109`) -> Hợp lệ (`TC-REG-001`, `TC-REG-002`).
* **Lớp không hợp lệ (Invalid):**
  * Thiếu số: 9 chữ số (`091234567`) -> Báo lỗi số điện thoại không hợp lệ.
  * Thừa số: 11 chữ số (`09123456789`) -> Báo lỗi số điện thoại không hợp lệ.
  * Sai đầu mạng: 10 chữ số nhưng bắt đầu bằng đầu số cố định hoặc không hợp lệ: `0123456789`, `0243123456`, `0412345678` -> Báo lỗi số điện thoại không hợp lệ (`TC-REG-014`).
  * Chứa ký tự chữ hoặc ký tự đặc biệt: `091234567a` -> Báo lỗi.

##### D. Phân vùng Xác nhận Mật khẩu (RULE-REG-04)
* **Lớp hợp lệ (Valid):** Mật khẩu xác nhận trùng khớp 100% từng ký tự với mật khẩu đã nhập (`TC-REG-001`, `TC-REG-005`).
* **Lớp không hợp lệ (Invalid):**
  * Khác chuỗi: `Pass1@ab` vs `DifferentPass1@` -> Báo lỗi *"Mật khẩu xác nhận không khớp."* (`TC-REG-011`).
  * Khác kiểu chữ hoa/thường (Case-sensitive): `Pass1@ab` vs `pass1@ab` -> Báo lỗi *"Mật khẩu xác nhận không khớp."*.

##### E. Phân vùng Định dạng Tệp tải lên GPKD (RULE-REG-07)
* **Lớp hợp lệ (Valid):** Tệp có phần mở rộng `.pdf`, `.png`, `.jpg`, `.jpeg` và dung lượng ≤ 10MB -> Chấp nhận tệp (`TC-REG-002`, `TC-REG-003`).
* **Lớp không hợp lệ (Invalid):**
  * Tệp tài liệu/mã nguồn sai định dạng: `.txt`, `.docx`, `.exe`, `.zip`, `.sh` -> Báo lỗi: *"Vui lòng chọn tệp định dạng PDF, PNG hoặc JPG."* và tự động xóa lựa chọn tệp (`TC-REG-015`).
  * Không tải tệp (bỏ trống khi chọn vai trò Doanh nghiệp) -> Báo lỗi: *"Vui lòng tải lên Giấy phép kinh doanh (PDF, PNG hoặc JPG)."* (`TC-REG-017`).

---

### 3.3. Bảng quyết định kiểm thử (Decision Table - M2)

| Điều kiện (Conditions) | R1 | R2 | R3 | R4 | R5 | R6 | R7 | R8 | R9 | R10 | R11 | R12 |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **Vai trò lựa chọn (Role)** | CAND | EMP | EMP | CAND | CAND | CAND | CAND | CAND | CAND | EMP | EMP | EMP |
| **Họ tên / Tên đại diện hợp lệ?** | Y | Y | Y | Y | Y | Y | Y | Y | Y | Y | Y | Y |
| **Tên công ty (với Doanh nghiệp)** | - | Y | Y | - | - | - | - | - | - | N | Y | Y |
| **File GPKD hợp lệ (PDF/PNG <=10MB)?** | - | Y (PDF) | Y (PNG) | - | - | - | - | - | - | Empty | Wrong (.txt) | > 10MB |
| **Email hợp lệ & chưa từng tồn tại?** | Y | Y | Y | Invalid Fmt | Trùng lặp | Y | Y | Y | Y | Y | Y | Y |
| **Số điện thoại VN 10 số (03/05/07/08/09)?** | Y | Y | Y | Y | Y | Y | Y | Y | Đầu số 01 | Y | Y | Y |
| **Độ dài mật khẩu [8, 32] ký tự?** | Y | Y | Y | Y | Y | 7 ký tự | 8-32 ký tự | 8-32 ký tự | Y | Y | Y | Y |
| **Độ phức tạp (Hoa + Thường + Số + ĐB)?**| Y | Y | Y | Y | Y | Y | Thiếu Hoa | Y | Y | Y | Y | Y |
| **Xác nhận mật khẩu trùng khớp?** | Y | Y | Y | Y | Y | Y | Y | Lệch | Y | Y | Y | Y |
| **HÀNH ĐỘNG (ACTIONS)** | | | | | | | | | | | | |
| **Đăng ký Candidate thành công** | **X** | | | | | | | | | | | |
| **Đăng ký Employer thành công (chờ duyệt)**| | **X** | **X** | | | | | | | | | |
| **Báo lỗi Email sai định dạng** | | | | **X** | | | | | | | | |
| **Báo lỗi Email đã tồn tại** | | | | | **X** | | | | | | | |
| **Báo lỗi mật khẩu < 8 ký tự** | | | | | | **X** | | | | | | |
| **Báo lỗi thiếu chữ hoa trong mật khẩu** | | | | | | | **X** | | | | | |
| **Báo lỗi xác nhận mật khẩu không khớp**| | | | | | | | **X** | | | | |
| **Báo lỗi số điện thoại không hợp lệ** | | | | | | | | | **X** | | | |
| **Báo lỗi thiếu tên công ty / thiếu GPKD**| | | | | | | | | | **X** | | |
| **Báo lỗi file GPKD sai định dạng (.txt)**| | | | | | | | | | | **X** | |
| **Báo lỗi dung lượng file GPKD > 10MB** | | | | | | | | | | | | **X** |
| **Mã Test Case tương ứng** | `TC-001` | `TC-002` | `TC-003` | `TC-012` | `TC-013` | `TC-004` | `TC-010` | `TC-011` | `TC-014` | `TC-017` | `TC-015` | `TC-016` |

---

## 4. KHUNG THEO DÕI CHO CÁC MODULE TIẾP THEO (M3, M4)

*(Các thành viên phụ trách sẽ cập nhật chi tiết trong các nhánh tương ứng theo phân công của nhóm)*

* **MODULE 3 (M3): TÌM KIẾM & LỌC VIỆC LÀM (JOB SEARCH & FILTER)**  
  * **Người phụ trách:** Nguyễn Trọng Tiến (`tiendeptroai`)  
  * **Quy tắc chính:** `RULE-SEARCH-01` (Tìm kiếm từ khóa), `RULE-SEARCH-02` (Lọc theo ngành nghề/địa điểm/mức lương), `RULE-SEARCH-03` (Không tìm thấy kết quả), `RULE-SEARCH-04` (Input an toàn XSS/SQLi).
* **MODULE 4 (M4): ỨNG TUYỂN & THEO DÕI HỒ SƠ (APPLICATION & TRACKING)**  
  * **Người phụ trách:** Lã Trọng Huy (`TrongHuy317`)  
  * **Quy tắc chính:** `RULE-APPLY-01` (Nộp hồ sơ ứng tuyển kèm CV), `RULE-APPLY-02` (Chặn ứng tuyển trùng lặp), `RULE-APPLY-03` (Theo dõi trạng thái hồ sơ).
