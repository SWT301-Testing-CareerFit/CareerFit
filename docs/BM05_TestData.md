# BM05 – ĐẶC TẢ DỮ LIỆU KIỂM THỬ & THỦ TỤC THỰC THI (TEST DATA & TEST PROCEDURE SPECIFICATION)
## Dự án: CareerFit – Nền tảng Tuyển dụng IT & Định hướng Kỹ năng Thông minh
### Môn học: SWT301 – Software Testing | Học kỳ: Fall 2026 | FPT University

---

| Thông tin tài liệu | Chi tiết |
|---|---|
| **Mã tài liệu** | `BM05_TestData` |
| **Tiêu chuẩn áp dụng** | ISO/IEC/IEEE 29119-3 (Test Data / Test Procedure Specification) & ISTQB CTFL v4.0 |
| **Phiên bản** | 2.0 (Hợp nhất toàn diện 4 Module: M1, M2, M3, M4) |
| **Ngày lập** | 01/11/2026 |
| **Tác giả** | Nguyễn Trọng Tiến (`tiendeptroai` - Documentation Owner) & Võ Nguyên Khánh (`richnk123a-cyber` - Framework Owner) |
| **Trạng thái** | Approved |

---

## 1. MỤC TIÊU VÀ NGUYÊN TẮC QUẢN TRỊ DỮ LIỆU KIỂM THỬ

### 1.1. Mục tiêu
Tài liệu này đặc tả toàn bộ cấu trúc, tập dữ liệu đầu vào (Input Test Data), định dạng mã hóa, và thủ tục thực thi (Test Procedure) phục vụ cho việc kiểm thử tự động Black-box theo mô hình **Data-Driven Testing (DDT)**.

### 1.2. 5 Nguyên tắc vàng về Dữ liệu kiểm thử
1. **Định dạng chuẩn UTF-8:** 100% các file dữ liệu CSV lưu trữ tại thư mục `src/test/resources/testdata/` phải được mã hóa theo chuẩn **UTF-8** (hỗ trợ tiếng Việt có dấu).
2. **Tách biệt hoàn toàn khỏi mã nguồn:** Không hard-code dữ liệu trong Test class; Test method nạp dữ liệu từ CSV thông qua annotation `@CsvFileSource`.
3. **Cấu trúc cột đồng bộ:** Cột đầu tiên luôn là `tcId`, cột kế cuối là `expectedResult`, cột cuối cùng là `expectedMessage` (hoặc kết quả kiểm tra tương ứng).
4. **Dữ liệu động chống xung đột:** Đối với các luồng đăng ký tài khoản (M2), sử dụng `DataGenerator` sinh email/username ngẫu nhiên kèm timestamp để tránh lỗi trùng lặp khi chạy hồi quy nhiều lần.
5. **Dữ liệu tĩnh đã seed sẵn:** Dựa trên tập dữ liệu mồi chuẩn (Seed data: 25 kỹ năng, các tài khoản mẫu Candidate/Employer/Admin, các tin tuyển dụng mẫu).

---

## 2. DANH MỤC CÁC BỘ DỮ LIỆU KIỂM THỬ (TEST DATA CSV)

| STT | Tên file dữ liệu CSV | Đường dẫn tương đối | Module áp dụng | Người phụ trách | Số lượng dòng dữ liệu |
|:---:|---|---|---|---|:---:|
| 1 | `login_data.csv` | `src/test/resources/testdata/login_data.csv` | **M1: Đăng nhập & Phân quyền** | Nguyễn Lâm Thi | 12 ca kiểm thử |
| 2 | `register_data.csv` | `src/test/resources/testdata/register_data.csv` | **M2: Đăng ký tài khoản** | Võ Nguyên Khánh | 11 ca kiểm thử |
| 3 | `search_data.csv` | `src/test/resources/testdata/search_data.csv` | **M3: Tìm kiếm & Lọc việc làm** | Nguyễn Trọng Tiến | 11 ca kiểm thử |
| 4 | `apply_data.csv` | `src/test/resources/testdata/apply_data.csv` | **M4: Ứng tuyển & Theo dõi hồ sơ** | Lã Trọng Huy | 11 ca kiểm thử |

---

## 3. CHI TIẾT BỘ DỮ LIỆU CHO TỪNG MODULE

### 3.1. Dữ liệu kiểm thử Module 1: `login_data.csv`
* **File nguồn:** `src/test/resources/testdata/login_data.csv`
* **Kịch bản kiểm thử áp dụng:** `LoginTest#testLoginDataDriven`

