package com.example.edutops.attendance.dto;

import com.example.edutops.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class AttendanceCreateRequest {

    @NotNull(message = "Lịch học không được để trống")
    private UUID schedulePublicId;

    @NotNull(message = "Học viên không được để trống")
    private UUID studentPublicId;

    @NotNull(message = "Ngày điểm danh không được để trống")
    private LocalDate attendanceDate;

    @NotNull(message = "Trạng thái điểm danh không được để trống")
    private AttendanceStatus status;

    private String note;

    // --- Getters & Setters ---

    public UUID getSchedulePublicId() {
        return schedulePublicId;
    }

    public void setSchedulePublicId(UUID schedulePublicId) {
        this.schedulePublicId = schedulePublicId;
    }

    public UUID getStudentPublicId() {
        return studentPublicId;
    }

    public void setStudentPublicId(UUID studentPublicId) {
        this.studentPublicId = studentPublicId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
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
