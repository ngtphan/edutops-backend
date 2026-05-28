package com.example.edutops.classgroup.dto;

import com.example.edutops.classgroup.entity.ClassGroupStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public class ClassGroupUpdateRequest {

    @NotBlank(message = "Mã lớp học không được để trống")
    @Size(max = 30, message = "Mã lớp học không được vượt quá 30 ký tự")
    private String code;

    @NotNull(message = "Khóa học liên kết không được để trống")
    private UUID coursePublicId;

    @NotNull(message = "Giáo viên liên kết không được để trống")
    private UUID teacherPublicId;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    private LocalDate endDate;

    @Positive(message = "Sĩ số tối đa phải lớn hơn 0")
    private int maxStudents;

    @NotNull(message = "Trạng thái lớp học không được để trống")
    private ClassGroupStatus status;

    // --- Getters & Setters ---

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getCoursePublicId() {
        return coursePublicId;
    }

    public void setCoursePublicId(UUID coursePublicId) {
        this.coursePublicId = coursePublicId;
    }

    public UUID getTeacherPublicId() {
        return teacherPublicId;
    }

    public void setTeacherPublicId(UUID teacherPublicId) {
        this.teacherPublicId = teacherPublicId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public ClassGroupStatus getStatus() {
        return status;
    }

    public void setStatus(ClassGroupStatus status) {
        this.status = status;
    }
}
