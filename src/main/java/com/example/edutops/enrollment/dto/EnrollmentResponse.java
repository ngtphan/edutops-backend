package com.example.edutops.enrollment.dto;

import com.example.edutops.classgroup.dto.ClassGroupResponse;
import com.example.edutops.common.dto.BaseResponse;
import com.example.edutops.enrollment.entity.EnrollmentStatus;
import com.example.edutops.student.dto.StudentResponse;

import java.time.LocalDate;

public class EnrollmentResponse extends BaseResponse {

    private StudentResponse student;
    private ClassGroupResponse classGroup;
    private LocalDate enrolledAt;
    private EnrollmentStatus status;

    // --- Getters & Setters ---

    public StudentResponse getStudent() {
        return student;
    }

    public void setStudent(StudentResponse student) {
        this.student = student;
    }

    public ClassGroupResponse getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroupResponse classGroup) {
        this.classGroup = classGroup;
    }

    public LocalDate getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDate enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}
