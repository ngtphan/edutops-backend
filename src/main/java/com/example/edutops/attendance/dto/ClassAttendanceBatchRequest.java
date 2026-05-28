package com.example.edutops.attendance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO yêu cầu điểm danh theo lô cho cả lớp học trong một ngày cụ thể.
 */
public class ClassAttendanceBatchRequest {

    @NotNull
    private UUID schedulePublicId;

    @NotNull
    private LocalDate attendanceDate;

    @NotEmpty
    @Valid
    private List<StudentAttendanceDto> studentAttendances;

    // --- Constructors ---

    public ClassAttendanceBatchRequest() {
    }

    public ClassAttendanceBatchRequest(UUID schedulePublicId, LocalDate attendanceDate, List<StudentAttendanceDto> studentAttendances) {
        this.schedulePublicId = schedulePublicId;
        this.attendanceDate = attendanceDate;
        this.studentAttendances = studentAttendances;
    }

    // --- Getters & Setters ---

    public UUID getSchedulePublicId() {
        return schedulePublicId;
    }

    public void setSchedulePublicId(UUID schedulePublicId) {
        this.schedulePublicId = schedulePublicId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public List<StudentAttendanceDto> getStudentAttendances() {
        return studentAttendances;
    }

    public void setStudentAttendances(List<StudentAttendanceDto> studentAttendances) {
        this.studentAttendances = studentAttendances;
    }
}
