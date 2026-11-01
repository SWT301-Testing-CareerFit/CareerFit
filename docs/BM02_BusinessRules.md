# BM02 – TÀI LIỆU BUSINESS RULES & TEST CONDITIONS (ĐẶC TẢ NGHIỆP VỤ & ĐIỀU KIỆN KIỂM THỬ)

**Dự án:** CareerFit – Nền tảng Tuyển dụng IT & Định hướng Kỹ năng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Phiên bản:** 2.0 (Hợp nhất toàn diện 4 Module: M1, M2, M3, M4)  
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
* **EG (Error Guessing):** Đoán lỗi ngoại lệ và dữ liệu bất thường.

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

## 3. MODULE 2 (M2): ĐĂNG KÝ TÀI KHOẢN (USER REGISTRATION)
* **Người phụ trách:** Võ Nguyên Khánh (`richnk123a-cyber`)
* **Endpoint / Giao diện kiểm thử:** `/login` (Tab "Đăng ký"), Form đăng ký Ứng viên (Candidate) và Nhà tuyển dụng (Employer).
* **Testable UI Scope:** Kiểm thử nhập form đăng ký, xác thực độ mạnh mật khẩu, đối khớp mật khẩu xác nhận, kiểm tra tính duy nhất của email, phân loại vai trò đăng ký và upload giấy phép kinh doanh đối với Employer.

### 3.1. Bảng Business Rules (M2)

| BR ID | Mô tả Quy tắc nghiệp vụ (Business Rule) | Test Condition ID | Kỹ thuật kiểm thử | Automation? | Ghi chú |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **RULE-REG-01** | Đăng ký tài khoản Ứng viên (CANDIDATE) thành công: Người dùng nhập đầy đủ thông tin hợp lệ (Họ tên, Email chưa đăng ký, Mật khẩu đạt chuẩn, Mật khẩu xác nhận khớp). Tài khoản được tạo với `role = CANDIDATE`, `status = ACTIVE`, hệ thống chuyển hướng hoặc thông báo đăng ký thành công. | `TCOND-REG-01`, `TCOND-REG-02` | Equivalence Partitioning / Decision Table | ✅ Yes | Dữ liệu email được sinh ngẫu nhiên qua `DataGenerator` |
| **RULE-REG-02** | Đăng ký tài khoản Doanh nghiệp (EMPLOYER) kèm Giấy phép kinh doanh: Doanh nghiệp đăng ký cần nhập thông tin công ty và tải file Giấy phép kinh doanh (PDF/Ảnh). Mặc định tài khoản có `role = EMPLOYER`, trạng thái xác minh `verification_status = UNVERIFIED`. Tài khoản này đăng nhập được nhưng bị chặn đăng tin tuyển dụng cho đến khi Admin duyệt (`BR-01`). | `TCOND-REG-03` | Decision Table / State Transition | ✅ Yes | Tuân thủ luật nghiệp vụ `BR-01` |
| **RULE-REG-03** | Ràng buộc tính duy nhất của Email (Unique Constraint): Mỗi email chỉ được đăng ký một tài khoản duy nhất trong toàn hệ thống. Nếu nhập email đã tồn tại trong database, hệ thống chặn submit và báo lỗi *"Email đã được sử dụng"*. | `TCOND-REG-04` | Equivalence Partitioning / Error Guessing | ✅ Yes | Ngăn chặn trùng lặp tài khoản |
| **RULE-REG-04** | Ràng buộc độ phức tạp của Mật khẩu (Password Strength Policy) & Đối khớp mật khẩu: Mật khẩu tối thiểu 8 ký tự, phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt. Mật khẩu xác nhận (Confirm Password) phải khớp 100% với mật khẩu đã nhập; nếu không khớp, hệ thống hiển thị thông báo lỗi tức thì. | `TCOND-REG-05`, `TCOND-REG-06`, `TCOND-REG-07`, `TCOND-REG-08` | Boundary Value Analysis / Equivalence Partitioning | ✅ Yes | BVA tại biên 7, 8, 32 ký tự |
| **RULE-REG-05** | Ràng buộc trường bắt buộc và định dạng đầu vào: Tất cả các trường đánh dấu `*` (Tên, Email, Mật khẩu, Xác nhận mật khẩu) không được để trống hoặc chỉ chứa khoảng trắng (whitespace-only). Email phải tuân thủ đúng định dạng chuẩn RFC. | `TCOND-REG-09`, `TCOND-REG-10`, `TCOND-REG-11` | Equivalence Partitioning / BVA | ✅ Yes | Kiểm tra client-side form validation |

