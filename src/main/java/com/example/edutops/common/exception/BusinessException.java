package com.example.edutops.common.exception;

/**
 * Ngoại lệ nghiệp vụ tùy chỉnh cho EduTopS.
 * Mang theo mã lỗi {@link ErrorCode} để phục vụ frontend phân loại lỗi.
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
