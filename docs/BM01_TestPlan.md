# BM01 – KẾ HOẠCH KIỂM THỬ PHẦN MỀM (TEST PLAN)
## Dự án: CareerFit – Nền tảng Tuyển dụng IT & Định hướng Kỹ năng Thông minh
### Môn học: SWT301 – Software Testing | Học kỳ: Fall 2026 | FPT University

---

| Thông tin tài liệu | Chi tiết |
|---|---|
| **Mã tài liệu** | `BM01_TestPlan` |
| **Tiêu chuẩn áp dụng** | ISO/IEC/IEEE 29119-3 (Test Plan) & ISTQB CTFL v4.0 |
| **Phiên bản** | 2.0 (Hợp nhất 4 Module) |
| **Ngày lập** | 01/11/2026 |
| **Tác giả chính** | Nguyễn Lâm Thi (Test Lead), Nguyễn Trọng Tiến (Documentation Owner) |
| **Người phối hợp** | Võ Nguyên Khánh (Framework Owner), Lã Trọng Huy (Defect Manager) |
| **Trạng thái** | Approved |

---

## 1. TỔNG QUAN VÀ MỤC TIÊU DỰ ÁN (PROJECT OVERVIEW & OBJECTIVES)

### 1.1. Bối cảnh dự án
**CareerFit** là nền tảng tìm việc chuyên biệt dành cho sinh viên và fresher ngành Công nghệ Thông tin (IT) tại Việt Nam. Hệ thống giải quyết 3 bài toán niềm tin cốt lõi:
1. **Xác minh doanh nghiệp:** Chống tin đăng lừa đảo bằng quy trình xét duyệt Giấy phép kinh doanh của Nhà tuyển dụng.
2. **Minh bạch tiến trình:** Ứng viên theo dõi trạng thái đơn tuyển dụng dạng danh sách phân nhóm Kanban (Đã nộp -> Đã xem -> Phỏng vấn -> Offer -> Từ chối).
3. **Phân tích kỹ năng 2 tầng:** Tầng 1 xếp hạng việc làm bằng so khớp tag kỹ năng (Tag-matching không gọi AI) và Tầng 2 phân tích chuyên sâu mức độ tương thích CV bằng AI/LLM.

### 1.2. Mục tiêu kiểm thử (Test Objectives)
* **Xác thực chất lượng chức năng:** Đảm bảo 100% các quy tắc nghiệp vụ (Business Rules - BR) của 4 module trọng yếu hoạt động chính xác theo tài liệu đặc tả.
* **Tự động hóa kiểm thử hồi quy (Regression Automation):** Xây dựng bộ kịch bản kiểm thử tự động Black-box dựa trên **Selenium 4 + JUnit 5 + Page Object Model (POM)** kết hợp kỹ thuật Data-Driven Testing (CSV) nhằm phát hiện sớm lỗi hồi quy khi cập nhật phần mềm.
* **Truy vết chất lượng toàn diện:** Đảm bảo độ bao phủ 100% yêu cầu thông qua Ma trận truy vết yêu cầu (RTM - BM04) và quản lý defect chặt chẽ trên GitHub Issues.

---

## 2. PHẠM VI KIỂM THỬ (TEST SCOPE)

### 2.1. Phạm vi kiểm thử (In-Scope)
Bộ tài liệu kiểm thử này tập trung vào 4 Module cốt lõi của nền tảng CareerFit:

| Module | Tên Module | Endpoint / Giao diện chính | Người phụ trách |
|---|---|---|---|
| **M1** | **Đăng nhập & Phân quyền (Login & Authorization)** | `/login`, `/jobs`, `/employer/dashboard`, `/admin/dashboard` | Nguyễn Lâm Thi |
| **M2** | **Đăng ký tài khoản (User Registration)** | `/login` (Tab Đăng ký Candidate & Employer) | Võ Nguyên Khánh |
| **M3** | **Tìm kiếm & Lọc việc làm (Job Search & Filter)** | `/jobs`, thanh tìm kiếm và bộ lọc đa tiêu chí | Nguyễn Trọng Tiến |
| **M4** | **Ứng tuyển & Theo dõi hồ sơ (Application & Tracking)** | `/jobs/:id` (Modal ứng tuyển), `/candidate/applications` (Kanban list) | Lã Trọng Huy |

