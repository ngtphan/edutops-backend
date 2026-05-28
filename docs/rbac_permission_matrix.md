# EduTopS Backend - Ma trận Chức năng và Phân quyền (RBAC Matrix)

Tài liệu này cung cấp cái nhìn trực quan và chính xác về quyền hạn truy cập của các vai trò trong hệ thống đối với từng endpoint REST API.

---

## 🔑 Ký hiệu trong Ma trận

| Ký hiệu | Ý nghĩa |
| :---: | :--- |
| **`✓`** | Được phép truy cập không giới hạn. |
| **`Self`**| Chỉ được phép truy cập trên tài nguyên chính chủ (dựa trên JWT của người đăng nhập trùng khớp với ID đối tượng). |
| **`✗`** | Bị từ chối truy cập (HTTP 403 Forbidden). |
| **`All`** | Mọi người dùng đã đăng nhập (Authenticators) đều có quyền. |

---

## 📊 Ma trận Phân quyền theo Vai trò

### 1. Phân hệ Tài khoản (`User`)

| Chức năng | Phương thức & API | ADMIN | STAFF | CLASS_MANAGER | TEACHER | STUDENT | OWNER |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Tạo mới tài khoản | `POST /api/v1/users` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Cập nhật tài khoản | `PUT /api/v1/users/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xóa tài khoản | `DELETE /api/v1/users/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Lấy chi tiết tài khoản | `GET /api/v1/users/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xem danh sách tài khoản | `GET /api/v1/users` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Tìm kiếm bằng Email | `GET /api/v1/users/email/{email}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xem tài khoản hiện tại | `GET /api/v1/users/me` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`Self`** |

---

### 2. Phân hệ Học viên (`Student`)

| Chức năng | Phương thức & API | ADMIN | STAFF | CLASS_MANAGER | TEACHER | STUDENT | OWNER |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Tạo mới hồ sơ học viên | `POST /api/v1/students` | **`✓`** | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Cập nhật thông tin học viên | `PUT /api/v1/students/{id}` | **`✓`** | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`Self`** |
| Hoàn tất hồ sơ cá nhân | `POST /api/v1/students/user/{userId}/complete-profile` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`Self`** |
| Xóa hồ sơ học viên | `DELETE /api/v1/students/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xem chi tiết học viên | `GET /api/v1/students/{id}` | **`✓`** | **`✓`** | **`✓`** | **`✓`** | **`✗`** | **`Self`** |
| Xem danh sách học viên | `GET /api/v1/students` | **`✓`** | **`✓`** | **`✓`** | **`✓`** | **`✗`** | **`✗`** |
| Tìm học viên theo User ID | `GET /api/v1/students/user/{userId}` | **`✓`** | **`✓`** | **`✓`** | **`✓`** | **`✗`** | **`Self`** |

---

### 3. Phân hệ Giáo viên (`Teacher`)

| Chức năng | Phương thức & API | ADMIN | STAFF | CLASS_MANAGER | TEACHER | STUDENT | OWNER |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Tạo mới hồ sơ giáo viên | `POST /api/v1/teachers` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Cập nhật thông tin giáo viên | `PUT /api/v1/teachers/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`Self`** |
| Xóa hồ sơ giáo viên | `DELETE /api/v1/teachers/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xem chi tiết giáo viên | `GET /api/v1/teachers/{id}` | **`✓`** | **`✓`** | **`✓`** | **`✓`** | **`✓`** | **`✗`** |
| Xem danh sách giáo viên | `GET /api/v1/teachers` | **`✓`** | **`✓`** | **`✓`** | **`✓`** | **`✓`** | **`✗`** |
| Tìm giáo viên theo User ID | `GET /api/v1/teachers/user/{userId}` | **`✓`** | **`✓`** | **`✓`** | **`✓`** | **`✗`** | **`Self`** |

---

### 4. Phân hệ Khóa học (`Course`)

| Chức năng | Phương thức & API | ADMIN | STAFF | CLASS_MANAGER | TEACHER | STUDENT |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: |
| Tạo mới khóa học | `POST /api/v1/courses` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Cập nhật khóa học | `PUT /api/v1/courses/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xóa khóa học | `DELETE /api/v1/courses/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xem chi tiết khóa học | `GET /api/v1/courses/{id}` | **`All`** | **`All`** | **`All`** | **`All`** | **`All`** |
| Xem danh sách khóa học | `GET /api/v1/courses` | **`All`** | **`All`** | **`All`** | **`All`** | **`All`** |

---

### 5. Phân hệ Môn học (`Subject`)

| Chức năng | Phương thức & API | ADMIN | STAFF | CLASS_MANAGER | TEACHER | STUDENT |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: |
| Tạo mới môn học | `POST /api/v1/subjects` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Cập nhật môn học | `PUT /api/v1/subjects/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xóa môn học | `DELETE /api/v1/subjects/{id}` | **`✓`** | **`✗`** | **`✗`** | **`✗`** | **`✗`** |
| Xem danh sách/Chi tiết môn | `GET /api/v1/subjects` | **`All`** | **`All`** | **`All`** | **`All`** | **`All`** |

---

## 💡 Điểm Nổi bật trong Kiến trúc Bảo mật

1. **Phân quyền Độc lập cho Vận hành & Quản lý**:
   - `STAFF` được xem như vai trò chăm sóc và hỗ trợ ban đầu, được phép **tạo hồ sơ học sinh** và **cập nhật thông tin học sinh**. Tuy nhiên `STAFF` không có quyền xóa hay chỉnh sửa các phân hệ học thuật khác.
   - `CLASS_MANAGER` được xem như vai trò điều hành giáo dục, có quyền **đọc toàn bộ danh sách giáo viên & học viên** để thực hiện lập lớp, chia lịch học, nhưng **không có quyền tạo mới hoặc thay đổi hồ sơ nhân sự** (giúp kiểm soát chặt chẽ dữ liệu).
2. **Cơ chế Bảo vệ Chính chủ (Self-Ownership Protection)**:
   - Các API cập nhật và xem hồ sơ cá nhân có ký hiệu **`Self`** được cấu hình bởi bộ lọc SpEL thông minh (`@securityUtils.isSelfOrAdmin(...)`). Chỉ chính tài khoản đang đăng nhập hoặc `ADMIN` mới được phép thao tác, tránh tuyệt đối lỗi rò rỉ dữ liệu chéo (`BOLA / IDOR`).
3. **Phân tách Rõ ràng Luồng OAuth2 Google & Điền Profile**:
   - Khi người dùng đăng nhập bằng tài khoản Google lần đầu (chưa điền profile), cờ `profileCompleted` trả về `false`.
   - Học sinh chỉ có thể gọi duy nhất API `/user/{userPublicId}/complete-profile` với phân quyền **`Self`** để hoàn tất hồ sơ, sau đó hệ thống mới mở các quyền hạn tiếp theo tương ứng với vai trò.