### 3.2. Bảng phân tích kiểm thử độ mạnh mật khẩu (BVA/EP - M2)

| Bộ dữ liệu Password | Đặc điểm | Phân vùng | Kết quả kỳ vọng | Mã TCOND |
|---|---|---|---|---|
| `Abc@1` (5 ký tự) | Dưới 8 ký tự | Invalid (Dưới biên) | Từ chối: Mật khẩu phải từ 8 ký tự | `TCOND-REG-05` |
| `Abcdef@1` (8 ký tự) | Đúng 8 ký tự, đủ hoa/thường/số/ký tự đặc biệt | Valid (Ngay biên chuẩn) | Chấp nhận mật khẩu hợp lệ | `TCOND-REG-01` |
| `candidate123` | Thiếu chữ hoa và ký tự đặc biệt | Invalid (Thiếu thành phần) | Từ chối: Mật khẩu chưa đủ độ mạnh | `TCOND-REG-06` |
| `CANDIDATE@123` | Thiếu chữ thường | Invalid (Thiếu chữ thường) | Từ chối: Cần có chữ thường | `TCOND-REG-07` |
| `Password123!` vs `Password123?` | Confirm password khác nhau | Invalid (Không khớp) | Từ chối: Mật khẩu xác nhận không khớp | `TCOND-REG-08` |

---

## 4. MODULE 3 (M3): TÌM KIẾM & LỌC VIỆC LÀM (JOB SEARCH & FILTER)
* **Người phụ trách:** Nguyễn Trọng Tiến (`tiendeptroai`)
* **Endpoint / Giao diện kiểm thử:** `/jobs`, Thanh tìm kiếm từ khóa, Bộ lọc đa tiêu chí (Ngành nghề, Địa điểm, Mức lương, Loại hình công việc).
* **Testable UI Scope:** Kiểm tra chức năng tìm kiếm toàn văn theo từ khóa, cơ chế lọc kết hợp đa điều kiện (AND logic), giao diện trạng thái trống khi không có kết quả (Empty State), và nút đặt lại bộ lọc (Reset Filter).

### 4.1. Bảng Business Rules (M3)

| BR ID | Mô tả Quy tắc nghiệp vụ (Business Rule) | Test Condition ID | Kỹ thuật kiểm thử | Automation? | Ghi chú |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **RULE-SEARCH-01** | Tìm kiếm theo từ khóa (Keyword Search): Người dùng nhập từ khóa tìm kiếm (theo chức danh, kỹ năng, hoặc tên công ty). Hệ thống thực hiện tìm kiếm không phân biệt chữ hoa/thường, hỗ trợ cả tiếng Việt có dấu và không dấu, trả về danh sách công việc phù hợp có tiêu đề, mô tả hoặc tag kỹ năng chứa từ khóa. | `TCOND-SEARCH-01`, `TCOND-SEARCH-02`, `TCOND-SEARCH-03` | Equivalence Partitioning / Error Guessing | ✅ Yes | Ví dụ: "react", "ReactJS", "Frontend" |
| **RULE-SEARCH-02** | Lọc đa tiêu chí kết hợp (Multi-criteria Filter - AND Logic): Cho phép người dùng lọc kết hợp đồng thời theo: Ngành nghề/Lĩnh vực (Category), Địa điểm (Location: Hà Nội, TP.HCM, Đà Nẵng, Remote), Mức lương tối thiểu (Salary range), và Loại hình công việc (Full-time, Part-time, Internship). Kết quả trả về phải thỏa mãn đồng thời tất cả các tiêu chí đang được áp dụng. | `TCOND-SEARCH-04`, `TCOND-SEARCH-05`, `TCOND-SEARCH-06` | Decision Table | ✅ Yes | Tổ hợp logic lọc nhiều cấp |
| **RULE-SEARCH-03** | Trạng thái hiển thị khi không tìm thấy kết quả (Empty State): Khi từ khóa tìm kiếm hoặc tổ hợp bộ lọc không khớp với bất kỳ công việc nào trong hệ thống, giao diện phải hiển thị thông điệp thân thiện *"Không tìm thấy việc làm phù hợp"* kèm nút "Xóa bộ lọc" / "Xem tất cả việc làm", không để màn hình trắng hay lỗi giao diện. | `TCOND-SEARCH-07`, `TCOND-SEARCH-08` | Equivalence Partitioning / Error Guessing | ✅ Yes | Nhập từ khóa vô nghĩa như `xyz999nonexist` |
| **RULE-SEARCH-04** | Đặt lại bộ lọc (Reset / Clear All Filters): Khi người dùng bấm nút "Xóa bộ lọc" hoặc "Đặt lại", toàn bộ từ khóa trong ô tìm kiếm và các dropdown bộ lọc sẽ được trả về trạng thái mặc định (Tất cả), danh sách công việc lập tức nạp lại đầy đủ các công việc khả dụng ban đầu. | `TCOND-SEARCH-09` | State Transition | ✅ Yes | Đảm bảo tính tiện dụng (Usability) |
| **RULE-SEARCH-05** | Ràng buộc hiển thị công việc khả dụng (Job Visibility Constraint): Ứng viên chỉ được xem các tin tuyển dụng đang ở trạng thái hiển thị (`is_visible = true`) và đã được Admin phê duyệt (`verification_status = APPROVED`). Các tin tuyển dụng đã bị xóa mềm hoặc đang chờ duyệt tuyệt đối không được xuất hiện trong kết quả tìm kiếm (`BR-07`). | `TCOND-SEARCH-10`, `TCOND-SEARCH-11` | Decision Table / State Transition | ✅ Yes | Tuân thủ triệt để Business Rule `BR-07` |

