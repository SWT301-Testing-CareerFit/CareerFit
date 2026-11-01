# BM09 – MA TRẬN PHÂN CÔNG TRÁCH NHIỆM (RACI MATRIX) & ĐÁNH GIÁ ĐÓNG GÓP THÀNH VIÊN
## Dự án: CareerFit – Nền tảng Tuyển dụng IT & Định hướng Kỹ năng Thông minh
### Môn học: SWT301 – Software Testing | Học kỳ: Fall 2026 | FPT University

---

| Thông tin tài liệu | Chi tiết |
|---|---|
| **Mã tài liệu** | `BM09_PhanCong` |
| **Tiêu chuẩn áp dụng** | Ma trận RACI (Responsible, Accountable, Consulted, Informed) & Quy chế đào tạo FPT University |
| **Phiên bản** | 2.0 (Tổng hợp tiến độ W1 – W3) |
| **Ngày lập** | 01/11/2026 |
| **Chịu trách nhiệm chính** | Nguyễn Lâm Thi (Test Lead) & Nguyễn Trọng Tiến (Documentation Owner) |
| **Kho mã nguồn dự án** | `https://github.com/SWT301-Testing-CareerFit/CareerFit` |

---

## 1. THÀNH VIÊN NHÓM VÀ VAI TRÒ CHÍNH

Nhóm gồm 4 thành viên, vận hành theo mô hình kết hợp **Trách nhiệm dọc (Module Ownership)** và **Vai trò điều phối ngang (System Role)**:

| STT | Họ và tên | Mã sinh viên | GitHub Username | Vai trò điều phối ngang | Module sở hữu trọn vẹn (Dọc) |
|:---:|---|:---:|:---:|---|---|
| 1 | **Nguyễn Lâm Thi** | DE190001 | `nguyenlamthi` | **Test Lead** | **M1: Đăng nhập & Phân quyền** (`/login`) |
| 2 | **Võ Nguyên Khánh** | DE190002 | `richnk123a-cyber` | **Framework Owner** | **M2: Đăng ký tài khoản** (`/login` tab Đăng ký) |
| 3 | **Nguyễn Trọng Tiến** | DE190036 | `tiendeptroai` | **Documentation Owner** | **M3: Tìm kiếm & Lọc việc làm** (`/jobs`) |
| 4 | **Lã Trọng Huy** | DE190004 | `TrongHuy317` | **Defect Manager** | **M4: Ứng tuyển & Theo dõi hồ sơ** (`/candidate/applications`) |

---

## 2. MA TRẬN PHÂN CÔNG TRÁCH NHIỆM RACI

* **R (Responsible):** Người trực tiếp thực hiện công việc.
* **A (Accountable):** Người chịu trách nhiệm cuối cùng về chất lượng công việc (duy nhất 1 người).
* **C (Consulted):** Người được tham vấn chuyên môn, đóng góp ý kiến.
* **I (Informed):** Người được thông báo tiến độ và kết quả sau khi hoàn thành.

| STT | Hạng mục công việc / Giao phẩm (Deliverables) | Nguyễn Lâm Thi (Test Lead) | Võ Nguyên Khánh (Framework Owner) | Nguyễn Trọng Tiến (Docs Owner) | Lã Trọng Huy (Defect Manager) |
|:---:|---|:---:|:---:|:---:|:---:|
| **1** | **Xây dựng Kế hoạch kiểm thử (BM01_TestPlan.md)** | **A / R** | C | R | I |
| **2** | **Thiết lập Khung Automation (BasePage, BaseTest, pom.xml)** | C | **A / R** | I | C |
| **3** | **Bộ Business Rules & Test Conditions (BM02_BusinessRules.md)** | C | C | **A / R** | C |
| **4** | **Bảng đặc tả Test Cases (BM03_TestCases.xlsx & .md)** | C | C | **A / R** | C |
| **5** | **Ma trận truy vết yêu cầu (BM04_RTM.md)** | C | I | **A / R** | C |
| **6** | **Thiết kế & Tự động hóa Module 1 (LoginTest, LoginPage, login_data.csv)** | **A / R** | C | I | I |
| **7** | **Thiết kế & Tự động hóa Module 2 (RegisterTest, RegisterPage, register_data.csv)**| I | **A / R** | I | I |
| **8** | **Thiết kế & Tự động hóa Module 3 (JobSearchTest, JobSearchPage, search_data.csv)**| I | C | **A / R** | I |
| **9** | **Thiết kế & Tự động hóa Module 4 (ApplicationTest, JobDetailPage, apply_data.csv)**| I | C | I | **A / R** |
| **10**| **Quản trị Defect Report & Bằng chứng lỗi (BM07_DefectReport)** | C | I | I | **A / R** |
| **11**| **Chạy Regression Test & Báo cáo tổng kết (BM08_TestSummaryReport)** | **A / R** | C | C | C |
| **12**| **Review Pull Request & Điều phối GitHub Project Board** | **A / R** | C | C | C |