```csv
tcId,email,password,expectedResult,expectedMessage
TC-LOGIN-001,candidate@careerfit.vn,Candidate@123,SUCCESS,/jobs
TC-LOGIN-002,employer@careerfit.vn,Employer@123,SUCCESS,/employer
TC-LOGIN-003,admin@careerfit.vn,Admin@123,SUCCESS,/admin
TC-LOGIN-004,candidate@careerfit.vn,WrongPass@999,FAIL,Email hoặc mật khẩu không chính xác
TC-LOGIN-005,notfound_99@careerfit.vn,SomePass@123,FAIL,Email hoặc mật khẩu không chính xác
TC-LOGIN-006,invalid-email-format,ValidPass@123,REQUIRED,
TC-LOGIN-007,,,REQUIRED,
TC-LOGIN-008,candidate@careerfit.vn,,REQUIRED,
TC-LOGIN-009,,Candidate@123,REQUIRED,
TC-LOGIN-010,candidate@careerfit.vn,candidate@123,FAIL,Email hoặc mật khẩu không chính xác
TC-LOGIN-011,locked_user@careerfit.vn,LockedPass@123,LOCKED,Tài khoản của bạn đã bị khóa hoặc chưa kích hoạt
TC-LOGIN-012,candidate@careerfit.vn,Candidate@123,LOGOUT,/login
```

---

### 3.2. Dữ liệu kiểm thử Module 2: `register_data.csv`
* **File nguồn:** `src/test/resources/testdata/register_data.csv`
* **Kịch bản kiểm thử áp dụng:** `RegisterTest#testRegisterDataDriven`

```csv
tcId,role,fullName,email,password,confirmPassword,licenseFile,expectedResult,expectedMessage
TC-REG-001,CANDIDATE,Trần Văn Nam,DYNAMIC_EMAIL,Candidate@123,Candidate@123,,SUCCESS,Đăng ký thành công
TC-REG-002,CANDIDATE,Lê Minh Quân,DYNAMIC_EMAIL,Pass@123,Pass@123,,SUCCESS,Đăng ký thành công
TC-REG-003,EMPLOYER,FPT Software,recruiter@fpt.com,Employer@123,Employer@123,license.pdf,SUCCESS,UNVERIFIED
TC-REG-004,CANDIDATE,Nguyễn Trùng Lặp,candidate@careerfit.vn,NewPass@123,NewPass@123,,FAIL,Email đã được sử dụng
TC-REG-005,CANDIDATE,Phạm Văn A,DYNAMIC_EMAIL,Abc@123,Abc@123,,FAIL,Mật khẩu phải từ 8 ký tự
TC-REG-006,CANDIDATE,Phạm Văn B,DYNAMIC_EMAIL,candidate123,candidate123,,FAIL,Mật khẩu phải chứa ít nhất 1 chữ hoa và ký tự đặc biệt
TC-REG-007,CANDIDATE,Phạm Văn C,DYNAMIC_EMAIL,Candidate@123,Candidate@124,,FAIL,Mật khẩu xác nhận không trùng khớp
TC-REG-008,CANDIDATE,,,,,FAIL,Vui lòng điền đầy đủ các trường bắt buộc
TC-REG-009,CANDIDATE,Hoàng Nam,usernodomain@,ValidPass@123,ValidPass@123,,FAIL,Email không hợp lệ
TC-REG-010,CANDIDATE,   ,DYNAMIC_EMAIL,Candidate@123,Candidate@123,,FAIL,Họ tên không được để trống
TC-REG-011,EMPLOYER,Công ty Mẫu,recruiter2@company.com,Employer@123,Employer@123,,FAIL,Vui lòng tải lên Giấy phép kinh doanh
```

---

### 3.3. Dữ liệu kiểm thử Module 3: `search_data.csv`
* **File nguồn:** `src/test/resources/testdata/search_data.csv`
* **Kịch bản kiểm thử áp dụng:** `JobSearchTest#testSearchAndFilterDataDriven`

```csv
tcId,keyword,location,jobType,expectedResult,minExpectedCount
TC-SEARCH-001,Java,Tất cả,Tất cả,FOUND,1
TC-SEARCH-002,frontend,Tất cả,Tất cả,FOUND,1
TC-SEARCH-003,React,Tất cả,Tất cả,FOUND,1
TC-SEARCH-004,,Hà Nội,Tất cả,FOUND,1
TC-SEARCH-005,,Tất cả,INTERNSHIP,FOUND,1
TC-SEARCH-006,React,TP.HCM,FULL_TIME,FOUND,1
TC-SEARCH-007,xyz_non_existent_job_999,Tất cả,Tất cả,EMPTY,0
TC-SEARCH-008,@#$%^&*(),Tất cả,Tất cả,EMPTY,0
TC-SEARCH-009,RESET,RESET,RESET,RESET_ALL,5
TC-SEARCH-010,Tuyển dụng bị ẩn vi phạm,Tất cả,Tất cả,EMPTY,0
TC-SEARCH-011,Đà Nẵng,Tất cả,Tất cả,FOUND,1
```

