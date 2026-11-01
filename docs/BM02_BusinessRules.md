# BM02 – BUSINESS RULES & TEST CONDITIONS
## Module M4: Ứng tuyển việc làm & Theo dõi trạng thái đơn trên Kanban (CareerFit)

> **Dự án:** CareerFit — Nền tảng tuyển dụng IT dành cho Sinh viên & Fresher  
> **Môn học:** SWT301 — Software Testing (Fall 2026) | **FPT University**  
> **Chuẩn tham chiếu:** ISTQB CTFL v4.0 (Ch.1.4 Test Analysis & Ch.4.2 Black-box Techniques)  
> **Người phụ trách (Module Owner):** Lã Trọng Huy (SV4 - Defect Manager & M4 Owner)  
> **Phạm vi giao diện:** `/jobs/:id` (Chi tiết tin tuyển dụng) và `/candidate/applications` (Bảng Kanban theo dõi đơn ứng tuyển)

---

## 1. TỔNG QUAN VỀ PHẠM VI NGHIỆP VỤ M4

Module M4 chịu trách nhiệm quản lý toàn bộ luồng tương tác của Ứng viên (Candidate) đối với tin tuyển dụng và quá trình theo dõi đơn sau khi ứng tuyển:
1. **Ứng tuyển việc làm (Job Application):** Kiểm tra điều kiện tiên quyết (đăng nhập, vai trò, trạng thái tài khoản, tệp CV đã tải lên) trước khi cho phép gửi hồ sơ.
2. **Theo dõi trạng thái đơn (Application Kanban Tracking):** Hiển thị danh sách các đơn đã nộp theo dạng nhóm trạng thái (Grouped List / Kanban Column): `Đã nộp` (Submitted) $\to$ `Đã xem` (Viewed) $\to$ `Phỏng vấn` (Interview) $\to$ `Offer` $\to$ `Từ chối` (Rejected).
3. **Hủy đơn ứng tuyển (Cancel Application):** Cho phép ứng viên rút lại hồ sơ khi còn ở trạng thái ban đầu, tự động cập nhật lại giao diện tin tuyển dụng và bảng Kanban.
4. **Lưu tin tuyển dụng (Bookmark Job):** Hỗ trợ bật/tắt đánh dấu tin tuyển dụng để tra cứu sau.

---

## 2. DANH SÁCH BUSINESS RULES CHI TIẾT (RULE-APP-01 ĐẾN RULE-APP-06)

