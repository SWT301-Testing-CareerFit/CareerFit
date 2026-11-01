# BM04 – MA TRẬN TRUY VẾT YÊU CẦU (REQUIREMENTS TRACEABILITY MATRIX - RTM)

**Dự án:** CareerFit – Nền tảng Tuyển dụng IT & Định hướng Kỹ năng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Tiêu chuẩn áp dụng:** ISO/IEC/IEEE 29119-3 & ISTQB CTFL v4.0  
**Phiên bản:** 2.0 (Hợp nhất toàn diện 4 Module: M1, M2, M3, M4)  
**Ngày cập nhật:** 01/11/2026  
**Chịu trách nhiệm tài liệu:** Nguyễn Trọng Tiến (`tiendeptroai` - Documentation Owner) & Nguyễn Lâm Thi (`nguyenlamthi` - Test Lead)  

---

## 1. MỤC TIÊU MA TRẬN TRUY VẾT (RTM OBJECTIVES)

Ma trận truy vết yêu cầu (RTM) là tài liệu cốt lõi chứng minh chất lượng kiểm thử của dự án, đảm bảo:
1. **Độ bao phủ yêu cầu 100% (100% Coverage):** Mọi quy tắc nghiệp vụ (**Business Rule - BR**) suy biến từ tài liệu yêu cầu (SRS) đều được kiểm thử thông qua ít nhất một **Test Condition (TCOND)** và một **Test Case (TC)** tương ứng.
2. **Truy vết hai chiều (Bidirectional Traceability):**
   * *Forward Traceability:* Từ Business Rule -> Test Condition -> Test Case -> Kịch bản tự động (Automation Method) -> Kết quả thực thi.
   * *Backward Traceability:* Từ khi phát hiện một Defect trên giao diện -> truy ngược lại mã Test Case và Quy tắc nghiệp vụ bị vi phạm.
3. **Theo dõi tỷ lệ tự động hóa (Automation Coverage):** 100% các Test Case chức năng cốt lõi đều được tự động hóa bằng Selenium 4 + JUnit 5 + POM.

---

## 2. MA TRẬN TRUY VẾT CHI TIẾT THEO TỪNG MODULE

### 2.1. Module 1 (M1): Đăng nhập & Phân quyền (Login & Authorization)
* **Người phụ trách:** Nguyễn Lâm Thi (`nguyenlamthi` - Test Lead)

| BR ID | Mô tả Business Rule | Test Condition ID | Danh sách Test Cases (TC IDs) | Kỹ thuật kiểm thử | Số TC Tự động | Tỷ lệ Tự động | Độ bao phủ |
| :--- | :--- | :--- | :--- | :--- | :---: | :---: | :---: |
| **RULE-LOGIN-01** | Đăng nhập thành công với thông tin hợp lệ & phân quyền đúng Role (Candidate -> `/jobs`, Employer -> `/employer`, Admin -> `/admin`) | `TCOND-LOGIN-01`<br>`TCOND-LOGIN-02`<br>`TCOND-LOGIN-03` | `TC-LOGIN-001`<br>`TC-LOGIN-002`<br>`TC-LOGIN-003` | Decision Table | 3 / 3 | 100% | **100%** |
| **RULE-LOGIN-02** | Đăng nhập thất bại khi sai email hoặc mật khẩu (Bảo mật: thông báo chung, không lộ trường sai) | `TCOND-LOGIN-04`<br>`TCOND-LOGIN-05` | `TC-LOGIN-004`<br>`TC-LOGIN-005` | Decision Table / Error Guessing | 2 / 2 | 100% | **100%** |
| **RULE-LOGIN-03** | Ràng buộc bắt buộc (Required fields) & Định dạng Email hợp lệ | `TCOND-LOGIN-06`<br>`TCOND-LOGIN-07`<br>`TCOND-LOGIN-08`<br>`TCOND-LOGIN-09` | `TC-LOGIN-006`<br>`TC-LOGIN-007`<br>`TC-LOGIN-008`<br>`TC-LOGIN-009` | Equivalence Partitioning | 4 / 4 | 100% | **100%** |
| **RULE-LOGIN-04** | Mật khẩu phân biệt chữ hoa và chữ thường (Case-sensitivity) | `TCOND-LOGIN-10` | `TC-LOGIN-010` | Equivalence Partitioning | 1 / 1 | 100% | **100%** |
| **RULE-LOGIN-05** | Từ chối đăng nhập với tài khoản bị khóa (LOCKED) hoặc chưa kích hoạt | `TCOND-LOGIN-11` | `TC-LOGIN-011` | Decision Table / State Transition | 1 / 1 | 100% | **100%** |
| **RULE-LOGIN-06** | Đăng xuất (Logout) và xóa sạch phiên làm việc / token | `TCOND-LOGIN-12` | `TC-LOGIN-012` | State Transition | 1 / 1 | 100% | **100%** |
| **TIỂU MỤC M1** | **6 Business Rules** | **12 Conditions** | **12 Test Cases** | | **12 / 12** | **100%** | **100%** |

---