### 2.2. Các loại kiểm thử áp dụng (Testing Types in Scope)
* **Functional Testing (Kiểm thử chức năng):** Kiểm thử luồng thao tác người dùng, kiểm tra validation form, phân quyền vai trò.
* **Data-driven Testing (Kiểm thử theo hướng dữ liệu):** Sử dụng các file CSV chứa dữ liệu biên và tương đương để chạy kịch bản tự động lặp.
* **Regression Testing (Kiểm thử hồi quy):** Chạy kiểm thử tự động toàn bộ 4 module trên nhánh `main` trước mỗi kỳ báo cáo/demo.
* **Negative & Boundary Testing (Kiểm thử biên và ca phủ định):** Nhập ký tự đặc biệt, để trống, vượt ngưỡng ký tự, sai định dạng.

### 2.3. Ngoài phạm vi kiểm thử (Out-of-Scope)
Theo giới hạn phạm vi môn học SWP391/SWT301, các nội dung sau không thuộc phạm vi kiểm thử tự động:
* Kiểm thử cổng thanh toán trực tuyến (hệ thống miễn phí, không có payment gateway).
* Kiểm thử gửi/nhận email thực tế qua giao thức SMTP (sử dụng in-app notification thay thế).
* Kiểm thử phòng phỏng vấn video trực tiếp tích hợp (hệ thống dùng liên kết họp ngoài: Google Meet/Zoom).
* Kiểm thử tải trọng cao (Stress/Load testing > 500 người dùng đồng thời).
* Kiểm thử bảo mật chuyên sâu (Penetration testing tầng mạng và kernel).

---

## 3. CÁCH TIẾP CẬN VÀ KỸ THUẬT KIỂM THỬ (TEST APPROACH & TECHNIQUES)

### 3.1. Kỹ thuật thiết kế kiểm thử Hộp đen (Black-Box Test Design Techniques)
Áp dụng nghiêm ngặt theo chuẩn **ISTQB Certified Tester Foundation Level (CTFL) v4.0**:
1. **Phân vùng tương đương (Equivalence Partitioning - EP):** Chia trường dữ liệu đầu vào thành các phân vùng hợp lệ (Valid Partition) và không hợp lệ (Invalid Partition) như định dạng email, độ dài mật khẩu, khoảng lương.
2. **Phân tích giá trị biên (Boundary Value Analysis - BVA):** Kiểm tra các điểm biên và giá trị cận biên (2-value boundary và 3-value boundary: min-1, min, min+1, max-1, max, max+1).
3. **Bảng quyết định (Decision Table Testing - DT):** Sử dụng cho các màn hình có sự kết hợp phức tạp giữa nhiều điều kiện (ví dụ: vai trò người dùng kết hợp trạng thái tài khoản khi đăng nhập, hoặc bộ lọc kết hợp nhiều tiêu chí tìm kiếm).
4. **Kiểm thử chuyển trạng thái (State Transition Testing - ST):** Áp dụng cho vòng đời đơn ứng tuyển (SUBMITTED -> VIEWED -> INTERVIEW -> OFFER / REJECTED) và trạng thái đăng nhập/đăng xuất.
5. **Đoán lỗi (Error Guessing - EG):** Áp dụng cho các trường hợp ngoại lệ như chèn ký tự Unicode, SQL injection thử nghiệm, khoảng trắng thừa, double-click nút submit.

### 3.2. Chiến lược Tự động hóa (Automation Testing Strategy)
* **Kiến trúc áp dụng:** **Page Object Model (POM)** chuẩn tách biệt hoàn toàn giữa cấu trúc trang (Locator, Action) và kịch bản kiểm thử (Assertion, Test Data).
* **Công nghệ cốt lõi:**
  - Ngôn ngữ: Java 17+
  - Trình điều khiển: Selenium WebDriver 4.x
  - Framework thực thi: JUnit Jupiter 5.x
  - Quản lý thư viện: Maven (`pom.xml`)
  - Quản trị Driver: WebDriverManager (tự động tải Chrome driver tương thích)
  - Quản lý cấu hình: `ConfigReader` đọc `config.properties`
