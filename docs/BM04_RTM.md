# BM04 – MA TRẬN TRUY VẾT YÊU CẦU (REQUIREMENTS TRACEABILITY MATRIX - RTM)

**Dự án:** CareerFit – Hệ thống Kết nối Tuyển dụng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Tác giả (Test Lead):** Nguyễn Lâm Thi (`nguyenlamthi`)  
**Phiên bản:** 1.0 | **Ngày cập nhật:** 01/11/2026  

---

## 1. MỤC TIÊU MA TRẬN RTM

Ma trận truy vết (RTM) đảm bảo:
1. Mọi quy tắc nghiệp vụ (**Business Rules - BR**) trong phạm vi kiểm thử đều có ít nhất 1 **Test Condition (TCOND)** và 1 **Test Case (TC)** tương ứng (100% Coverage).
2. Xác định rõ tỷ lệ tự động hóa (**Automation Coverage**) trên từng quy tắc.
3. Liên kết hai chiều: Từ yêu cầu/quy tắc -> Test Case -> Kết quả chạy thực tế / Defect.

---

## 2. MA TRẬN TRUY VẾT MODULE 1 (M1): ĐĂNG NHẬP & PHÂN QUYỀN

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

## 3. KHUNG MA TRẬN CHO CÁC MODULE TIẾP THEO

*(Võ Nguyên Khánh - M2, Nguyễn Trọng Tiến - M3, Lã Trọng Huy - M4 sẽ điền bổ sung khi hoàn thành thiết kế Test Cases)*

| Module | Phụ trách | Tổng số BR | Số Test Cases | Tỷ lệ Tự động hóa | Trạng thái |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **M1: Đăng nhập & Phân quyền** | Nguyễn Lâm Thi | 6 | 12 | 100% (12/12) | ✅ Đã hoàn thành thiết kế (W1) |
| **M2: Đăng ký tài khoản** | Võ Nguyên Khánh | (Dự kiến 4) | (Dự kiến 10-12) | Đang cập nhật | ⏳ Đang triển khai (#4) |
| **M3: Tìm kiếm & Lọc việc làm** | Nguyễn Trọng Tiến | (Dự kiến 4) | (Dự kiến 10-12) | Đang cập nhật | ⏳ Đang triển khai (#5) |
| **M4: Ứng tuyển & Theo dõi hồ sơ** | Lã Trọng Huy | (Dự kiến 4) | (Dự kiến 10-12) | Đang cập nhật | ⏳ Đang triển khai (#6) |