### 2.2. Module 2 (M2): Đăng ký tài khoản (User Registration)
* **Người phụ trách:** Võ Nguyên Khánh (`richnk123a-cyber` - Framework Owner)

| BR ID | Mô tả Business Rule | Test Condition ID | Danh sách Test Cases (TC IDs) | Kỹ thuật kiểm thử | Số TC Tự động | Tỷ lệ Tự động | Độ bao phủ |
| :--- | :--- | :--- | :--- | :--- | :---: | :---: | :---: |
| **RULE-REG-01** | Đăng ký tài khoản Ứng viên (CANDIDATE) thành công với thông tin hợp lệ | `TCOND-REG-01`<br>`TCOND-REG-02` | `TC-REG-001` | Equivalence Partitioning | 1 / 1 | 100% | **100%** |
| **RULE-REG-02** | Đăng ký tài khoản Doanh nghiệp (EMPLOYER) kèm Giấy phép kinh doanh, trạng thái mặc định `UNVERIFIED` | `TCOND-REG-03` | `TC-REG-003`<br>`TC-REG-011` | Decision Table / State Transition | 2 / 2 | 100% | **100%** |
| **RULE-REG-03** | Ràng buộc tính duy nhất của Email (Chặn đăng ký trùng email đã có) | `TCOND-REG-04` | `TC-REG-004` | Equivalence Partitioning | 1 / 1 | 100% | **100%** |
| **RULE-REG-04** | Ràng buộc độ phức tạp mật khẩu (Tối thiểu 8 ký tự, có hoa/thường/số/ký tự đặc biệt) & Khớp xác nhận | `TCOND-REG-05`<br>`TCOND-REG-06`<br>`TCOND-REG-07`<br>`TCOND-REG-08` | `TC-REG-002`<br>`TC-REG-005`<br>`TC-REG-006`<br>`TC-REG-007` | Boundary Value Analysis / EP | 4 / 4 | 100% | **100%** |
| **RULE-REG-05** | Ràng buộc trường bắt buộc, định dạng Email hợp lệ, chặn họ tên toàn khoảng trắng | `TCOND-REG-09`<br>`TCOND-REG-10`<br>`TCOND-REG-11` | `TC-REG-008`<br>`TC-REG-009`<br>`TC-REG-010` | Equivalence Partitioning / Error Guessing | 3 / 3 | 100% | **100%** |
| **TIỂU MỤC M2** | **5 Business Rules** | **11 Conditions** | **11 Test Cases** | | **11 / 11** | **100%** | **100%** |

---

### 2.3. Module 3 (M3): Tìm kiếm & Lọc việc làm (Job Search & Filter)
* **Người phụ trách:** Nguyễn Trọng Tiến (`tiendeptroai` - Documentation Owner)

| BR ID | Mô tả Business Rule | Test Condition ID | Danh sách Test Cases (TC IDs) | Kỹ thuật kiểm thử | Số TC Tự động | Tỷ lệ Tự động | Độ bao phủ |
| :--- | :--- | :--- | :--- | :--- | :---: | :---: | :---: |
| **RULE-SEARCH-01** | Tìm kiếm theo từ khóa (chức danh, tag kỹ năng), không phân biệt hoa thường, hỗ trợ tiếng Việt | `TCOND-SEARCH-01`<br>`TCOND-SEARCH-02`<br>`TCOND-SEARCH-03` | `TC-SEARCH-001`<br>`TC-SEARCH-002`<br>`TC-SEARCH-003`<br>`TC-SEARCH-011` | Equivalence Partitioning | 4 / 4 | 100% | **100%** |
| **RULE-SEARCH-02** | Lọc đa tiêu chí kết hợp (Địa điểm, Loại hình, Ngành nghề) theo logic giao (AND Logic) | `TCOND-SEARCH-04`<br>`TCOND-SEARCH-05`<br>`TCOND-SEARCH-06` | `TC-SEARCH-004`<br>`TC-SEARCH-005`<br>`TC-SEARCH-006` | Decision Table | 3 / 3 | 100% | **100%** |
| **RULE-SEARCH-03** | Hiển thị giao diện Trạng thái trống (Empty State) khi không tìm thấy kết quả hoặc ký tự lạ | `TCOND-SEARCH-07`<br>`TCOND-SEARCH-08` | `TC-SEARCH-007`<br>`TC-SEARCH-008` | Error Guessing | 2 / 2 | 100% | **100%** |
| **RULE-SEARCH-04** | Nút Đặt lại bộ lọc (Reset Filter) khôi phục danh sách đầy đủ ban đầu | `TCOND-SEARCH-09` | `TC-SEARCH-009` | State Transition | 1 / 1 | 100% | **100%** |
| **RULE-SEARCH-05** | Ràng buộc hiển thị tin tuyển dụng: Ẩn tin chưa duyệt hoặc bị xóa mềm vi phạm (`BR-07`) | `TCOND-SEARCH-10`<br>`TCOND-SEARCH-11` | `TC-SEARCH-010` | Decision Table / State Transition | 1 / 1 | 100% | **100%** |
| **TIỂU MỤC M3** | **5 Business Rules** | **11 Conditions** | **11 Test Cases** | | **11 / 11** | **100%** | **100%** |

