# EduTopS Backend - Nhật ký Thay đổi & Lộ trình Phát triển (Changelog & Roadmap)

Tài liệu này lưu trữ lịch sử các phiên bản phát hành, các tính năng đã tạo, các thay đổi bảo mật và lộ trình tối ưu hóa các module nghiệp vụ của hệ thống **EduTopS** theo từng cột mốc.

---

## 📌 Lộ trình Phát hành & Trạng thái các Phiên bản

| Phiên bản | Cột mốc nghiệp vụ | Trạng thái | Ngày hoàn thành |
| :--- | :--- | :--- | :--- |
| **v1.0.0-beta1** | Security & Auth Infrastructure |  Hoàn thành | 2026-05-28 |
| **v1.0.0-beta2** | Core Business Baseline (Milestone 1) |  **Đang triển khai** | *Hiện tại* |
| **v1.1.0** | Classrooms & Enrollment (Milestone 2) |  *Kế hoạch* | *Giai đoạn 2* |
| **v1.2.0** | Smart Schedule & Attendance (Milestone 3) |  *Kế hoạch* | *Giai đoạn 3* |

---

## [v1.0.0-beta2] - Core Business Baseline (Giai đoạn 1)
*Phiên bản nâng cấp Milestone 1 tích hợp Quản lý Giáo viên & Khóa học đi kèm kiểm soát bảo mật nâng cao và tối ưu luồng Google Sign-In.*

### 🛠️ Chức năng Đang triển khai:
#### 1. Khóa học có thời hạn đào tạo (`startDate`/`endDate`)
* **Course Entity**: Bổ sung `startDate` (Ngày bắt đầu) và `endDate` (Ngày kết thúc).
* **Validation**: Ràng buộc chặt chẽ ngày bắt đầu phải trước hoặc bằng ngày kết thúc.
* **DTOs**: Cập nhật DTO Requests và Response đồng bộ.

#### 2. Tự hoàn thiện Profile Google Login (Không dùng Dữ liệu Rác)
* **Auth Flow**: Khi người dùng đăng nhập bằng Google lần đầu tiên, chỉ tạo thực thể `User` mặc định (vai trò `STUDENT`), không tự động chèn profile `Student` rác với các giá trị mặc định trống.
* **TokenResponse**: Thêm thuộc tính `profileCompleted` (trả về `false` nếu tài khoản chưa hoàn tất điền hồ sơ học viên).
* **Complete Profile API**: Tạo mới API `POST /api/v1/students/user/{userPublicId}/complete-profile` cho phép Học viên tự điền hồ sơ thật (*Số điện thoại, Ngày sinh, Giới tính*).

#### 3. Vai trò Quản lý lớp học (`CLASS_MANAGER`) & Ràng buộc bảo mật
* **Role**: Khai báo hằng số vai trò `CLASS_MANAGER` trong `UserRole.java`.
* **Phân quyền APIs**:
  * **Hồ sơ gốc (Giáo viên, Học viên, Khóa học)**: Chỉ `ADMIN` được quyền tạo, sửa, xóa (Giữ nguyên tính bảo mật đặc quyền hệ thống).
  * **Đọc dữ liệu điều phối**: Cấp quyền cho `CLASS_MANAGER` được đọc danh sách Giáo viên và Học viên để ghép lớp học ở giai đoạn sau.
  * **Lớp học & Đăng ký (Giai đoạn 2)**: `CLASS_MANAGER` sẽ được cấp toàn quyền tạo mới lớp học, ghép giáo viên dạy và đăng ký/hủy đăng ký học viên.

#### 4. Hạ tầng & Sửa lỗi hệ thống
* **OpenAPI/Swagger UI**: Tích hợp nút nhập Bearer JWT Token hỗ trợ thử nghiệm API phân quyền.
* **DDL Fix**: Khắc phục lỗi `column "deleted" of relation contains null values` của PostgreSQL bằng cách bổ sung `default false` trong định nghĩa cột của `BaseEntity`.

---

## [v1.0.0-beta1] - Security & Auth Infrastructure
*Phiên bản nền tảng tích hợp hạ tầng bảo mật JWT Bearer, xác thực Google OAuth2 và cấu trúc lập trình Generic.*

### 🛠️ Các chức năng đã hoàn thành:
* **Hạ tầng bảo mật**:
  * Kích hoạt Method Security (`@EnableMethodSecurity`).
  * Tích hợp Nimbus JWT symmetric key (HMAC-SHA256) ký và xác thực token tự động.
  * Triển khai helper bean `SecurityUtils` xử lý phân quyền "ADMIN hoặc Chính chủ" (`isSelfOrAdmin`).
* **Hạ tầng mã nguồn**:
  * Xây dựng cấu trúc Generic Base Class (`BaseEntity`, `BaseRepository`, `BaseService`, `BaseServiceImpl`, `BaseController`).
  * Đồng bộ cơ chế Dual ID Pattern (`Long id` nội bộ / `UUID publicId` đối ngoại) bảo mật thông tin.
  * Xử lý ngoại lệ tập trung qua `GlobalExceptionHandler`, `BusinessException` và mã lỗi nghiệp vụ `ErrorCode`.
* **Module Môn học (Subject)**:
  * Hoàn thành API CRUD môn học, cấu hình lưu trữ validation thông điệp tập trung trong `ValidationMessages.properties`.

---

## 🔮 Lộ trình phát triển tương lai (Product Roadmap)

### 1. Phiên bản v1.1.0 (Classrooms & Enrollment)
* **Quản lý lớp học (ClassGroup)**: Triển khai CRUD lớp học thuộc về Khóa học, do Giáo viên phụ trách.
* **Gán ghép thành viên**:
  * Quản lý lớp học (`CLASS_MANAGER`) có quyền thêm học viên vào lớp học.
  * Tự động kiểm tra sức chứa tối đa của lớp (`maxStudents`), chặn đăng ký khi lớp học đầy sĩ số.
  * Đảm bảo một học sinh không đăng ký trùng lặp 2 lần trong cùng một lớp.

### 2. Phiên bản v1.2.0 (Smart Schedule & Attendance)
* **Xếp lịch học (Schedule)**:
  * Hỗ trợ tạo lịch học lặp lại nhiều ngày trong tuần (2-4-6, 3-5-7) thông qua mô hình chuẩn hóa đa bản ghi.
  * Thuật toán **Kiểm tra trùng lịch thông minh (Collision Detection)**: Tự động chặn xếp lịch nếu phòng học hoặc giáo viên bị vướng lịch dạy khác trong cùng khung giờ.
  * Đảm bảo sĩ số tối đa của lớp học không vượt quá sức chứa tối đa (`capacity`) của phòng học chỉ định.
* **Điểm danh (Attendance)**:
  * Điểm danh học viên từng buổi học, tính toán tỷ lệ đi học chuyên cần phục vụ xuất báo cáo.
