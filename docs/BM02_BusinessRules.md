# BM02 – TÀI LIỆU BUSINESS RULES & TEST CONDITIONS (ĐẶC TẢ NGHIỆP VỤ & ĐIỀU KIỆN KIỂM THỬ)

**Dự án:** CareerFit – Hệ thống Kết nối Tuyển dụng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Tác giả (Test Lead):** Nguyễn Lâm Thi (`nguyenlamthi`)  
**Phiên bản:** 1.0  
**Ngày cập nhật:** 01/11/2026  

---

## 1. TỔNG QUAN

Tài liệu này xác định các quy tắc nghiệp vụ (Business Rules - BR) và các điều kiện kiểm thử (Test Conditions - TCOND) suy biến từ yêu cầu hệ thống (SRS/Acceptance Criteria).  
Các điều kiện kiểm thử được thiết kế dựa trên các kỹ thuật kiểm thử hộp đen chuẩn ISTQB:
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

## 3. KHUNG THEO DÕI CHO CÁC MODULE TIẾP THEO (M2, M3, M4)

*(Các thành viên phụ trách sẽ cập nhật chi tiết trong các nhánh tương ứng theo phân công của nhóm)*

* **MODULE 2 (M2): ĐĂNG KÝ TÀI KHOẢN (REGISTRATION)**  
  * **Người phụ trách:** Võ Nguyên Khánh (`richnk123a-cyber`)  
  * **Quy tắc chính:** `RULE-REG-01` (Đăng ký Ứng viên), `RULE-REG-02` (Đăng ký Doanh nghiệp kèm tải lên GPKD), `RULE-REG-03` (Trùng lặp email), `RULE-REG-04` (Validate mật khẩu mạnh).
* **MODULE 3 (M3): TÌM KIẾM & LỌC VIỆC LÀM (JOB SEARCH & FILTER)**  
  * **Người phụ trách:** Nguyễn Trọng Tiến (`tiendeptroai`)  
  * **Quy tắc chính:** `RULE-SEARCH-01` (Tìm kiếm từ khóa), `RULE-SEARCH-02` (Lọc theo ngành nghề/địa điểm/mức lương), `RULE-SEARCH-03` (Không tìm thấy kết quả).
* **MODULE 4 (M4): ỨNG TUYỂN & THEO DÕI HỒ SƠ (APPLICATION & TRACKING)**  
  * **Người phụ trách:** Lã Trọng Huy (`TrongHuy317`)  
  * **Quy tắc chính:** `RULE-APPLY-01` (Nộp hồ sơ ứng tuyển kèm CV), `RULE-APPLY-02` (Chặn ứng tuyển trùng lặp), `RULE-APPLY-03` (Theo dõi trạng thái hồ sơ).
