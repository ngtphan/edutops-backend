package com.example.edutops.attendance.entity;

import com.example.edutops.common.entity.BaseEntity;
import com.example.edutops.schedule.entity.Schedule;
import com.example.edutops.student.entity.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Điểm danh từng buổi học.
 * <p>
 * Unique constraint: mỗi học viên chỉ điểm danh 1 lần
 * cho mỗi buổi học vào mỗi ngày cụ thể.
 * </p>
 */
@Entity
@Table(
        name = "attendances",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_attendance_schedule_student_date",
                columnNames = {"schedule_id", "student_id", "attendance_date"}
        )
)
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull
    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;

    @Column(columnDefinition = "TEXT")
    private String note;

    // --- Constructors ---

    public Attendance() {
    }

    public Attendance(Schedule schedule, Student student,
                      LocalDate attendanceDate, AttendanceStatus status) {
        this.schedule = schedule;
        this.student = student;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    // --- Getters & Setters ---

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
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