### 4.2. Bảng quyết định kiểm thử tìm kiếm & bộ lọc (Decision Table - M3)

| Điều kiện kiểm thử (Conditions) | S1 | S2 | S3 | S4 | S5 | S6 | S7 |
|---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **Từ khóa tìm kiếm (Keyword)** | "Java" | "React" | Không nhập | Không nhập | "XYZ_NON_EXIST" | "Java" | Bất kỳ |
| **Địa điểm (Location)** | Tất cả | TP.HCM | Hà Nội | Đà Nẵng | Tất cả | Hà Nội | Đã chọn |
| **Loại hình (Job Type)** | Tất cả | Full-time | Internship | Tất cả | Tất cả | Full-time | Đã chọn |
| **Trạng thái duyệt của Job** | APPROVED | APPROVED | APPROVED | APPROVED | APPROVED | REJECTED/HIDDEN | APPROVED |
| **Hành động (Expected Results)** | | | | | | | |
| Trả về danh sách việc làm khớp tiêu đề/tag | **X** | **X** | | | | | |
| Trả về danh sách việc làm khớp địa điểm & loại hình | | **X** | **X** | **X** | | | **X** |
| Hiển thị thông báo Empty State ("Không tìm thấy") | | | | | **X** | | |
| Loại trừ không hiển thị Job ẩn / chưa duyệt | | | | | | **X** | |
| Bấm "Reset": Phục hồi danh sách ban đầu | | | | | | | **X** |

---

## 5. MODULE 4 (M4): ỨNG TUYỂN & THEO DÕI HỒ SƠ (APPLICATION & TRACKING)
* **Người phụ trách:** Lã Trọng Huy (`TrongHuy317`)
* **Endpoint / Giao diện kiểm thử:** `/jobs/:id` (Trang chi tiết việc làm & Modal nộp hồ sơ), `/candidate/applications` (Trang theo dõi trạng thái ứng tuyển dạng Kanban/Grouped List).
* **Testable UI Scope:** Kiểm tra luồng ứng tuyển việc làm bằng hồ sơ trực tuyến/CV, ngăn chặn ứng tuyển trùng lặp, xác thực quyền ứng tuyển theo vai trò, theo dõi phân nhóm trạng thái hồ sơ (Kanban 5 cột), và kiểm tra thông báo in-app khi có cập nhật trạng thái phỏng vấn.

### 5.1. Bảng Business Rules (M4)

