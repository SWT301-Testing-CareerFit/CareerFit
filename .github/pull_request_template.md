## 📋 Mô tả thay đổi (Description)
* **Module phụ trách:** M1 (Login) / M2 (Register) / M3 (Search) / M4 (Application) / Framework
* **Mã Issue liên quan:** Closes #___
* **Nội dung thay đổi:** 

---

## 🧪 Các Test Cases liên quan (Associated Test Cases)
- `TC-XXX-001` đến `TC-XXX-00X`

---

## ✅ Checklist tự kiểm tra của tác giả (Author Checklist)
- [ ] Đã chạy `mvn clean test -Dtest=<Class>` thành công trên máy local trước khi tạo PR.
- [ ] Tuân thủ quy tắc POM: **Không có Assertion trong Page class**; **Không có locator (By/findElement) trong Test class**.
- [ ] **Không sử dụng `Thread.sleep()`**, 100% dùng `WebDriverWait` (Explicit Wait).
- [ ] Không hard-code dữ liệu trong code test, toàn bộ dữ liệu nằm trong file CSV (`src/test/resources/testdata/`).
- [ ] Đã cập nhật mã phương thức tự động vào bảng Test Cases (BM03).

---

## 📊 Kết quả kiểm thử trên máy Local
* Tests run: ___, Failures: ___, Errors: ___, Skipped: ___
*(Đính kèm ảnh kết quả chạy hoặc ảnh terminal nếu có)*