| BR ID | Tên quy tắc nghiệp vụ | Mô tả quy tắc kiểm thử được (Testable Statement) | Điều kiện tiên quyết & Dữ liệu | Kết quả mong đợi (Expected Result) | Auto? |
|---|---|---|---|---|:---:|
| **RULE-APP-01** | Yêu cầu Đăng nhập & Quyền hạn ứng tuyển | Khi người dùng chưa đăng nhập bấm "Ứng tuyển ngay" tại `/jobs/:id`, hệ thống phải chặn thao tác, thông báo yêu cầu đăng nhập và chuyển hướng sang `/login`. Nếu đăng nhập với vai trò khác CANDIDATE (ví dụ EMPLOYER), hệ thống hiển thị thông báo lỗi quyền hạn và không cho nộp đơn. | - Người dùng là Guest hoặc Employer.<br>- Tin tuyển dụng công khai hợp lệ. | - Chưa đăng nhập: Toast thông báo *"Vui lòng đăng nhập tài khoản Ứng viên để nộp đơn!"*, chuyển sang `/login`.<br>- Sai vai trò: Toast *"Chỉ tài khoản Ứng viên mới có thể nộp đơn ứng tuyển!"*. | ✅ Có |
| **RULE-APP-02** | Ràng buộc Hồ sơ & Tệp CV ứng viên | Ứng viên đã đăng nhập chỉ được phép nộp đơn khi đã có tệp CV (PDF/DOCX) trong hồ sơ cá nhân (`hasCV = true`). Nếu chưa có CV, hệ thống phải chặn nộp đơn, hiển thị cảnh báo và chuyển hướng sang `/candidate-profile`. Tài khoản bị Admin khóa (`status = 'LOCKED'`) cũng bị chặn hoàn toàn. | - Candidate đã đăng nhập.<br>- Case A: Chưa upload CV (`cvFileName = null`).<br>- Case B: Tài khoản bị khóa (`status = LOCKED`). | - Chưa có CV: Toast *"⚠️ Vui lòng tải lên file CV (PDF/DOCX) trong Hồ sơ & CV trước khi nộp đơn ứng tuyển!"*, điều hướng sang `/candidate-profile`.<br>- Tài khoản khóa: Toast *"Tài khoản của bạn đã bị khóa bởi Quản trị viên. Không thể ứng tuyển!"*. | ✅ Có |
| **RULE-APP-03** | Chuyển đổi trạng thái Nộp đơn (State Transition) | Khi Candidate thỏa mãn mọi điều kiện bấm "Ứng tuyển ngay", hệ thống tạo đơn ứng tuyển mới với trạng thái mặc định là `Đã nộp`. Nút bấm trên giao diện ngay lập tức chuyển từ `[Ứng tuyển ngay]` sang `[✓ Đã ứng tuyển — Hủy]`. | - Candidate đăng nhập, đã có CV hợp lệ.<br>- Chưa từng nộp đơn vào job này. | - Toast *"Ứng tuyển thành công! Nhà tuyển dụng đã nhận hồ sơ của bạn."* xuất hiện.<br>- Nút chuyển thành `✓ Đã ứng tuyển — Hủy`.<br>- Tạo 1 record đơn mới với status = 'Đã nộp'. | ✅ Có |
| **RULE-APP-04** | Chặn nộp đơn trùng lặp (Duplicate Prevention) | Mỗi ứng viên chỉ được nộp tối đa 01 đơn ứng tuyển cho một tin tuyển dụng cụ thể. Khi đã nộp, nút ứng tuyển hiển thị trạng thái đã ứng tuyển; nếu kích hoạt lại lệnh nộp đơn, hệ thống phải chặn và báo lỗi đã nộp. | - Candidate đã nộp đơn trước đó cho job này.<br>- Thao tác nộp lại lần 2. | - Hệ thống không sinh thêm đơn trùng trong cơ sở dữ liệu.<br>- Toast hiển thị: *"Bạn đã nộp đơn ứng tuyển cho công việc này rồi!"*. | ✅ Có |
| **RULE-APP-05** | Chuyển đổi trạng thái Hủy đơn (State Transition) | Khi ứng viên đang ở trạng thái `[Đã ứng tuyển]`, bấm vào nút `[— Hủy]`, hệ thống lập tức hủy đơn ứng tuyển. Nút bấm trên giao diện lập tức quay về trạng thái ban đầu `[Ứng tuyển ngay]`. | - Candidate đã nộp đơn thành công trước đó.<br>- Đang ở trang chi tiết `/jobs/:id`. | - Toast *"Đã hủy đơn ứng tuyển thành công."* xuất hiện.<br>- Nút bấm đổi lại thành `Ứng tuyển ngay`.<br>- Đơn ứng tuyển bị xóa khỏi danh sách ứng tuyển của candidate. | ✅ Có |
| **RULE-APP-06** | Đồng bộ Bảng Kanban & Quản lý Bookmark (Lưu tin) | - **Đồng bộ Kanban:** Khi nộp đơn thành công, truy cập `/candidate/applications` đơn phải xuất hiện tại cột/nhóm `Đã nộp hồ sơ`. Khi hủy đơn, đơn phải biến mất hoàn toàn khỏi Kanban.<br>- **Bookmark:** Bấm nút "Lưu tin" chuyển sang trạng thái "Đã lưu tin" (icon fill), bấm lần nữa quay lại "Lưu tin" (icon unfill). | - Candidate đã đăng nhập.<br>- Thực hiện nộp/hủy đơn và bật/tắt lưu tin. | - Kanban `/candidate/applications`: Đơn hiển thị đúng cột `Đã nộp`, ngày nộp và tên công ty; khi hủy thì biến mất.<br>- Nút Lưu tin: Toggle đúng văn bản và trạng thái active `is-saved`. | ✅ Có |

---

## 3. PHÂN TÍCH KỸ THUẬT KIỂM THỬ HỘP ĐEN (BLACK-BOX TECHNIQUES)

### 3.1. Kỹ thuật Bảng quyết định (Decision Table) cho RULE-APP-01, RULE-APP-02, RULE-APP-04

Bảng quyết định kiểm tra tất cả các tổ hợp điều kiện đầu vào khi người dùng kích hoạt hành động **"Ứng tuyển ngay"**:

| Điều kiện (Conditions) | R1 | R2 | R3 | R4 | R5 | R6 |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| Người dùng đã đăng nhập? | **N** | **Y** | **Y** | **Y** | **Y** | **Y** |
| Vai trò tài khoản là `CANDIDATE`? | — | **N** (Employer) | **Y** | **Y** | **Y** | **Y** |
| Trạng thái tài khoản `ACTIVE` (không bị khóa)? | — | — | **N** (Locked) | **Y** | **Y** | **Y** |
| Hồ sơ ứng viên đã có CV đính kèm? | — | — | — | **N** (No CV) | **Y** | **Y** |
| Đã từng ứng tuyển vào job này trước đó? | — | — | — | — | **Y** (Đã nộp) | **N** (Chưa nộp) |
| **Hành động / Kết quả kỳ vọng (Actions)** | | | | | | |
| Chuyển hướng sang `/login` | **X** | | | | | |
| Báo lỗi quyền hạn (Không cho phép Employer) | | **X** | | | | |
| Báo lỗi tài khoản bị khóa (`LOCKED`) | | | **X** | | | |
| Báo lỗi thiếu CV, chuyển hướng `/candidate-profile` | | | | **X** | | |
| Chặn nộp trùng lặp, giữ trạng thái "Đã ứng tuyển" | | | | | **X** | |
| Nộp đơn thành công, nút đổi thành "✓ Đã ứng tuyển — Hủy" | | | | | | **X** |
| **Test Case ID tương ứng** | `TC-APP-001` | `TC-APP-002` | `TC-APP-003` | `TC-APP-004` | `TC-APP-006` | `TC-APP-005` |

