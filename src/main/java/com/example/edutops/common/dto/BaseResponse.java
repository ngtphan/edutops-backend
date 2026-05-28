package com.example.edutops.common.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Lớp cơ sở cho tất cả Response DTO trả về từ API.
 * Ẩn ID nội bộ (Long id) và hiển thị ID công khai (publicId) cùng thông tin audit.
 */
public abstract class BaseResponse {
    
    private UUID publicId;
    private Instant createdAt;
    private Instant updatedAt;

    // --- Getters & Setters ---

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
