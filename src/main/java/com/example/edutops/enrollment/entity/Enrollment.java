package com.example.edutops.enrollment.entity;

import com.example.edutops.classgroup.entity.ClassGroup;
import com.example.edutops.common.entity.BaseEntity;
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
 * Ghi danh học viên vào lớp.
 * <p>
 * Unique constraint: mỗi học viên chỉ đăng ký 1 lần cho mỗi lớp.
 * </p>
 */
@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_enrollment_student_class",
                columnNames = {"student_id", "class_group_id"}
        )
)
public class Enrollment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_group_id", nullable = false)
    private ClassGroup classGroup;

    @NotNull
    @Column(nullable = false)
    private LocalDate enrolledAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    // --- Constructors ---

    public Enrollment() {
    }

    public Enrollment(Student student, ClassGroup classGroup, LocalDate enrolledAt) {
        this.student = student;
        this.classGroup = classGroup;
        this.enrolledAt = enrolledAt;
        this.status = EnrollmentStatus.ACTIVE;
    }

    // --- Getters & Setters ---

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
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
