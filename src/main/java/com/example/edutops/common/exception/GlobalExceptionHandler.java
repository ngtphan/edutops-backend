package com.example.edutops.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Xử lý lỗi toàn cục và định dạng lại cấu trúc lỗi trả về client thống nhất.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Bắt lỗi Validation dữ liệu đầu vào trên DTO (@Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            details.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Instant.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setErrorCode(ErrorCode.VALIDATION_FAILED.getCode());
        response.setError("Lỗi Dữ Liệu Đầu Vào");
        response.setMessage(ErrorCode.VALIDATION_FAILED.getDefaultMessage());
        response.setPath(request.getRequestURI());
        response.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Bắt lỗi Nghiệp vụ của hệ thống (BusinessException).
     * HTTP status code được lấy từ {@link ErrorCode#getHttpStatus()} thay vì hardcode BAD_REQUEST.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        HttpStatus httpStatus = ex.getErrorCode().getHttpStatus();

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Instant.now());
        response.setStatus(httpStatus.value());
        response.setErrorCode(ex.getErrorCode().getCode());
        response.setError("Lỗi Nghiệp Vụ");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());

        return ResponseEntity.status(httpStatus).body(response);
    }

    /**
     * Bắt tất cả ngoại lệ hệ thống ngoài dự kiến.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Instant.now());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setErrorCode(ErrorCode.SYSTEM_ERROR.getCode());
        response.setError("Lỗi Hệ Thống");
        response.setMessage(ErrorCode.SYSTEM_ERROR.getDefaultMessage());
        response.setPath(request.getRequestURI());

        // Sử dụng SLF4J structured logging thay vì ex.printStackTrace()
        log.error("Unhandled exception at path [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
