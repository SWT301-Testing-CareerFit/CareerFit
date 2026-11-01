# BM02 – TÀI LIỆU BUSINESS RULES & TEST CONDITIONS (ĐẶC TẢ NGHIỆP VỤ & ĐIỀU KIỆN KIỂM THỬ)

**Dự án:** CareerFit – Hệ thống Kết nối Tuyển dụng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Tác giả:** Nhóm Kiểm thử CareerFit (Nguyễn Lâm Thi - M1, Võ Nguyên Khánh - M2, Nguyễn Trọng Tiến - M3, Lã Trọng Huy - M4)  
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

## 3. MODULE 4 (M4): ỨNG TUYỂN VIỆC LÀM & THEO DÕI TRẠNG THÁI ĐƠN TRÊN KANBAN
* **Người phụ trách (Module Owner):** Lã Trọng Huy (`TrongHuy317` - SV4)
* **Endpoint / Giao diện kiểm thử:** `/jobs/:id` (Chi tiết tin tuyển dụng) và `/candidate/applications` (Bảng Kanban theo dõi đơn ứng tuyển)
* **Testable UI Scope:** Toàn bộ luồng tương tác của Ứng viên (Candidate) đối với tin tuyển dụng (ứng tuyển, hủy đơn, lưu bookmark) và theo dõi trạng thái đơn trên Kanban.

### 3.1. Bảng Business Rules (M4)

| BR ID | Tên quy tắc nghiệp vụ | Mô tả quy tắc kiểm thử được (Testable Statement) | Điều kiện tiên quyết & Dữ liệu | Kết quả mong đợi (Expected Result) | Auto? |
|---|---|---|---|---|:---:|
| **RULE-APP-01** | Yêu cầu Đăng nhập & Quyền hạn ứng tuyển | Khi người dùng chưa đăng nhập bấm "Ứng tuyển ngay" tại `/jobs/:id`, hệ thống phải chặn thao tác, thông báo yêu cầu đăng nhập và chuyển hướng sang `/login`. Nếu đăng nhập với vai trò khác CANDIDATE (ví dụ EMPLOYER), hệ thống hiển thị thông báo lỗi quyền hạn và không cho nộp đơn. | - Người dùng là Guest hoặc Employer.<br>- Tin tuyển dụng công khai hợp lệ. | - Chưa đăng nhập: Toast thông báo *"Vui lòng đăng nhập tài khoản Ứng viên để nộp đơn!"*, chuyển sang `/login`.<br>- Sai vai trò: Toast *"Chỉ tài khoản Ứng viên mới có thể nộp đơn ứng tuyển!"*. | ✅ Có |
| **RULE-APP-02** | Ràng buộc Hồ sơ & Tệp CV ứng viên | Ứng viên đã đăng nhập chỉ được phép nộp đơn khi đã có tệp CV (PDF/DOCX) trong hồ sơ cá nhân (`hasCV = true`). Nếu chưa có CV, hệ thống phải chặn nộp đơn, hiển thị cảnh báo và chuyển hướng sang `/candidate-profile`. Tài khoản bị Admin khóa (`status = 'LOCKED'`) cũng bị chặn hoàn toàn. | - Candidate đã đăng nhập.<br>- Case A: Chưa upload CV (`cvFileName = null`).<br>- Case B: Tài khoản bị khóa (`status = LOCKED`). | - Chưa có CV: Toast *"⚠️ Vui lòng tải lên file CV (PDF/DOCX) trong Hồ sơ & CV trước khi nộp đơn ứng tuyển!"*, điều hướng sang `/candidate-profile`.<br>- Tài khoản khóa: Toast *"Tài khoản của bạn đã bị khóa bởi Quản trị viên. Không thể ứng tuyển!"*. | ✅ Có |
| **RULE-APP-03** | Chuyển đổi trạng thái Nộp đơn (State Transition) | Khi Candidate thỏa mãn mọi điều kiện bấm "Ứng tuyển ngay", hệ thống tạo đơn ứng tuyển mới với trạng thái mặc định là `Đã nộp`. Nút bấm trên giao diện ngay lập tức chuyển từ `[Ứng tuyển ngay]` sang `[✓ Đã ứng tuyển — Hủy]`. | - Candidate đăng nhập, đã có CV hợp lệ.<br>- Chưa từng nộp đơn vào job này. | - Toast *"Ứng tuyển thành công! Nhà tuyển dụng đã nhận hồ sơ của bạn."* xuất hiện.<br>- Nút chuyển thành `✓ Đã ứng tuyển — Hủy`.<br>- Tạo 1 record đơn mới với status = 'Đã nộp'. | ✅ Có |
| **RULE-APP-04** | Chặn nộp đơn trùng lặp (Duplicate Prevention) | Mỗi ứng viên chỉ được nộp tối đa 01 đơn ứng tuyển cho một tin tuyển dụng cụ thể. Khi đã nộp, nút ứng tuyển hiển thị trạng thái đã ứng tuyển; nếu kích hoạt lại lệnh nộp đơn, hệ thống phải chặn và báo lỗi đã nộp. | - Candidate đã nộp đơn trước đó cho job này.<br>- Thao tác nộp lại lần 2. | - Hệ thống không sinh thêm đơn trùng trong cơ sở dữ liệu.<br>- Toast hiển thị: *"Bạn đã nộp đơn ứng tuyển cho công việc này rồi!"*. | ✅ Có |
| **RULE-APP-05** | Chuyển đổi trạng thái Hủy đơn (State Transition) | Khi ứng viên đang ở trạng thái `[Đã ứng tuyển]`, bấm vào nút `[— Hủy]`, hệ thống lập tức hủy đơn ứng tuyển. Nút bấm trên giao diện lập tức quay về trạng thái ban đầu `[Ứng tuyển ngay]`. | - Candidate đã nộp đơn thành công trước đó.<br>- Đang ở trang chi tiết `/jobs/:id`. | - Toast *"Đã hủy đơn ứng tuyển thành công."* xuất hiện.<br>- Nút bấm đổi lại thành `Ứng tuyển ngay`.<br>- Đơn ứng tuyển bị xóa khỏi danh sách ứng tuyển của candidate. | ✅ Có |
| **RULE-APP-06** | Đồng bộ Bảng Kanban & Quản lý Bookmark (Lưu tin) | - **Đồng bộ Kanban:** Khi nộp đơn thành công, truy cập `/candidate/applications` đơn phải xuất hiện tại cột/nhóm `Đã nộp hồ sơ`. Khi hủy đơn, đơn phải biến mất hoàn toàn khỏi Kanban.<br>- **Bookmark:** Bấm nút "Lưu tin" chuyển sang trạng thái "Đã lưu tin" (icon fill), bấm lần nữa quay lại "Lưu tin" (icon unfill). | - Candidate đã đăng nhập.<br>- Thực hiện nộp/hủy đơn và bật/tắt lưu tin. | - Kanban `/candidate/applications`: Đơn hiển thị đúng cột `Đã nộp`, ngày nộp và tên công ty; khi hủy thì biến mất.<br>- Nút Lưu tin: Toggle đúng văn bản và trạng thái active `is-saved`. | ✅ Có |