---

### 3.2. Kỹ thuật Chuyển đổi trạng thái (State Transition Testing)

#### Sơ đồ 1: Vòng đời nút Ứng tuyển tại trang Chi tiết việc làm (`/jobs/:id`)
```
                     [S1: Chưa ứng tuyển]
                       (Nút: "Ứng tuyển ngay")
                                 |
           Thao tác: Click "Ứng tuyển ngay" (Hồ sơ hợp lệ)
                                 v
                     [S2: Đã ứng tuyển]
                   (Nút: "✓ Đã ứng tuyển — Hủy")
                                 |
           Thao tác: Click "— Hủy" (Xác nhận hủy)
                                 v
                     [S1: Chưa ứng tuyển]
                       (Nút: "Ứng tuyển ngay")
```

#### Sơ đồ 2: Đồng bộ hiển thị trên Bảng theo dõi Kanban (`/candidate/applications`)
```
 [Chưa nộp đơn] ──(Nộp đơn tại /jobs/:id)──> [Hiển thị tại Cột: "Đã nộp hồ sơ"]
       ^                                                      |
       │                                                      │ (Nhà tuyển dụng review)
       │                                                      v
       │                                            [Cột: "Đã được xem"]
       │                                                      |
       │ (Hủy đơn khi còn ở "Đã nộp")                          v
       └─────────────────────────────────────── [Cột: "Phỏng vấn / Offer / Từ chối"]
```

#### Sơ đồ 3: Vòng đời nút Bookmark Lưu tin tuyển dụng
```
 [B1: Chưa lưu tin] ──(Click "Lưu tin")──> [B2: Đã lưu tin] (is-saved = true, icon fill)
          ^                                        |
          └───────(Click "Đã lưu tin")─────────────┘
```

---

## 4. MA TRẬN ĐIỀU KIỆN KIỂM THỬ (TEST CONDITIONS MATRIX)

| Test Condition ID | Mô tả điều kiện kiểm thử (Test Condition) | Kỹ thuật áp dụng | Ánh xạ Test Case ID |
|---|---|:---:|:---:|
| `TCOND-APP-01` | Kiểm tra chuyển hướng về trang `/login` khi Guest (chưa đăng nhập) bấm Ứng tuyển | Decision Table | `TC-APP-001` |
| `TCOND-APP-02` | Kiểm tra chặn ứng tuyển khi đăng nhập bằng tài khoản không phải Ứng viên (Employer) | Decision Table | `TC-APP-002` |
| `TCOND-APP-03` | Kiểm tra chặn ứng tuyển đối với tài khoản Ứng viên đang ở trạng thái bị khóa (`LOCKED`) | Decision Table | `TC-APP-003` |
| `TCOND-APP-04` | Kiểm tra cảnh báo thiếu CV và điều hướng sang trang cập nhật hồ sơ khi ứng viên chưa tải CV | Decision Table | `TC-APP-004` |
| `TCOND-APP-05` | Kiểm tra nộp đơn ứng tuyển thành công, đổi nhãn nút sang "✓ Đã ứng tuyển — Hủy" và toast thông báo | State Transition | `TC-APP-005` |
| `TCOND-APP-06` | Kiểm tra chặn nộp đơn trùng lặp khi ứng viên đã có hồ sơ đang xử lý cho tin tuyển dụng đó | Decision Table | `TC-APP-006` |
| `TCOND-APP-07` | Kiểm tra đơn ứng tuyển mới nộp xuất hiện đồng bộ chính xác tại cột "Đã nộp" trên bảng Kanban | State Transition | `TC-APP-007` |
| `TCOND-APP-08` | Kiểm tra hủy đơn ứng tuyển thành công tại trang chi tiết job, nút chuyển lại "Ứng tuyển ngay" | State Transition | `TC-APP-008` |
| `TCOND-APP-09` | Kiểm tra sau khi hủy đơn ứng tuyển, đơn bị gỡ bỏ hoàn toàn khỏi giao diện Kanban | State Transition | `TC-APP-009` |
| `TCOND-APP-10` | Kiểm tra chuyển đổi trạng thái nút Bookmark từ "Lưu tin" sang "Đã lưu tin" | State Transition | `TC-APP-010` |
| `TCOND-APP-11` | Kiểm tra chuyển đổi trạng thái nút Bookmark từ "Đã lưu tin" trở về "Lưu tin" | State Transition | `TC-APP-011` |
| `TCOND-APP-12` | Kiểm tra nút ứng tuyển bị vô hiệu hóa ("Chưa mở ứng tuyển") khi tin chưa được duyệt hoặc bị ẩn | BVA / State | `TC-APP-012` |
| `TCOND-APP-13` | Kiểm tra chức năng tìm kiếm đơn ứng tuyển theo từ khóa tên việc làm / công ty trên Kanban | Equivalence Partitioning | `TC-APP-013` |
| `TCOND-APP-14` | Kiểm tra chuyển đổi chế độ xem giữa dạng Danh sách (List) và dạng Bảng (Board/Kanban) | State Transition | `TC-APP-014` |