---

### 3.4. Dữ liệu kiểm thử Module 4: `apply_data.csv`
* **File nguồn:** `src/test/resources/testdata/apply_data.csv`
* **Kịch bản kiểm thử áp dụng:** `ApplicationTest#testApplicationDataDriven`

```csv
tcId,candidateEmail,jobId,hasCV,expectedAction,expectedResult,expectedColumn
TC-APPLY-001,candidate@careerfit.vn,1,true,APPLY,SUCCESS,SUBMITTED
TC-APPLY-002,candidate_nocv@careerfit.vn,1,false,APPLY,BLOCKED_NO_CV,
TC-APPLY-003,candidate@careerfit.vn,1,true,RE_APPLY,DISABLED,SUBMITTED
TC-APPLY-004,GUEST,1,false,APPLY,REDIRECT_LOGIN,
TC-APPLY-005,employer@careerfit.vn,1,false,APPLY,FORBIDDEN,
TC-APPLY-006,candidate@careerfit.vn,N/A,true,VIEW_KANBAN,5_COLUMNS_DISPLAYED,
TC-APPLY-007,candidate@careerfit.vn,1,true,CHECK_STATUS,CARD_IN_SUBMITTED,SUBMITTED
TC-APPLY-008,candidate@careerfit.vn,1,true,DRAG_DROP,READ_ONLY,SUBMITTED
TC-APPLY-009,candidate@careerfit.vn,2,true,VIEW_INTERVIEW_NOTE,SHOW_TIME_AND_LINK,INTERVIEW
TC-APPLY-010,candidate@careerfit.vn,N/A,true,CHECK_NOTIFICATION,BADGE_INCREMENT,
TC-APPLY-011,candidate@careerfit.vn,N/A,true,CLICK_NOTIFICATION,MARK_READ_DECREMENT,
```

---

## 4. THỦ TỤC THỰC THI KIỂM THỬ (TEST PROCEDURE SPECIFICATION)

### 4.1. Quy trình chuẩn bị môi trường (Pre-requisite Setup)
1. Khởi động cơ sở dữ liệu và nạp dữ liệu mồi:
   ```bash
   cd database
   docker-compose up -d
   ```
2. Khởi chạy máy chủ Backend (Spring Boot):
   ```bash
   cd backend
   mvn spring-boot:run
   ```
3. Khởi chạy máy chủ Frontend (Vite Dev Server):
   ```bash
   cd frontend
   npm run dev
   ```

### 4.2. Lệnh thực thi kiểm thử tự động
* **Chạy kiểm thử riêng từng Module:**
  ```bash
  # Chạy M1: Đăng nhập & Phân quyền
  mvn test -Dtest=LoginTest

  # Chạy M2: Đăng ký tài khoản
  mvn test -Dtest=RegisterTest

  # Chạy M3: Tìm kiếm & Lọc việc làm
  mvn test -Dtest=JobSearchTest

  # Chạy M4: Ứng tuyển & Theo dõi hồ sơ
  mvn test -Dtest=ApplicationTest
  ```
* **Chạy toàn bộ bộ kiểm thử hồi quy (Full Regression Test Suite):**
  ```bash
  mvn clean test
  ```

### 4.3. Quy trình xử lý lỗi khi kiểm thử thất bại (Failure Handling Procedure)
1. Khi có bất kỳ ca kiểm thử nào bị **FAIL**:
   - `ScreenshotOnFailure` tự động lưu ảnh chụp màn hình tại thư mục `target/screenshots/<TestClassName>_<MethodName>_<Timestamp>.png`.
2. Kiểm thử viên đối chiếu kết quả thực tế với `expectedResult` trong file CSV.
3. Nếu xác định là lỗi hệ thống (Defect/Bug):
   - Mở issue mới trên GitHub Issues theo mẫu **Defect Report (BM07)**.
   - Gắn nhãn phân loại: `bug`, `severity:high`/`critical`, `module:<name>`.
   - Đính kèm ảnh chụp màn hình từ `target/screenshots/` làm bằng chứng.
   - Tuyệt đối **không sửa kết quả mong đợi trong CSV** để né lỗi.

---
*Tài liệu Đặc tả Dữ liệu kiểm thử BM05 hoàn tất bộ hồ sơ kiểm thử chuẩn mực ISO/IEC/IEEE 29119-3.*