---

### 2.4. Module 4 (M4): Ứng tuyển & Theo dõi hồ sơ (Application & Tracking)
* **Người phụ trách:** Lã Trọng Huy (`TrongHuy317` - Defect Manager)

| BR ID | Mô tả Business Rule | Test Condition ID | Danh sách Test Cases (TC IDs) | Kỹ thuật kiểm thử | Số TC Tự động | Tỷ lệ Tự động | Độ bao phủ |
| :--- | :--- | :--- | :--- | :--- | :---: | :---: | :---: |
| **RULE-APPLY-01** | Ứng viên đã có CV nộp hồ sơ thành công; Cảnh báo chặn nộp nếu chưa có CV (`BR-02`) | `TCOND-APPLY-01`<br>`TCOND-APPLY-02` | `TC-APPLY-001`<br>`TC-APPLY-002` | State Transition / Decision Table | 2 / 2 | 100% | **100%** |
| **RULE-APPLY-02** | Chặn ứng tuyển trùng lặp vào cùng 1 tin tuyển dụng (nút đổi sang "Đã ứng tuyển" & disabled) | `TCOND-APPLY-03` | `TC-APPLY-003` | State Transition | 1 / 1 | 100% | **100%** |
| **RULE-APPLY-03** | Phân quyền nộp đơn: Chuyển hướng người dùng ẩn danh về `/login`; Chặn Employer ứng tuyển | `TCOND-APPLY-04`<br>`TCOND-APPLY-05` | `TC-APPLY-004`<br>`TC-APPLY-005` | Decision Table | 2 / 2 | 100% | **100%** |
| **RULE-APPLY-04** | Theo dõi trạng thái ứng tuyển dạng Grouped List Kanban 5 cột; Giao diện Read-only (QĐ #8) | `TCOND-APPLY-06`<br>`TCOND-APPLY-07`<br>`TCOND-APPLY-08` | `TC-APPLY-006`<br>`TC-APPLY-007`<br>`TC-APPLY-008` | Equivalence Partitioning / ST | 3 / 3 | 100% | **100%** |
| **RULE-APPLY-05** | Nhận thông báo In-app và hiển thị thông tin phỏng vấn (thời gian, link họp) khi Employer cập nhật (`BR-09`, `BR-10`) | `TCOND-APPLY-09`<br>`TCOND-APPLY-10`<br>`TCOND-APPLY-11` | `TC-APPLY-009`<br>`TC-APPLY-010`<br>`TC-APPLY-011` | State Transition | 3 / 3 | 100% | **100%** |
| **TIỂU MỤC M4** | **5 Business Rules** | **11 Conditions** | **11 Test Cases** | | **11 / 11** | **100%** | **100%** |

---

## 3. BẢNG TỔNG HỢP VÀ ĐÁNH GIÁ ĐỘ BAO PHỦ TOÀN DỰ ÁN

| Chỉ số đánh giá chất lượng (Metric) | Kết quả đạt được | Mục tiêu đề ra | Trạng thái |
|:--- | :---: | :---: | :---: |
| **Tổng số Business Rules được định nghĩa** | **21 Rules** | >= 18 Rules | ✅ Đạt |
| **Tổng số Điều kiện kiểm thử (Test Conditions)** | **45 Conditions** | >= 40 Conditions | ✅ Đạt |
| **Tổng số Test Cases thiết kế (Test Cases)** | **45 Cases** | >= 40 Cases | ✅ Đạt |
| **Số Test Cases được tự động hóa (Automated TCs)** | **45 Cases** | >= 36 Cases (80%) | ✅ Vượt mục tiêu (100%) |
| **Tỷ lệ bao phủ Business Rules (Rule Coverage)** | **100% (21/21 Rules)** | 100% | ✅ Hoàn thành xuất sắc |
| **Tỷ lệ bao phủ Điều kiện kiểm thử (Condition Coverage)** | **100% (45/45 Conditions)** | 100% | ✅ Hoàn thành xuất sắc |

---

## 4. KẾT LUẬN & CAM KẾT TRUY VẾT

1. **Tính hoàn thiện:** 100% các quy tắc nghiệp vụ quan trọng từ đăng ký, đăng nhập, tìm kiếm việc làm đến ứng tuyển và theo dõi trạng thái đều có Test Case kiểm thử tương ứng.
2. **Tính nhất quán:** Toàn bộ mã định danh (`RULE-xxx`, `TCOND-xxx`, `TC-xxx`) đồng bộ tuyệt đối giữa 4 tài liệu: `BM02_BusinessRules.md`, `BM03_TestCases.md`, `BM03_TestCases.xlsx`, và `BM04_RTM.md`.
3. **Độ sẵn sàng kiểm thử:** Bộ ma trận RTM này là cơ sở trực tiếp để chạy kiểm thử hồi quy tự động, báo cáo Defect (BM07), và nghiệm thu hoàn thành Milestone `W3-Closure`.

---
*Tài liệu Ma trận RTM BM04 được ban hành chính thức theo tiêu chuẩn ISO/IEC/IEEE 29119-3.*