| BR ID | Mô tả Quy tắc nghiệp vụ (Business Rule) | Test Condition ID | Kỹ thuật kiểm thử | Automation? | Ghi chú |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **RULE-APPLY-01** | Ứng tuyển thành công với hồ sơ/CV đã có: Ứng viên (CANDIDATE) đã đăng nhập và đã tải lên CV trong hồ sơ cá nhân có thể bấm nút "Ứng tuyển ngay" trên trang chi tiết công việc. Sau khi xác nhận nộp hồ sơ, đơn ứng tuyển được tạo với trạng thái khởi tạo `status = SUBMITTED`, thời gian nộp bằng thời gian hiện tại (`NOW()`). | `TCOND-APPLY-01`, `TCOND-APPLY-02` | State Transition / Decision Table | ✅ Yes | Tuân thủ yêu cầu có CV theo `BR-02` |
| **RULE-APPLY-02** | Chặn ứng tuyển trùng lặp (Duplicate Application Constraint): Một ứng viên chỉ được nộp hồ sơ **1 lần duy nhất** vào cùng một tin tuyển dụng. Nếu ứng viên đã ứng tuyển công việc đó trước đó, nút "Ứng tuyển ngay" sẽ đổi thành nhãn "Đã ứng tuyển" và bị disable, ngăn chặn hành động gửi lại yêu cầu. | `TCOND-APPLY-03` | State Transition / Decision Table | ✅ Yes | Tránh spam đơn tuyển dụng |
| **RULE-APPLY-03** | Ràng buộc phân quyền nộp đơn (Role & Auth Check): Chỉ tài khoản có vai trò `CANDIDATE` mới có quyền nộp đơn ứng tuyển. Nếu người dùng chưa đăng nhập, hệ thống yêu cầu chuyển hướng về `/login`. Nếu đăng nhập bằng tài khoản `EMPLOYER` hoặc `ADMIN`, nút ứng tuyển sẽ bị ẩn hoặc vô hiệu hóa. | `TCOND-APPLY-04`, `TCOND-APPLY-05` | Decision Table | ✅ Yes | Đảm bảo tính toàn vẹn phân quyền |
| **RULE-APPLY-04** | Theo dõi trạng thái ứng tuyển dạng Grouped List (Kanban Tracking): Trang theo dõi đơn tuyển dụng của ứng viên hiển thị các đơn ứng tuyển được gom nhóm theo 5 cột trạng thái: **Đã nộp (`SUBMITTED`) -> Đã xem (`VIEWED`) -> Phỏng vấn (`INTERVIEW`) -> Offer (`OFFER`) -> Từ chối (`REJECTED`)**. Giao diện này hoàn toàn là **Read-Only** đối với ứng viên (không cho phép ứng viên tự kéo thả chuyển trạng thái theo Quyết định thiết kế #8). | `TCOND-APPLY-06`, `TCOND-APPLY-07`, `TCOND-APPLY-08` | State Transition / Decision Table | ✅ Yes | Quyết định thiết kế #8 (Grouped List view) |
| **RULE-APPLY-05** | Nhận thông báo In-app khi cập nhật trạng thái phỏng vấn: Khi Nhà tuyển dụng chuyển trạng thái đơn sang "Phỏng vấn" (`INTERVIEW`), hệ thống bắt buộc nhập ghi chú phỏng vấn (thời gian, hình thức, link họp online theo `BR-09`). Ngay sau đó, một bản ghi thông báo in-app được tự động sinh ra trong bảng `notifications` (`BR-10`) và hiển thị thông báo mới trên biểu tượng chuông (🔔) của ứng viên. | `TCOND-APPLY-09`, `TCOND-APPLY-10`, `TCOND-APPLY-11` | State Transition / Decision Table | ✅ Yes | Tuân thủ nghiêm ngặt `BR-09` & `BR-10` |

### 5.2. Máy trạng thái vòng đời đơn ứng tuyển (State Machine - M4)

```
[Candidate nộp đơn] ────► SUBMITTED (Đã nộp)
                                │
                    [Employer mở xem hồ sơ]
                                ▼
                             VIEWED (Đã xem)
                                │
                    ┌───────────┴───────────┐
      [Hồ sơ phù hợp]                       [Không phù hợp]
            ▼                                     ▼
      INTERVIEW (Phỏng vấn)                 REJECTED (Từ chối)
  (Bắt buộc ghi chú: giờ/link họp)
            │
      ┌─────┴─────┐
[Đạt] ▼           ▼ [Không đạt]
    OFFER      REJECTED (Từ chối)
```

---

## 6. BẢNG TỔNG HỢP TOÀN BỘ BUSINESS RULES HỆ THỐNG

| Module | Tên Module | Mã tiền tố | Số lượng BRs | Số lượng TCONDs | Tỷ lệ Tự động hóa |
|:---:|---|:---:|:---:|:---:|:---:|
| **M1** | Đăng nhập & Phân quyền | `RULE-LOGIN-xx` | 6 | 12 | 100% |
| **M2** | Đăng ký tài khoản | `RULE-REG-xx` | 5 | 11 | 100% |
| **M3** | Tìm kiếm & Lọc việc làm | `RULE-SEARCH-xx` | 5 | 11 | 100% |
| **M4** | Ứng tuyển & Theo dõi hồ sơ | `RULE-APPLY-xx` | 5 | 11 | 100% |
| **TỔNG**| **Toàn bộ 4 Module** | | **21 Rules** | **45 Conditions** | **100% (45/45)** |

---
*Tài liệu BM02 được chuẩn hóa theo định dạng ISO/IEC/IEEE 29119-3 và làm căn cứ gốc cho việc lập Ma trận truy vết RTM (BM04).*
