package com.example.edutops.attendance.dto;

import com.example.edutops.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public class AttendanceUpdateRequest {

    @NotNull(message = "Trạng thái điểm danh không được để trống")
    private AttendanceStatus status;

    private String note;

    // --- Getters & Setters ---

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
