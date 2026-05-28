package com.example.edutops.enrollment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class EnrollmentCreateRequest {

    @NotNull(message = "Học viên không được để trống")
    private UUID studentPublicId;

    @NotNull(message = "Lớp học không được để trống")
    private UUID classGroupPublicId;

    @NotNull(message = "Ngày ghi danh không được để trống")
    private LocalDate enrolledAt;

    // --- Getters & Setters ---

    public UUID getStudentPublicId() {
        return studentPublicId;
    }

    public void setStudentPublicId(UUID studentPublicId) {
        this.studentPublicId = studentPublicId;
    }

    public UUID getClassGroupPublicId() {
        return classGroupPublicId;
    }

    public void setClassGroupPublicId(UUID classGroupPublicId) {
        this.classGroupPublicId = classGroupPublicId;
    }

    public LocalDate getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDate enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
