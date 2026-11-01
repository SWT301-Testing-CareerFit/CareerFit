# BM04 – MA TRẬN TRUY VẾT YÊU CẦU (REQUIREMENTS TRACEABILITY MATRIX - RTM)

**Dự án:** CareerFit – Hệ thống Kết nối Tuyển dụng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Tác giả:** Nguyễn Lâm Thi (Test Lead - M1), Võ Nguyên Khánh (Framework Owner / Tester - M2)  
**Phiên bản:** 1.1 | **Ngày cập nhật:** 01/11/2026  

---

## 1. MỤC TIÊU MA TRẬN RTM

Ma trận truy vết (RTM) đảm bảo:
1. Mọi quy tắc nghiệp vụ (**Business Rules - BR**) trong phạm vi kiểm thử đều có ít nhất 1 **Test Condition (TCOND)** và 1 **Test Case (TC)** tương ứng (**100% Requirement Coverage**).
2. Xác định rõ tỷ lệ tự động hóa (**Automation Coverage**) trên từng quy tắc nghiệp vụ.
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

## 3. MA TRẬN TRUY VẾT MODULE 2 (M2): ĐĂNG KÝ TÀI KHOẢN (REGISTRATION)

* **Người phụ trách:** Võ Nguyên Khánh (`richnk123a-cyber` / `VoNguyenKhanh`)
* **Sheet tương ứng trong BM03:** `M2_Register`

| BR ID | Mô tả Business Rule | Test Condition ID | Danh sách Test Cases (TC IDs) | Tổng số TC | Số TC Tự động | Pass | Fail | Not Run | Độ bao phủ (Coverage) |
| :--- | :--- | :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **RULE-REG-01** | Định dạng Email hợp lệ (`local@domain.tld`), chặn email sai định dạng | `TCOND-REG-01`<br>`TCOND-REG-02` | `TC-REG-001`<br>`TC-REG-012` | 2 | 2 (100%) | 0 | 0 | 2 | **100%** |
| **RULE-REG-02** | Tính duy nhất của Email (chặn đăng ký email đã tồn tại trong DB) | `TCOND-REG-03`<br>`TCOND-REG-04` | `TC-REG-001`<br>`TC-REG-013` | 2 | 2 (100%) | 0 | 0 | 2 | **100%** |
| **RULE-REG-03** | Mật khẩu chuẩn BVA [8, 32] ký tự và độ phức tạp (chữ hoa, số, ký tự đặc biệt) | `TCOND-REG-05`<br>`TCOND-REG-06`<br>`TCOND-REG-07`<br>`TCOND-REG-08`<br>`TCOND-REG-09`<br>`TCOND-REG-10`<br>`TCOND-REG-11` | `TC-REG-004`<br>`TC-REG-005`<br>`TC-REG-006`<br>`TC-REG-007`<br>`TC-REG-008`<br>`TC-REG-009`<br>`TC-REG-010` | 7 | 7 (100%) | 0 | 0 | 7 | **100%** |
| **RULE-REG-04** | Xác nhận mật khẩu phải trùng khớp 100% (Case-sensitive) | `TCOND-REG-12`<br>`TCOND-REG-13` | `TC-REG-001`<br>`TC-REG-011` | 2 | 2 (100%) | 0 | 0 | 2 | **100%** |
| **RULE-REG-05** | Họ tên / Người đại diện bắt buộc, dài 2-100 ký tự, chặn XSS/Script | `TCOND-REG-14`<br>`TCOND-REG-15` | `TC-REG-001`<br>`TC-REG-017` | 2 | 2 (100%) | 0 | 0 | 2 | **100%** |
| **RULE-REG-06** | Số điện thoại di động VN gồm 10 số, bắt đầu bằng 03, 05, 07, 08, 09 | `TCOND-REG-16`<br>`TCOND-REG-17` | `TC-REG-001`<br>`TC-REG-014` | 2 | 2 (100%) | 0 | 0 | 2 | **100%** |
| **RULE-REG-07** | Doanh nghiệp bắt buộc tên công ty & upload GPKD (PDF/PNG/JPG ≤ 10MB) | `TCOND-REG-18`<br>`TCOND-REG-19`<br>`TCOND-REG-20`<br>`TCOND-REG-21`<br>`TCOND-REG-22` | `TC-REG-002`<br>`TC-REG-003`<br>`TC-REG-015`<br>`TC-REG-016`<br>`TC-REG-017` | 5 | 5 (100%) | 0 | 0 | 5 | **100%** |
| **RULE-REG-08** | Luồng đăng ký thành công (Candidate/Employer) & Token kích hoạt 24h | `TCOND-REG-23`<br>`TCOND-REG-24`<br>`TCOND-REG-25` | `TC-REG-001`<br>`TC-REG-002`<br>`TC-REG-003`<br>`TC-REG-018` | 4 | 3 (75%) | 0 | 0 | 4 | **100%** |
| **TỔNG CỘNG M2** | **8 Business Rules** | **25 Test Conditions** | **18 Test Cases** | **18** | **17 (94.4%)** | **0** | **0** | **18** | **100%** |

---

## 4. TỔNG HỢP TIẾN ĐỘ THIẾT KẾ VÀ ĐỘ BAO PHỦ CÁC MODULE

| Module | Phụ trách | Tổng số BR | Số Test Cases | Tỷ lệ Tự động hóa | Trạng thái |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **M1: Đăng nhập & Phân quyền** | Nguyễn Lâm Thi | 6 | 12 | 100% (12/12) | ✅ Đã hoàn thành thiết kế (W1) |
| **M2: Đăng ký tài khoản** | Võ Nguyên Khánh | 8 | 18 | 94.4% (17/18) | ✅ Đã hoàn thành thiết kế (W1 - #4) |
| **M3: Tìm kiếm & Lọc việc làm** | Nguyễn Trọng Tiến | (Dự kiến 4) | (Dự kiến 10-12) | Đang cập nhật | ⏳ Đang triển khai (#5) |
| **M4: Ứng tuyển & Theo dõi hồ sơ** | Lã Trọng Huy | (Dự kiến 4) | (Dự kiến 10-12) | Đang cập nhật | ⏳ Đang triển khai (#6) |
