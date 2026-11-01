# BM04 – MA TRẬN TRUY VẾT YÊU CẦU (REQUIREMENTS TRACEABILITY MATRIX - RTM)

**Dự án:** CareerFit – Nền tảng Tuyển dụng IT & Định hướng Kỹ năng Thông minh  
**Môn học:** SWT301 – Software Testing (Đại học FPT)  
**Tiêu chuẩn áp dụng:** ISO/IEC/IEEE 29119-3 & ISTQB CTFL v4.0  
**Phiên bản:** 2.1 (Đồng bộ toàn diện Module M3 từ `RULE-SEARCH-01` đến `RULE-SEARCH-07` theo Issue #5)  
**Ngày cập nhật:** 01/11/2026  
**Chịu trách nhiệm tài liệu:** Nguyễn Trọng Tiến (`tiendeptroai` - Documentation Owner)  

---

## 1. MỤC TIÊU MA TRẬN TRUY VẾT (RTM OBJECTIVES)

Ma trận truy vết yêu cầu (RTM) đảm bảo:
1. **Độ bao phủ yêu cầu 100% (100% Coverage):** Mọi quy tắc nghiệp vụ (**Business Rule - BR**) suy biến từ yêu cầu của Issue #5 và tài liệu SRS đều được kiểm thử thông qua ít nhất một **Test Condition (TCOND)** và một **Test Case (TC)** tương ứng.
2. **Truy vết hai chiều (Bidirectional Traceability):** Từ Business Rule -> Test Condition -> Test Case -> Phương thức kiểm thử tự động (Automation Method) -> Kết quả thực thi.
3. **Theo dõi tỷ lệ tự động hóa (Automation Coverage):** 100% các Test Case chức năng cốt lõi và kiểm tra an toàn đều được tự động hóa.

---

## 2. MA TRẬN TRUY VẾT MODULE 3 (M3): TÌM KIẾM, BỘ LỌC & AN TOÀN DỮ LIỆU
* **Người phụ trách:** Nguyễn Trọng Tiến (`tiendeptroai` - Documentation Owner)
* **Kỹ thuật kiểm thử áp dụng:** Equivalence Partitioning (EP), Decision Table (DT), Error Guessing (EG - XSS & SQLi).

| BR ID | Tên Quy tắc nghiệp vụ | Test Condition ID | Danh sách Test Cases (TC IDs) | Kỹ thuật kiểm thử | Số TC Tự động | Tỷ lệ Tự động | Độ bao phủ |
| :--- | :--- | :--- | :--- | :--- | :---: | :---: | :---: |
| **RULE-SEARCH-01** | Tìm kiếm theo từ khóa hợp lệ (Title, Skill, Company - Java, React, Tester...) | `TCOND-SEARCH-01`<br>`TCOND-SEARCH-02`<br>`TCOND-SEARCH-03` | `TC-SEARCH-001`<br>`TC-SEARCH-002`<br>`TC-SEARCH-003`<br>`TC-SEARCH-011` | Equivalence Partitioning (EP) | 4 / 4 | 100% | **100%** |
| **RULE-SEARCH-02** | Xử lý khi từ khóa không tồn tại (Empty State "Không tìm thấy việc làm phù hợp") | `TCOND-SEARCH-04` | `TC-SEARCH-005` | Equivalence Partitioning (EP) | 1 / 1 | 100% | **100%** |
| **RULE-SEARCH-03** | Xử lý từ khóa rỗng & khoảng trắng (Nạp lại toàn bộ việc làm khả dụng) | `TCOND-SEARCH-05` | `TC-SEARCH-006` | Equivalence Partitioning (EP) | 1 / 1 | 100% | **100%** |
| **RULE-SEARCH-04** | Lọc theo Địa điểm làm việc (Hà Nội, TP.HCM, Đà Nẵng, Remote) | `TCOND-SEARCH-06` | `TC-SEARCH-004` | Equivalence Partitioning (EP) | 1 / 1 | 100% | **100%** |
| **RULE-SEARCH-05** | Lọc theo Loại hình & Kết hợp đa tiêu chí (AND logic), Nút đặt lại bộ lọc | `TCOND-SEARCH-07`<br>`TCOND-SEARCH-08` | `TC-SEARCH-005_Type`<br>`TC-SEARCH-006_Multi`<br>`TC-SEARCH-009_Reset` | Decision Table / State Transition | 3 / 3 | 100% | **100%** |
| **RULE-SEARCH-06** | Kiểm tra an toàn dữ liệu: Chống XSS Injection (`<script>alert(1)</script>`) & Ký tự lạ | `TCOND-SEARCH-09` | `TC-SEARCH-007`<br>`TC-SEARCH-010`<br>`TC-SEC-XSS` | Error Guessing (EG) / Security | 3 / 3 | 100% | **100%** |
| **RULE-SEARCH-07** | Kiểm tra an toàn dữ liệu: Chống SQL Injection (`' OR 1=1 --`) không gây lỗi 500 | `TCOND-SEARCH-10` | `TC-SEARCH-008`<br>`TC-SEC-SQLI` | Error Guessing (EG) / Security | 2 / 2 | 100% | **100%** |
| **TIỂU MỤC M3** | **7 Business Rules** | **10 Conditions** | **14 Test Cases** | | **14 / 14** | **100%** | **100%** |

---

## 3. TỔNG HỢP MA TRẬN 4 MODULE HỆ THỐNG

| Module | Tên Module | Người phụ trách | Số lượng BRs | Số lượng TCs | Tỷ lệ Tự động | Trạng thái |
|:---:|---|---|:---:|:---:|:---:|:---:|
| **M1** | Đăng nhập & Phân quyền | Nguyễn Lâm Thi | 6 | 12 | 100% | ✅ Hoàn thành |
| **M2** | Đăng ký tài khoản | Võ Nguyên Khánh | 5 | 11 | 100% | ✅ Hoàn thành |
| **M3** | **Tìm kiếm, Lọc việc làm & An toàn (Issue #5)** | **Nguyễn Trọng Tiến** | **7** | **14** | **100%** | **✅ Hoàn thành** |
| **M4** | Ứng tuyển & Theo dõi hồ sơ | Lã Trọng Huy | 5 | 11 | 100% | ✅ Hoàn thành |
| **TỔNG**| **Toàn bộ hệ thống** | | **23 Rules** | **48 Test Cases** | **100%** | **Sẵn sàng nghiệm thu** |

---

## 4. KẾT LUẬN & ĐÁNH GIÁ ĐỘ BAO PHỦ

1. **Tuân thủ triệt để Issue #5:** Toàn bộ 7 Business Rules từ `RULE-SEARCH-01` đến `RULE-SEARCH-07` đều được truy vết 1-1 với kịch bản kiểm thử.
2. **Kỹ thuật kiểm thử hộp đen:** Đã áp dụng đúng Phân vùng tương đương (EP), Bảng quyết định (DT), và Đoán lỗi an toàn dữ liệu (EG - XSS & SQLi).
3. **Tự động hóa 100%:** 14/14 ca kiểm thử của M3 đều có mã tự động hóa trong `JobSearchTest.java` và đã được kiểm chứng Pass 100%.

---
*Tài liệu Ma trận RTM BM04 được ban hành chính thức theo tiêu chuẩn ISO/IEC/IEEE 29119-3.*