* **8 Nguyên tắc vàng tự động hóa (Strict Rules):**
  1. *Tuyệt đối không Assert trong Page class.*
  2. *Tuyệt đối không dùng `By` / `findElement` trong Test class.*
  3. *Tuyệt đối cấm `Thread.sleep()`; 100% dùng Explicit Wait (`WebDriverWait` + `ExpectedConditions`).*
  4. *Ưu tiên Locator theo chuẩn: `data-testid` > `id` duy nhất > `name` > CSS Selector.*
  5. *100% dữ liệu test đọc từ file CSV (UTF-8) thông qua `@CsvFileSource`.*
  6. *Không sửa kết quả mong đợi trong CSV để làm đẹp báo cáo (lỗi phải để Fail và mở Defect).*
  7. *Mỗi ca kiểm thử phải hoàn toàn độc lập, không phụ thuộc trạng thái ca khác.*
  8. *Tự động chụp màn hình khi Fail (`ScreenshotOnFailure` extension) lưu vào `target/screenshots/`.*

---

## 4. TIÊU CHÍ BẮT ĐẦU VÀ KẾT THÚC (ENTRY & EXIT CRITERIA)

### 4.1. Tiêu chí bắt đầu (Entry Criteria)
* Bộ tài liệu Business Rules (BM02), Test Cases (BM03), và RTM (BM04) đã được review và phê duyệt.
* Ứng dụng CareerFit đã được deploy chạy ổn định tại môi trường kiểm thử cục bộ (`http://localhost:5173` cho Frontend và `http://localhost:8080` cho Backend).
* Cơ sở dữ liệu đã nạp đầy đủ dữ liệu mồi (Seed Data: 25 kỹ năng mẫu, tài khoản mẫu cho Candidate, Employer, Admin và 15 tin tuyển dụng mẫu).
* Hạ tầng Automation Framework và WebDriver đã biên dịch thành công không có lỗi build Maven.

### 4.2. Tiêu chí dừng và đình chỉ (Suspension & Resumption Criteria)
* **Đình chỉ (Suspension):** Nếu hệ thống gặp sự cố máy chủ Backend (crash database, sập server 500 toàn hệ thống) khiến > 30% kịch bản kiểm thử bị Blocked, dừng kiểm thử tự động và chuyển sang thông báo bug nghiêm trọng.
* **Tiếp tục (Resumption):** Sau khi đội ngũ dev sửa lỗi và deploy bản vá (fix build), kiểm thử viên thực hiện Smoke Test trên M1 rồi tiếp tục chạy các module còn lại.

### 4.3. Tiêu chí kết thúc (Exit Criteria)
* **Độ bao phủ yêu cầu:** 100% Business Rules của 4 Module được ánh xạ trong RTM và có ít nhất 1 Test Case kiểm tra tương ứng.
* **Tỷ lệ thực thi:** 100% Test Cases được thực thi tự động hoặc thủ công.
* **Tỷ lệ vượt qua (Pass Rate):** Tối thiểu 90% số lượng Test Cases đạt trạng thái **PASS**.
* **Quản lý Defect:** 100% Test Cases bị **FAIL** phải có mã lỗi **Defect ID** tương ứng được ghi nhận trên GitHub Issues kèm bằng chứng chụp màn hình. Không còn bất kỳ Defect mức **Critical (Blocker)** nào chưa được xử lý.

---

## 5. PHÂN TÍCH RỦI RO VÀ GIẢI PHÁP GIẢM THIỂU (RISKS & MITIGATION STRATEGY)

