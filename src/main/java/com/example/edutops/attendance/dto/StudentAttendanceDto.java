package com.example.edutops.attendance.dto;

import com.example.edutops.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO chứa thông tin điểm danh chi tiết của một học viên.
 */
public class StudentAttendanceDto {

    @NotNull
    private UUID studentPublicId;

    @NotNull
    private AttendanceStatus status;

    private String note;

    // --- Constructors ---

    public StudentAttendanceDto() {
    }

    public StudentAttendanceDto(UUID studentPublicId, AttendanceStatus status, String note) {
        this.studentPublicId = studentPublicId;
        this.status = status;
        this.note = note;
    }

    // --- Getters & Setters ---

    public UUID getStudentPublicId() {
        return studentPublicId;
    }

    public void setStudentPublicId(UUID studentPublicId) {
        this.studentPublicId = studentPublicId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
