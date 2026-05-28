package com.example.edutops.common.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Cấu trúc thông báo lỗi chuẩn hóa (premium error response) trả về client.
 */
public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String errorCode;
    private String error;
    private String message;
    private String path;
    private Map<String, String> details;

    // --- Getters & Setters ---

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
