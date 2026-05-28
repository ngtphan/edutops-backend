package com.example.edutops.common.exception;

/**
 * Bộ mã lỗi nghiệp vụ chuẩn hóa cho toàn bộ hệ thống EduTopS.
 */
public enum ErrorCode {

    VALIDATION_FAILED("VAL_001", "Dữ liệu đầu vào không hợp lệ"),
    RESOURCE_NOT_FOUND("RES_001", "Không tìm thấy tài nguyên yêu cầu"),
    EMAIL_ALREADY_EXISTS("USR_001", "Email này đã được sử dụng bởi tài khoản khác"),
    SUBJECT_CODE_ALREADY_EXISTS("SUB_001", "Mã môn học đã tồn tại trong hệ thống"),
    SYSTEM_ERROR("SYS_999", "Hệ thống gặp sự cố, vui lòng thử lại sau"),
    GOOGLE_AUTH_FAILED("ATH_001", "Xác thực tài khoản Google không thành công hoặc token hết hạn"),
    INVALID_TOKEN("ATH_002", "Token bảo mật không hợp lệ hoặc đã hết hạn"),
    COURSE_CODE_ALREADY_EXISTS("CRS_001", "Mã khóa học đã tồn tại trong hệ thống");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
