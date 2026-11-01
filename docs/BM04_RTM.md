# BM04 – MA TRẬN TRUY VẾT YÊU CẦU (REQUIREMENTS TRACEABILITY MATRIX - RTM)

**Dự án:** CareerFit – Hệ thống Kết nối Tuyển dụng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Tác giả:** Nhóm Kiểm thử CareerFit (Nguyễn Lâm Thi - M1, Võ Nguyên Khánh - M2, Nguyễn Trọng Tiến - M3, Lã Trọng Huy - M4)  
**Phiên bản:** 1.1 | **Ngày cập nhật:** 01/11/2026  

---

## 1. MỤC TIÊU MA TRẬN RTM

Ma trận truy vết (RTM) đảm bảo:
1. Mọi quy tắc nghiệp vụ (**Business Rules - BR**) trong phạm vi kiểm thử đều có ít nhất 1 **Test Condition (TCOND)** và 1 **Test Case (TC)** tương ứng (100% Requirement Coverage).
2. Xác định rõ tỷ lệ tự động hóa (**Automation Coverage**) trên từng quy tắc.
3. Liên kết hai chiều: Từ yêu cầu/quy tắc -> Test Case -> Kết quả chạy thực tế / Defect.

---

## 2. MA TRẬN TRUY VẾT MODULE 1 (M1): ĐĂNG NHẬP & PHÂN QUYỀN
* **Người phụ trách:** Nguyễn Lâm Thi (`nguyenlamthi` - Test Lead)

| BR ID | Mô tả Business Rule | Test Condition ID | Danh sách Test Cases (TC IDs) | Tổng số TC | Số TC Tự động | Pass | Fail | Not Run | Độ bao phủ (Coverage) |
| :--- | :--- | :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **RULE-LOGIN-01** | Đăng nhập thành công với thông tin hợp lệ & phân quyền theo vai trò (Candidate, Employer, Admin) | `TCOND-LOGIN-01`<br>`TCOND-LOGIN-02`<br>`TCOND-LOGIN-03` | `TC-LOGIN-001`<br>`TC-LOGIN-002`<br>`TC-LOGIN-003` | 3 | 3 (100%) | 0 | 0 | 3 | **100%** |
| **RULE-LOGIN-02** | Đăng nhập thất bại khi sai email hoặc mật khẩu (Bảo mật: không lộ trường sai) | `TCOND-LOGIN-04`<br>`TCOND-LOGIN-05` | `TC-LOGIN-004`<br>`TC-LOGIN-005` | 2 | 2 (100%) | 0 | 0 | 2 | **100%** |
| **RULE-LOGIN-03** | Ràng buộc bắt buộc (Required fields) & Định dạng Email hợp lệ | `TCOND-LOGIN-06`<br>`TCOND-LOGIN-07`<br>`TCOND-LOGIN-08`<br>`TCOND-LOGIN-09` | `TC-LOGIN-006`<br>`TC-LOGIN-007`<br>`TC-LOGIN-008`<br>`TC-LOGIN-009` | 4 | 4 (100%) | 0 | 0 | 4 | **100%** |
| **RULE-LOGIN-04** | Mật khẩu phân biệt chữ hoa và chữ thường (Case-sensitivity) | `TCOND-LOGIN-10` | `TC-LOGIN-010` | 1 | 1 (100%) | 0 | 0 | 1 | **100%** |
| **RULE-LOGIN-05** | Từ chối đăng nhập với tài khoản bị khóa hoặc chưa kích hoạt | `TCOND-LOGIN-11` | `TC-LOGIN-011` | 1 | 1 (100%) | 0 | 0 | 1 | **100%** |
| **RULE-LOGIN-06** | Đăng xuất (Logout) và xóa sạch phiên làm việc | `TCOND-LOGIN-12` | `TC-LOGIN-012` | 1 | 1 (100%) | 0 | 0 | 1 | **100%** |
| **TỔNG CỘNG M1** | **6 Business Rules** | **12 Test Conditions** | **12 Test Cases** | **12** | **12 (100%)** | **0** | **0** | **12** | **100%** |

---

## 3. MA TRẬN TRUY VẾT MODULE 4 (M4): ỨNG TUYỂN & THEO DÕI ĐƠN TRÊN KANBAN
* **Người phụ trách:** Lã Trọng Huy (`TrongHuy317` - SV4)

