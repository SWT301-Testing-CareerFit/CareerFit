# BM04 – REQUIREMENTS TRACEABILITY MATRIX (RTM)
## Module M4: Ứng tuyển việc làm & Theo dõi trạng thái đơn trên Kanban (CareerFit)

> **Dự án:** CareerFit — Nền tảng tuyển dụng IT cho Sinh viên & Fresher  
> **Môn học:** SWT301 — Software Testing (Fall 2026) | **FPT University**  
> **Chuẩn tham chiếu:** ISTQB CTFL v4.0 & ISO/IEC/IEEE 29119-3  
> **Người phụ trách:** Lã Trọng Huy (SV4 - M4 Module Owner)  
> **File Excel đồng bộ:** Sheet `BM04_RTM` trong [BM03_TestCases.xlsx](file:///d:/SWT/CareerFit/docs/BM03_TestCases.xlsx)

---

## 1. BẢNG TRUY VẾT YÊU CẦU & ĐỘ BAO PHỦ KIỂM THỬ (RTM TABLE)

| Mã BR (Requirement) | Tên Business Rule | Test Condition ID & Tóm tắt | Danh sách Test Cases (TC IDs) | Số TC | Automated | Manual | Trạng thái (P/F/NR) | Coverage (%) |
|---|---|---|---|:---:|:---:|:---:|:---:|:---:|
| **RULE-APP-01** | Yêu cầu Đăng nhập & Quyền hạn ứng tuyển | - `TCOND-APP-01`: Guest bấm ứng tuyển chuyển `/login`<br>- `TCOND-APP-02`: Chặn Employer bấm ứng tuyển<br>- `TCOND-APP-12`: Tin chưa duyệt/ẩn vô hiệu hóa nút ứng tuyển | `TC-APP-001`<br>`TC-APP-002`<br>`TC-APP-012` | 3 | 3 | 0 | 0 / 0 / 3 | **100%** |
| **RULE-APP-02** | Ràng buộc Hồ sơ & Tệp CV ứng viên | - `TCOND-APP-03`: Chặn tài khoản bị khóa (`LOCKED`)<br>- `TCOND-APP-04`: Chưa có CV chuyển `/candidate-profile` | `TC-APP-003`<br>`TC-APP-004` | 2 | 2 | 0 | 0 / 0 / 2 | **100%** |
| **RULE-APP-03** | Chuyển đổi trạng thái Nộp đơn ứng tuyển | - `TCOND-APP-05`: Nộp đơn thành công, nút chuyển `✓ Đã ứng tuyển — Hủy`, tạo record status 'Đã nộp' | `TC-APP-005` | 1 | 1 | 0 | 0 / 0 / 1 | **100%** |
| **RULE-APP-04** | Chặn nộp đơn trùng lặp (Duplicate Prevention) | - `TCOND-APP-06`: Chặn submit lại lần 2 khi đã có đơn đang xử lý | `TC-APP-006` | 1 | 1 | 0 | 0 / 0 / 1 | **100%** |
| **RULE-APP-05** | Chuyển đổi trạng thái Hủy đơn ứng tuyển | - `TCOND-APP-08`: Hủy đơn thành công, nút quay lại `Ứng tuyển ngay`, xóa đơn khỏi hồ sơ | `TC-APP-008` | 1 | 1 | 0 | 0 / 0 / 1 | **100%** |
| **RULE-APP-06** | Đồng bộ Bảng Kanban & Quản lý Bookmark | - `TCOND-APP-07`: Đồng bộ đơn mới nộp vào cột 'Đã nộp'<br>- `TCOND-APP-09`: Đơn hủy biến mất khỏi Kanban<br>- `TCOND-APP-10`: Bật Bookmark 'Đã lưu tin'<br>- `TCOND-APP-11`: Tắt Bookmark về 'Lưu tin'<br>- `TCOND-APP-13`: Tìm kiếm đơn theo từ khóa<br>- `TCOND-APP-14`: Đổi chế độ xem List / Board | `TC-APP-007`<br>`TC-APP-009`<br>`TC-APP-010`<br>`TC-APP-011`<br>`TC-APP-013`<br>`TC-APP-014` | 6 | 6 | 0 | 0 / 0 / 6 | **100%** |
| **TỔNG CỘNG** | **6 Business Rules** | **14 Test Conditions** | **14 Test Cases** | **14** | **14** | **0** | **0 / 0 / 14** | **100%** |

---

## 2. CHỈ SỐ ĐO LƯỜNG ĐỘ BAO PHỦ (METRICS)

* **Tổng số Business Rules trong phạm vi M4:** 6 rules (`RULE-APP-01` $\to$ `RULE-APP-06`).
* **Số Business Rules có ít nhất 1 Test Case:** 6 / 6 (**100% Requirement Coverage** - Đạt tiêu chí Exit Criteria của ISTQB).
* **Tổng số Test Cases thiết kế:** 14 Test Cases (`TC-APP-001` $\to$ `TC-APP-014`).
* **Tỷ lệ Tự động hóa (Automation Rate):** 14 / 14 = **100%** (Vượt yêu cầu tối thiểu $\ge$ 10 TC automated).
* **Phân bổ theo kỹ thuật kiểm thử:**
  * **State Transition Testing:** 7 Test Cases (`TC-APP-005`, `TC-APP-007`, `TC-APP-008`, `TC-APP-009`, `TC-APP-010`, `TC-APP-011`, `TC-APP-014`).
  * **Decision Table Testing:** 6 Test Cases (`TC-APP-001`, `TC-APP-002`, `TC-APP-003`, `TC-APP-004`, `TC-APP-006`, `TC-APP-012`).
  * **Equivalence Partitioning (EP):** 1 Test Case (`TC-APP-013`).
* **Phân bổ theo Mức độ ưu tiên (Priority):**
  * `High`: 9 Test Cases.
  * `Medium`: 4 Test Cases.
  * `Low`: 1 Test Case.
