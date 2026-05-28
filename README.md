# EduTopS - Hệ thống Vận hành & Quản lý Trung tâm Giáo dục (Backend)

EduTopS là hệ thống backend hiệu năng cao, được xây dựng trên nền tảng Spring Boot để hỗ trợ quản lý lịch học, hồ sơ học viên, phân công giảng dạy và điểm danh chuyên cần cho các trung tâm giáo dục.

---

## 🛠️ Công nghệ Sử dụng (Tech Stack)

* **Ngôn ngữ**: Java 21 (LTS)
* **Framework**: Spring Boot 3.x / 4.x
* **Bảo mật**: Spring Security, OAuth2 Resource Server, Nimbus JWT
* **CSDL & ORM**: PostgreSQL, Spring Data JPA, Hibernate 6 (Soft Delete tích hợp)
* **Tài liệu hóa API**: OpenAPI 3 / Swagger UI
* **Kiểm thử**: JUnit 5, Spring Security Test

---

## 🏗️ Kiến trúc & Thiết kế Hệ thống

Dự án áp dụng các pattern thiết kế chuẩn mực, tối ưu hóa tối đa khả năng bảo trì và sẵn sàng mở rộng (production-ready):

### 1. Dual ID Pattern
Mọi thực thể (`BaseEntity`) kế thừa cơ chế hai định danh song song:
* `id (Long)`: Khóa chính tăng tự động nội bộ hệ thống (Primary Key), tối ưu hóa tốc độ đánh chỉ mục (indexing) và liên kết khóa ngoại (Foreign Key join).
* `publicId (UUID)`: ID công khai ngẫu nhiên dùng đối ngoại (REST APIs, frontend URL path), ngăn chặn nguy cơ tấn công dò quét dữ liệu tuần tự (Enumeration Attacks).

### 2. Generic CRUD Base Structure
* Các REST Controller, Service, ServiceImpl và Repository đều kế thừa từ cấu trúc Generic (`BaseController`, `BaseService`, `BaseServiceImpl`, `BaseRepository`).
* Triển khai nhanh gọn các API CRUD chuẩn hóa mà không phải lặp lại mã nguồn.

### 3. Hibernate 6 Soft Delete
* Áp dụng cơ chế xóa mềm an toàn thông qua các annotation hiện đại `@SQLDelete` và `@SQLRestriction("deleted = false")` (tương thích hoàn toàn với Hibernate 6).
* Ngăn ngừa mất dữ liệu ngoài ý muốn khi có liên kết CSDL phức tạp.

### 4. Hệ thống Phân quyền Chặt chẽ & An toàn
* Sử dụng **Method Security** (`@EnableMethodSecurity` và `@PreAuthorize`).
* Triển khai helper bean chuyên dụng **[SecurityUtils](src/main/java/com/example/edutops/common/security/SecurityUtils.java)** để thực hiện các kiểm tra chính chủ gián tiếp (`isSelfOrAdminTeacher`, `isSelfOrAdminStudent`), triệt tiêu các lỗi logic phân quyền khi ID thực thể khác ID tài khoản User.

### 5. Validation không Hardcode
* Toàn bộ các thông điệp ràng buộc dữ liệu được cấu hình tập trung trong file **`src/main/resources/ValidationMessages.properties`** thông qua định dạng placeholder `{key}`, dễ dàng cho việc quốc tế hóa (i18n) về sau.

---

## 🚀 Hướng dẫn Cài đặt & Chạy ứng dụng

### 1. Khởi động Cơ sở dữ liệu (PostgreSQL)
Hệ thống sử dụng PostgreSQL trên cổng mặc định `5432`. Bạn có thể sử dụng Docker để khởi động nhanh CSDL:
```bash
docker run --name edutops-db -e POSTGRES_DB=edutops -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
```

### 2. Cấu hình Biến môi trường (Environment Variables)
Để bảo vệ an toàn cho các thông tin nhạy cảm, ứng dụng nạp động cấu hình từ môi trường. Hãy khai báo các biến sau trước khi chạy:
```bash
# Mã bí mật dùng ký và xác thực token JWT (Độ dài tối thiểu 256-bit)
export JWT_SECRET=your_super_secret_key_with_at_least_256_bits_length_here

# Client ID ứng dụng Google OAuth2 Client
export GOOGLE_CLIENT_ID=your_google_oauth2_client_id.apps.googleusercontent.com
```
*(Trên Windows PowerShell, sử dụng `$env:JWT_SECRET="..."` và `$env:GOOGLE_CLIENT_ID="..."`)*

### 3. Biên dịch & Khởi động Ứng dụng
Chạy ứng dụng bằng Maven Wrapper:
```bash
# Biên dịch dự án
./mvnw clean compile

# Chạy ứng dụng
./mvnw spring-boot:run
```

---

## 📑 Tài liệu hóa API (Swagger UI)

Khi ứng dụng đang chạy, bạn có thể truy cập Swagger UI để kiểm thử trực tiếp các endpoint REST API:
* **Đường dẫn**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **OpenAPI Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📌 Quy chuẩn Cam kết Mã nguồn (Git Commits)

Dự án áp dụng quy chuẩn **Conventional Commits** để quản lý lịch sử phát triển sạch sẽ:
* `feat(scope)`: Phát triển tính năng mới (ví dụ: `feat(course): implement course endpoints`).
* `fix(scope)`: Sửa lỗi (ví dụ: `fix(security): fix token expiration check`).
* `docs(scope)`: Cập nhật tài liệu (ví dụ: `docs(readme): update setup guide`).
* `refactor(scope)`: Tái cấu trúc mã nguồn không làm thay đổi tính năng.