### 3.2. Bảng quyết định kiểm thử (Decision Table - M4)

| Điều kiện (Conditions) | R1 | R2 | R3 | R4 | R5 | R6 |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| Người dùng đã đăng nhập? | **N** | **Y** | **Y** | **Y** | **Y** | **Y** |
| Vai trò tài khoản là `CANDIDATE`? | — | **N** (Employer) | **Y** | **Y** | **Y** | **Y** |
| Trạng thái tài khoản `ACTIVE` (không bị khóa)? | — | — | **N** (Locked) | **Y** | **Y** | **Y** |
| Hồ sơ ứng viên đã có CV đính kèm? | — | — | — | **N** (No CV) | **Y** | **Y** |
| Đã từng ứng tuyển vào job này trước đó? | — | — | — | — | **Y** (Đã nộp) | **N** (Chưa nộp) |
| **Hành động / Kết quả kỳ vọng (Actions)** | | | | | | |
| Chuyển hướng sang `/login` | **X** | | | | | |
| Báo lỗi quyền hạn (Không cho phép Employer) | | **X** | | | | |
| Báo lỗi tài khoản bị khóa (`LOCKED`) | | | **X** | | | |
| Báo lỗi thiếu CV, chuyển hướng `/candidate-profile` | | | | **X** | | |
| Chặn nộp trùng lặp, giữ trạng thái "Đã ứng tuyển" | | | | | **X** | |
| Nộp đơn thành công, nút đổi thành "✓ Đã ứng tuyển — Hủy" | | | | | | **X** |
| **Test Case ID tương ứng** | `TC-APP-001` | `TC-APP-002` | `TC-APP-003` | `TC-APP-004` | `TC-APP-006` | `TC-APP-005` |

### 3.3. Sơ đồ Chuyển đổi trạng thái (State Transition Testing - M4)

