package com.example.edutops.common.exception;

/**
 * Ngoại lệ nghiệp vụ tùy chỉnh cho EduTopS.
 * <p>
 * Mang theo mã lỗi {@link ErrorCode} để phục vụ frontend phân loại lỗi.
 * Hỗ trợ 3 cách sử dụng:
 * </p>
 * <ul>
 *   <li>{@code new BusinessException(ErrorCode.XXX)} – dùng defaultMessage</li>
 *   <li>{@code new BusinessException(ErrorCode.XXX, "custom message")} – message tùy chỉnh</li>
 *   <li>{@code BusinessException.withDetail(ErrorCode.XXX, identifier)} – defaultMessage + ": identifier"</li>
 * </ul>
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * Tạo exception với message mặc định từ ErrorCode.
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    /**
     * Tạo exception với message tùy chỉnh (cho những trường hợp cần mô tả chi tiết hơn).
     */
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Factory method tiện ích: Tạo exception với message = defaultMessage + ": " + detail.
     * <p>
     * Ví dụ: {@code BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId)}
     * → "Không tìm thấy tài nguyên yêu cầu: 550e8400-e29b-41d4-a716-446655440000"
     * </p>
     *
     * @param errorCode mã lỗi nghiệp vụ
     * @param detail    thông tin bổ sung (ID, tên, mã, ...)
     * @return BusinessException đã định dạng message
     */
    public static BusinessException withDetail(ErrorCode errorCode, Object detail) {
        String message = errorCode.getDefaultMessage() + ": " + detail;
        return new BusinessException(errorCode, message);
    }
}