---

## 3. TỰ ĐÁNH GIÁ TỶ LỆ ĐÓNG GÓP VÀ ĐIỂM SỐ CỦA CÁC THÀNH VIÊN

Tất cả 4 thành viên đều tham gia tích cực, nghiêm túc, hoàn thành đúng hạn các nhiệm vụ được giao và phối hợp nhịp nhàng theo GitHub Flow. Nhóm thống nhất tự đánh giá tỷ lệ đóng góp công bằng:

| STT | Thành viên | Nhiệm vụ hoàn thành xuất sắc | Tỷ lệ đóng góp | Tự đánh giá điểm (Thang 10) | Nhóm trưởng xác nhận |
|:---:|---|---|:---:|:---:|:---:|
| 1 | **Nguyễn Lâm Thi** | - Khởi tạo tài liệu Test Plan (BM01), điều phối chung toàn nhóm.<br>- Hoàn thiện trọn vẹn Module 1: 12 Test Cases, code `LoginPage`, `LoginTest`, `login_data.csv`.<br>- Review và duyệt các Pull Request vào nhánh `main`. | **25%** | **10 / 10** | ✅ Đồng ý |
| 2 | **Võ Nguyên Khánh** | - Dựng khung Automation chuẩn Selenium 4 + JUnit 5 + Maven.<br>- Viết `BasePage`, `BaseTest`, `ConfigReader`, `DataGenerator`, `ScreenshotOnFailure`.<br>- Hoàn thiện trọn vẹn Module 2: 11 Test Cases, code `RegisterPage`, `RegisterTest`, `register_data.csv`. | **25%** | **10 / 10** | ✅ Đồng ý |
| 3 | **Nguyễn Trọng Tiến** | - Chuẩn hóa và hợp nhất toàn bộ bộ hồ sơ kiểm thử: BM01, BM02, BM03, BM04.<br>- Thiết lập ma trận truy vết RTM đạt độ bao phủ 100% (21 Rules, 45 TCs).<br>- Hoàn thiện trọn vẹn Module 3: 11 Test Cases, code `JobSearchPage`, `JobSearchTest`, `search_data.csv`. | **25%** | **10 / 10** | ✅ Đồng ý |
| 4 | **Lã Trọng Huy** | - Quản lý quy trình kiểm thử lỗi (Defects) trên GitHub Issues, gắn nhãn phân loại theo độ nghiêm trọng.<br>- Kiểm tra và lưu trữ ảnh chụp bằng chứng lỗi tại `target/screenshots/`.<br>- Hoàn thiện trọn vẹn Module 4: 11 Test Cases, code `JobDetailPage`, `ApplicationTest`, `apply_data.csv`. | **25%** | **10 / 10** | ✅ Đồng ý |
| **TỔNG CỘNG** | **4 Thành viên** | **100% các đầu việc hoàn thành đúng tiến độ Milestone W3** | **100%** | **10 / 10** | **Đồng thuận 100%** |

---

## 4. QUY TRÌNH LÀM VIỆC NHÓM & QUY ƯỚC CHẤT LƯỢNG (DEFINITION OF DONE)

### 4.1. Quy ước Git & GitHub Flow
1. **Phân nhánh độc lập:** Mọi công việc đều thực hiện trên nhánh tính năng riêng:
   - Thi: `feature/M1-login-tests`
   - Khánh: `feature/M2-register-tests`
   - Tiến: `feature/M3-search-tests` hoặc `docs/m3-search`
   - Huy: `feature/M4-application-tests`
2. **Quy tắc Commit:** Tuân thủ Conventional Commits (`test(search): add JobSearchPage locators (#13)`).
3. **Quy tắc Review chéo (Peer Review):**
   - Mọi Pull Request bắt buộc có tối thiểu **1 reviewer phê duyệt (`Approved`)** trước khi merge.
   - Nhóm trưởng (Nguyễn Lâm Thi) thực hiện **Squash and Merge** để giữ lịch sử commit của `main` sạch sẽ.

### 4.2. Tiêu chí hoàn thành (Definition of Done - DoD)
- [x] 100% tài liệu (BM01, BM02, BM03, BM04, BM09) được định dạng thống nhất, chuẩn Markdown và Excel UTF-8.
- [x] Không còn lỗi chính tả hoặc thiếu sót mã định danh giữa các tài liệu.
- [x] Ma trận RTM đạt độ bao phủ **100%** (toàn bộ 21 Business Rules đều có Test Case tương ứng).
- [x] File Excel `docs/BM03_TestCases.xlsx` đầy đủ 4 sheet (`M1_Login`, `M2_Register`, `M3_Search`, `M4_Application`) kèm đầy đủ cột `Automation Method`.
- [x] Code Automation tuân thủ 8 nguyên tắc vàng POM (không sleep, không hardcode, đọc từ CSV).

---
*Biên bản phân công và tự đánh giá đóng góp BM09 được toàn thể 4 thành viên nhóm thống nhất và ký nhận.*