| Mã Rủi ro | Mô tả Rủi ro | Mức độ ảnh hưởng | Khả năng xảy ra | Biện pháp phòng ngừa & Giảm thiểu |
|:---:|---|:---:|:---:|---|
| **RSK-01** | Phần tử UI render bất đồng bộ (DOM render trễ) dẫn đến lỗi `StaleElementReferenceException` hoặc `TimeoutException` | Cao | Trung bình | Sử dụng `WebDriverWait` với `ExpectedConditions.elementToBeClickable` và `visibilityOfElementLocated`. Tăng timeout linh hoạt trong `config.properties`. |
| **RSK-02** | Dữ liệu kiểm thử bị xung đột (trùng lặp email khi chạy test đăng ký lặp lại nhiều lần) | Cao | Cao | Sử dụng tiện ích `DataGenerator` tạo chuỗi email/username ngẫu nhiên kèm timestamp trước khi nạp vào form. |
| **RSK-03** | Khác biệt độ phân giải màn hình hoặc browser zoom làm ẩn nút thao tác | Trung bình | Trung bình | Cấu hình ChromeOptions tự động bật cờ `--start-maximized` và `--window-size=1920,1080` trong `BaseTest.java`. |
| **RSK-04** | Sai lệch giữa kết quả thực tế và kỳ vọng do thay đổi giao diện/nghiệp vụ | Cao | Thấp | Cập nhật tập trung tại Page Object classes; duy trì tài liệu BM02 và BM04 song song với quá trình phát triển mã nguồn. |

---

## 6. MÔI TRƯỜNG KIỂM THỬ VÀ CÔNG CỤ (TEST ENVIRONMENT & TOOLS)

### 6.1. Cấu hình phần cứng và phần mềm kiểm thử
* **Hệ điều hành:** Windows 10 / Windows 11 (64-bit)
* **Trình duyệt kiểm thử:** Google Chrome (Phiên bản mới nhất, hỗ trợ cả Headed và Headless mode)
* **Môi trường máy chủ thử nghiệm:**
  - Frontend: Node.js v24.x, Vite Dev Server (`http://localhost:5173`)
  - Backend: Java 17, Spring Boot 3.x (`http://localhost:8080`)
  - Database: PostgreSQL / MySQL (Cổng 5432 / 3306)

### 6.2. Bộ công cụ kiểm thử (Tool Stack)
* **IDE:** IntelliJ IDEA Community / VS Code
* **Automation Framework:** Selenium WebDriver Java 4.18+, JUnit 5 (Jupiter), WebDriverManager 5.7+
* **Quản lý phiên bản & CI/CD:** Git, GitHub (GitHub Issues, Projects Kanban, Pull Requests)
* **Định dạng báo cáo & Dữ liệu:** Markdown, Microsoft Excel (`.xlsx`), CSV (UTF-8)

---

## 7. LỊCH TRÌNH VÀ PHÂN CÔNG TRÁCH NHIỆM (SCHEDULE & RACI)

### 7.1. Kế hoạch thực hiện theo Sprint (W1 – W3)
* **Tuần 1 (Foundation & Design):** Phân tích yêu cầu, xây dựng Test Plan (BM01), xác định Business Rules (BM02) cho 4 Module, thiết lập Framework Base.
* **Tuần 2 (Test Implementation & Scripting):** Viết Test Cases chi tiết (BM03), chuẩn bị file CSV, hiện thực hóa các Page Objects và Test Classes cho M1, M2, M3, M4.
* **Tuần 3 (Execution, Traceability & Closure):** Chạy thực thi kiểm thử toàn diện, hoàn thiện ma trận RTM (BM04), báo cáo defect (BM07), hợp nhất hồ sơ và đóng Issue #13.

### 7.2. Tóm tắt phân công trách nhiệm
* **Nguyễn Lâm Thi (Test Lead):** Phụ trách Module 1 (Login & Auth), chịu trách nhiệm chất lượng tài liệu BM01, BM08 và điều phối chung.
* **Võ Nguyên Khánh (Framework Owner):** Phụ trách Module 2 (Register), thiết lập nền tảng Selenium và quản trị cấu hình Maven.
* **Nguyễn Trọng Tiến (Documentation Owner):** Phụ trách Module 3 (Search & Filter), quản lý và hợp nhất bộ tài liệu BM02, BM03, BM04.
* **Lã Trọng Huy (Defect Manager):** Phụ trách Module 4 (Application & Tracking), quản lý vòng đời lỗi và bằng chứng kiểm thử.

---
*Tài liệu Kế hoạch kiểm thử BM01 được ban hành và tuân thủ nghiêm ngặt theo tiến trình môn học SWT301.*
