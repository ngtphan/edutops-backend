package com.example.edutops.attendance.dto;

import com.example.edutops.attendance.entity.AttendanceStatus;
import com.example.edutops.common.dto.BaseResponse;
import com.example.edutops.schedule.dto.ScheduleResponse;
import com.example.edutops.student.dto.StudentResponse;

import java.time.LocalDate;

public class AttendanceResponse extends BaseResponse {

    private ScheduleResponse schedule;
    private StudentResponse student;
    private LocalDate attendanceDate;
    private AttendanceStatus status;
    private String note;

    // --- Getters & Setters ---

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleResponse schedule) {
        this.schedule = schedule;
    }

    public StudentResponse getStudent() {
        return student;
    }

    public void setStudent(StudentResponse student) {
        this.student = student;
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
