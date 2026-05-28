package com.example.edutops.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Bộ mã lỗi nghiệp vụ chuẩn hóa cho toàn bộ hệ thống EduTopS.
 * <p>
 * Mỗi mã lỗi bao gồm:
 * <ul>
 *   <li>{@code code}: Mã ngắn gọn dùng cho frontend phân loại lỗi</li>
 *   <li>{@code defaultMessage}: Thông điệp mặc định bằng tiếng Việt</li>
 *   <li>{@code httpStatus}: HTTP status code tương ứng để trả về client đúng ngữ cảnh</li>
 * </ul>
 * </p>
 */
public enum ErrorCode {

    VALIDATION_FAILED("VAL_001", "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND("RES_001", "Không tìm thấy tài nguyên yêu cầu", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("USR_001", "Email này đã được sử dụng bởi tài khoản khác", HttpStatus.CONFLICT),
    SUBJECT_CODE_ALREADY_EXISTS("SUB_001", "Mã môn học đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    SYSTEM_ERROR("SYS_999", "Hệ thống gặp sự cố, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),
    GOOGLE_AUTH_FAILED("ATH_001", "Xác thực tài khoản Google không thành công hoặc token hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("ATH_002", "Token bảo mật không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),
    COURSE_CODE_ALREADY_EXISTS("CRS_001", "Mã khóa học đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    ROOM_NAME_ALREADY_EXISTS("ROM_001", "Tên phòng học đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    CLASS_GROUP_CODE_ALREADY_EXISTS("CLG_001", "Mã lớp học đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    ENROLLMENT_ALREADY_EXISTS("ENR_001", "Học viên này đã được ghi danh vào lớp học này", HttpStatus.CONFLICT),
    CLASS_GROUP_FULL("CLG_002", "Lớp học đã đạt sĩ số tối đa", HttpStatus.valueOf(422)),
    SCHEDULE_CONFLICT("SCH_001", "Xung đột lịch học: Trùng giáo viên hoặc trùng phòng học", HttpStatus.CONFLICT),
    ATTENDANCE_ALREADY_EXISTS("ATT_001", "Bản ghi điểm danh của học viên này trong buổi học này đã tồn tại", HttpStatus.CONFLICT),
    STUDENT_NOT_ENROLLED("ENR_002", "Học viên chưa được ghi danh vào lớp học này", HttpStatus.valueOf(422)),
    USER_LOCKED("USR_002", "Tài khoản của bạn hiện đang bị khóa", HttpStatus.UNAUTHORIZED),
    STUDENT_PROFILE_REQUIRE_STUDENT_ROLE("USR_003", "Chỉ tài khoản học viên mới có quyền hoàn tất hồ sơ học viên", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
