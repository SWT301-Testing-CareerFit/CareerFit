# BM06 – TEST EXECUTION LOG (NHẬT KÝ THỰC THI KIỂM THỬ)

**Dự án:** CareerFit – Nền tảng Tuyển dụng IT cho Sinh viên & Fresher  
**Môn học:** SWT301 – Software Testing (Fall 2026) | Đại học FPT  
**Chuẩn tham chiếu:** ISO/IEC/IEEE 29119-3 & ISTQB CTFL v4.0  
**Người thực hiện:** Lã Trọng Huy (SV4 - Defect Manager) & Nhóm Kiểm thử CareerFit  
**Đợt kiểm thử:** Regression Test Toàn diện 4 Module (M1, M2, M3, M4)  
**Ngày thực hiện:** 01/11/2026  

---

## 1. THÔNG TIN MÔI TRƯỜNG THỰC THI (TEST ENVIRONMENT)

* **Trình duyệt:** Google Chrome 156 (Headless & UI Mode)
* **WebDriver:** ChromeDriver 156.0.8078.80
* **Frontend:** Vite Dev Server (`http://localhost:5173`)
* **Backend:** Spring Boot (Java 17, Port 8080) / Fallback Mode
* **Hệ điều hành:** Windows 11 64-bit
* **Công cụ thực thi:** Maven Surefire Plugin 3.2.5 + JUnit 5

---

## 2. KẾT QUẢ THỰC THI CHI TIẾT THEO TỪNG ĐỢT CHẠY

### 2.1. ĐỢT CHẠY 1 (RUN-01) — REGRESSION TEST ĐẦU TIÊN
* **Mục tiêu:** Chạy toàn bộ test suite của cả 4 module ngay sau khi merge vào nhánh `main`.
* **Lệnh thực thi:** `mvn clean test -Dheadless=true`
* **Thời gian thực thi:** 5 phút 21 giây
* **Tổng số kịch bản thực thi:** **59 tests**

| Test Suite / Module | Người phụ trách | Số Tests | PASS | FAIL | ERROR | Tỷ lệ Pass |
|---|---|:---:|:---:|:---:|:---:|:---:|
| `ConfigReaderTest` (Framework) | Võ Nguyên Khánh | 3 | 3 | 0 | 0 | 100% |
| `DataGeneratorTest` (Framework) | Võ Nguyên Khánh | 4 | 4 | 0 | 0 | 100% |
| `SampleFrameworkTest` (Framework) | Võ Nguyên Khánh | 2 | 1 | 0 | 1 | 50% |
| `LoginTest` (M1: Đăng nhập) | Nguyễn Lâm Thi | 13 | 13 | 0 | 0 | 100% |
| `RegisterTest` (M2: Đăng ký) | Võ Nguyên Khánh | 14 | 8 | 6 | 0 | 57.1% |
| `JobSearchTest` (M3: Tìm kiếm) | Nguyễn Trọng Tiến | 14 | 14 | 0 | 0 | 100% |
| `ApplicationTest` (M4: Ứng tuyển) | Lã Trọng Huy | 9 | 9 | 0 | 0 | 100% |
| **TỔNG CỘNG RUN-01** | **Cả nhóm** | **59** | **52** | **6** | **1** | **88.1%** |

#### Ghi nhận sự cố trong RUN-01 chuyển sang Defect Manager (Huy) phân loại:
1. `SampleFrameworkTest.testScreenshotOnFailureGeneration`: Lỗi `NullPointerException` tại `context.getStore()` $\implies$ **Lỗi Script** (đã sửa null check).
2. `RegisterTest.testEmployerRegistrationWithLicenseUploadSmoke`: Lỗi `Failed to fetch` khi nộp form đăng ký doanh nghiệp $\implies$ **Lỗi SUT Bug** (M2).
3. `RegisterTest.testRegisterDataDriven` (5 ca kiểm thử): Đều báo `Failed to fetch` do form đăng ký chưa có cơ chế local fallback khi ngắt kết nối backend $\implies$ **Lỗi SUT Bug** (M2).
4. `ApplicationTest` & `LoginTest`: **100% PASS** nhờ các bản sửa lỗi `localStorage`, `setActivePage` và deduplicate `userMap` đã được áp dụng trong đợt trước.

---

### 2.2. ĐỢT CHẠY 2 (RUN-02) — KIỂM THỬ LẠI & BÁO CÁO SUREFIRE HTML
* **Mục tiêu:** Chạy lại sau khi sửa lỗi script và xuất báo cáo nghiệm thu dạng HTML.
* **Lệnh thực thi:** `mvn clean test -Dheadless=true surefire-report:report`
* **File báo cáo HTML:** Đã tạo tại `backend/target/reports/surefire.html` và lưu trữ tại `docs/reports/surefire-report.html`.
* **Ảnh minh chứng chụp lỗi:** Đã lưu trữ toàn bộ tại thư mục `docs/reports/`:
  - `RegisterTest_testEmployerRegistrationWithLicenseUploadSmoke_*.png`
  - `RegisterTest_testRegisterDataDriven_*.png`
  - `SampleFrameworkTest_testScreenshotOnFailureGeneration_*.png`

---

## 3. KẾT LUẬN & ĐÁNH GIÁ ĐỢT KIỂM THỬ

1. **Module M4 (Ứng tuyển & Theo dõi đơn) của Lã Trọng Huy:** Đạt **100% PASS** (9/9 tests tự động bao phủ trọn vẹn 14 Test Cases trong BM03).
2. **Module M1 (Đăng nhập) & M3 (Tìm kiếm):** Hoạt động ổn định, 100% PASS.
3. **Các lỗi phát hiện được:** Đã được Defect Manager chuyển sang tài liệu **`docs/BM07_DefectReport.md`** và quản lý theo chuẩn ISTQB CTFL 5.5.
