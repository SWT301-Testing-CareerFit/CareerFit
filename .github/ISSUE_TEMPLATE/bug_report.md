---
name: Bug Report (Báo cáo lỗi SUT)
about: Dành cho thành viên báo cáo lỗi website phát hiện khi chạy kiểm thử tự động
title: "[BUG] <Tiêu đề mô tả ngắn gọn lỗi>"
labels: ["bug"]
assignees: ""
---

### 1. Thông tin định danh (Identification)
* **Defect ID:** `BUG-XX` *(Điền số thứ tự bug để đối chiếu với BM07)*
* **Module:** `M1 - Đăng nhập` / `M2 - Đăng ký` / `M3 - Tìm kiếm` / `M4 - Ứng tuyển`
* **Business Rule liên quan:** `RULE-XXX-XX`
* **Test Case liên quan:** `TC-XXX-XXX`
* **Người phát hiện:** @username (Tên thành viên)
* **Ngày phát hiện:** DD/MM/YYYY

---

### 2. Phân loại mức độ (Severity & Priority)
* **Severity (Mức độ nghiêm trọng đối với hệ thống):**
  - [ ] `Critical` *(Chặn luồng chính, crash hệ thống, mất dữ liệu, SQL Injection...)*
  - [ ] `Major` *(Sai lệch nghiệp vụ nghiêm trọng nhưng có cách tạm thời)*
  - [ ] `Minor` *(Sai thông báo validate, sai thông tin hiển thị nhẹ)*
  - [ ] `Trivial` *(Lỗi chính tả, lệch giao diện nhỏ không ảnh hưởng chức năng)*
* **Priority (Mức độ ưu tiên sửa lỗi):**
  - [ ] `High`
  - [ ] `Medium`
  - [ ] `Low`

---

### 3. Môi trường kiểm thử (Environment)
* **URL:** `http://localhost:5173/...`
* **Trình duyệt:** Google Chrome (phiên bản mới nhất)
* **Hệ điều hành:** Windows 10/11
* **Dữ liệu kiểm thử:** Trích từ file CSV dòng số ...

---

### 4. Các bước tái hiện lỗi (Steps to Reproduce)
1. Mở trang ...
2. Nhập ...
3. Bấm nút ...

---

### 5. Kết quả mong đợi vs Kết quả thực tế
* **Expected Result (Mong đợi theo yêu cầu):** 
* **Actual Result (Thực tế website trả về):** 

---

### 6. Bằng chứng kiểm thử (Evidence)
* **Ảnh chụp màn hình lỗi:** *(Kéo thả file ảnh chụp từ thư mục `target/screenshots/` vào đây)*
* **Kịch bản tự động phát hiện:** `ClassTest#methodName` (Dữ liệu dòng số ...)
* **Tỷ lệ tái hiện:** 100% (X/X lần chạy)