| Mã BR (Requirement) | Tên Business Rule | Test Condition ID & Tóm tắt | Danh sách Test Cases (TC IDs) | Số TC | Automated | Manual | Trạng thái (P/F/NR) | Coverage (%) |
|---|---|---|---|:---:|:---:|:---:|:---:|:---:|
| **RULE-APP-01** | Yêu cầu Đăng nhập & Quyền hạn ứng tuyển | - `TCOND-APP-01`: Guest bấm ứng tuyển chuyển `/login`<br>- `TCOND-APP-02`: Chặn Employer bấm ứng tuyển<br>- `TCOND-APP-12`: Tin chưa duyệt/ẩn vô hiệu hóa nút ứng tuyển | `TC-APP-001`<br>`TC-APP-002`<br>`TC-APP-012` | 3 | 3 | 0 | 3 / 0 / 0 | **100%** |
| **RULE-APP-02** | Ràng buộc Hồ sơ & Tệp CV ứng viên | - `TCOND-APP-03`: Chặn tài khoản bị khóa (`LOCKED`)<br>- `TCOND-APP-04`: Chưa có CV chuyển `/candidate-profile` | `TC-APP-003`<br>`TC-APP-004` | 2 | 2 | 0 | 2 / 0 / 0 | **100%** |
| **RULE-APP-03** | Chuyển đổi trạng thái Nộp đơn ứng tuyển | - `TCOND-APP-05`: Nộp đơn thành công, nút chuyển `✓ Đã ứng tuyển — Hủy`, tạo record status 'Đã nộp' | `TC-APP-005` | 1 | 1 | 0 | 1 / 0 / 0 | **100%** |
| **RULE-APP-04** | Chặn nộp đơn trùng lặp (Duplicate Prevention) | - `TCOND-APP-06`: Chặn submit lại lần 2 khi đã có đơn đang xử lý | `TC-APP-006` | 1 | 1 | 0 | 1 / 0 / 0 | **100%** |
| **RULE-APP-05** | Chuyển đổi trạng thái Hủy đơn ứng tuyển | - `TCOND-APP-08`: Hủy đơn thành công, nút quay lại `Ứng tuyển ngay`, xóa đơn khỏi hồ sơ | `TC-APP-008` | 1 | 1 | 0 | 1 / 0 / 0 | **100%** |
| **RULE-APP-06** | Đồng bộ Bảng Kanban & Quản lý Bookmark | - `TCOND-APP-07`: Đồng bộ đơn mới nộp vào cột 'Đã nộp'<br>- `TCOND-APP-09`: Đơn hủy biến mất khỏi Kanban<br>- `TCOND-APP-10`: Bật Bookmark 'Đã lưu tin'<br>- `TCOND-APP-11`: Tắt Bookmark về 'Lưu tin'<br>- `TCOND-APP-13`: Tìm kiếm đơn theo từ khóa<br>- `TCOND-APP-14`: Đổi chế độ xem List / Board | `TC-APP-007`<br>`TC-APP-009`<br>`TC-APP-010`<br>`TC-APP-011`<br>`TC-APP-013`<br>`TC-APP-014` | 6 | 6 | 0 | 6 / 0 / 0 | **100%** |
| **TỔNG CỘNG M4** | **6 Business Rules** | **14 Test Conditions** | **14 Test Cases** | **14** | **14 (100%)** | **0** | **14 / 0 / 0** | **100%** |

### Chỉ số đo lường M4 (Metrics):
* **Tổng số Business Rules M4:** 6 rules (`RULE-APP-01` $\to$ `RULE-APP-06`).
* **Độ bao phủ yêu cầu:** 6 / 6 (**100% Requirement Coverage**).
* **Tỷ lệ Tự động hóa:** 14 / 14 = **100%** (14 automated test cases).

---

## 4. TỔNG HỢP TIẾN ĐỘ THEO DÕI CÁC MODULE TRONG DỰ ÁN

| Module | Phụ trách | Tổng số BR | Số Test Cases | Tỷ lệ Tự động hóa | Trạng thái |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **M1: Đăng nhập & Phân quyền** | Nguyễn Lâm Thi | 6 | 12 | 100% (12/12) | ✅ Đã hoàn thành thiết kế & automation |
| **M2: Đăng ký tài khoản** | Võ Nguyên Khánh | (Dự kiến 4) | (Dự kiến 10-12) | Đang cập nhật | ⏳ Đang triển khai |
| **M3: Tìm kiếm & Lọc việc làm** | Nguyễn Trọng Tiến | (Dự kiến 4) | (Dự kiến 10-12) | Đang cập nhật | ⏳ Đang triển khai |
| **M4: Ứng tuyển & Theo dõi hồ sơ** | Lã Trọng Huy | 6 | 14 | 100% (14/14) | ✅ Đã hoàn thành thiết kế & automation |
