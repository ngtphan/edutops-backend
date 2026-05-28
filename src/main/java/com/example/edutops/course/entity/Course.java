package com.example.edutops.course.entity;

import com.example.edutops.common.entity.BaseEntity;
import com.example.edutops.subject.entity.Subject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

/**
 * Khóa học — VD: "Toán lớp 10 - Nâng cao", "IELTS 6.5".
 * <p>
 * Mỗi khóa học thuộc 1 môn (Subject).
 * Từ 1 khóa học có thể mở nhiều lớp (ClassGroup).
 * </p>
 */
@Entity
@Table(name = "courses")
@SQLDelete(sql = "UPDATE courses SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Course extends BaseEntity {

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Positive
    @Column(nullable = false)
    private int totalSessions;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal fee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseStatus status = CourseStatus.ACTIVE;

    @NotNull
    @Column(nullable = false)
    private java.time.LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private java.time.LocalDate endDate;

    // --- Constructors ---

    public Course() {
    }

    public Course(String code, String name, Subject subject, int totalSessions, BigDecimal fee, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        this.code = code;
        this.name = name;
        this.subject = subject;
        this.totalSessions = totalSessions;
        this.fee = fee;
        this.status = CourseStatus.ACTIVE;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // --- Getters & Setters ---

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public java.time.LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(java.time.LocalDate startDate) {
        this.startDate = startDate;
    }

    public java.time.LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(java.time.LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Tự xác thực các ràng buộc logic của khóa học (OOP - Rich Domain Model).
     */
    public void validate() {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new com.example.edutops.common.exception.BusinessException(
                    com.example.edutops.common.exception.ErrorCode.VALIDATION_FAILED, 
                    "Ngày bắt đầu khóa học phải trước hoặc bằng ngày kết thúc");
        }
    }
}
