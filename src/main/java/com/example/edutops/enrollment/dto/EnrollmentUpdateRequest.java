package com.example.edutops.enrollment.dto;

import com.example.edutops.enrollment.entity.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;

public class EnrollmentUpdateRequest {

    @NotNull(message = "Trạng thái ghi danh không được để trống")
    private EnrollmentStatus status;

    // --- Getters & Setters ---

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}