```
[S1: Chưa ứng tuyển] ──(Click "Ứng tuyển ngay")──> [S2: Đã ứng tuyển]
        ^                                                   │
        └─────────────────(Click "— Hủy")───────────────────┘
```

### 3.4. Ma trận Điều kiện kiểm thử (Test Conditions Matrix - M4)

| Test Condition ID | Mô tả điều kiện kiểm thử (Test Condition) | Kỹ thuật áp dụng | Ánh xạ Test Case ID |
|---|---|:---:|:---:|
| `TCOND-APP-01` | Kiểm tra chuyển hướng về trang `/login` khi Guest (chưa đăng nhập) bấm Ứng tuyển | Decision Table | `TC-APP-001` |
| `TCOND-APP-02` | Kiểm tra chặn ứng tuyển khi đăng nhập bằng tài khoản không phải Ứng viên (Employer) | Decision Table | `TC-APP-002` |
| `TCOND-APP-03` | Kiểm tra chặn ứng tuyển đối với tài khoản Ứng viên đang ở trạng thái bị khóa (`LOCKED`) | Decision Table | `TC-APP-003` |
| `TCOND-APP-04` | Kiểm tra cảnh báo thiếu CV và điều hướng sang trang cập nhật hồ sơ khi ứng viên chưa tải CV | Decision Table | `TC-APP-004` |
| `TCOND-APP-05` | Kiểm tra nộp đơn ứng tuyển thành công, đổi nhãn nút sang "✓ Đã ứng tuyển — Hủy" và toast thông báo | State Transition | `TC-APP-005` |
| `TCOND-APP-06` | Kiểm tra chặn nộp đơn trùng lặp khi ứng viên đã có hồ sơ đang xử lý cho tin tuyển dụng đó | Decision Table | `TC-APP-006` |
| `TCOND-APP-07` | Kiểm tra đơn ứng tuyển mới nộp xuất hiện đồng bộ chính xác tại cột "Đã nộp" trên bảng Kanban | State Transition | `TC-APP-007` |
| `TCOND-APP-08` | Kiểm tra hủy đơn ứng tuyển thành công tại trang chi tiết job, nút chuyển lại "Ứng tuyển ngay" | State Transition | `TC-APP-008` |
| `TCOND-APP-09` | Kiểm tra sau khi hủy đơn ứng tuyển, đơn bị gỡ bỏ hoàn toàn khỏi giao diện Kanban | State Transition | `TC-APP-009` |
| `TCOND-APP-10` | Kiểm tra chuyển đổi trạng thái nút Bookmark từ "Lưu tin" sang "Đã lưu tin" | State Transition | `TC-APP-010` |
| `TCOND-APP-11` | Kiểm tra chuyển đổi trạng thái nút Bookmark từ "Đã lưu tin" trở về "Lưu tin" | State Transition | `TC-APP-011` |
| `TCOND-APP-12` | Kiểm tra nút ứng tuyển bị vô hiệu hóa ("Chưa mở ứng tuyển") khi tin chưa được duyệt hoặc bị ẩn | BVA / State | `TC-APP-012` |
| `TCOND-APP-13` | Kiểm tra chức năng tìm kiếm đơn ứng tuyển theo từ khóa tên việc làm / công ty trên Kanban | Equivalence Partitioning | `TC-APP-013` |
| `TCOND-APP-14` | Kiểm tra chuyển đổi chế độ xem giữa dạng Danh sách (List) và dạng Bảng (Board/Kanban) | State Transition | `TC-APP-014` |

---

## 4. KHUNG THEO DÕI CHO CÁC MODULE TIẾP THEO (M2, M3)

* **MODULE 2 (M2): ĐĂNG KÝ TÀI KHOẢN (REGISTRATION)**  
  * **Người phụ trách:** Võ Nguyên Khánh (`richnk123a-cyber`)  
  * **Quy tắc chính:** `RULE-REG-01` (Đăng ký Ứng viên), `RULE-REG-02` (Đăng ký Doanh nghiệp kèm tải lên GPKD), `RULE-REG-03` (Trùng lặp email), `RULE-REG-04` (Validate mật khẩu mạnh).
* **MODULE 3 (M3): TÌM KIẾM & LỌC VIỆC LÀM (JOB SEARCH & FILTER)**  
  * **Người phụ trách:** Nguyễn Trọng Tiến (`tiendeptroai`)  
  * **Quy tắc chính:** `RULE-SEARCH-01` (Tìm kiếm từ khóa), `RULE-SEARCH-02` (Lọc theo ngành nghề/địa điểm/mức lương), `RULE-SEARCH-03` (Không tìm thấy kết quả).
