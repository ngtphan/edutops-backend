package com.example.edutops.classgroup.entity;

import com.example.edutops.common.entity.BaseEntity;
import com.example.edutops.course.entity.Course;
import com.example.edutops.teacher.entity.Teacher;
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

import java.time.LocalDate;

/**
 * Lớp học cụ thể — một instance của Course, do một Teacher phụ trách.
 * <p>
 * VD: Lớp "TOAN10-K1-2025" thuộc khóa "Toán lớp 10", giáo viên Nguyễn Văn A.
 * </p>
 */
@Entity
@Table(name = "class_groups")
public class ClassGroup extends BaseEntity {

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Positive
    @Column(nullable = false)
    private int maxStudents;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClassGroupStatus status = ClassGroupStatus.OPEN;

    // --- Constructors ---

    public ClassGroup() {
    }

    public ClassGroup(String code, Course course, Teacher teacher,
                      LocalDate startDate, int maxStudents) {
        this.code = code;
        this.course = course;
        this.teacher = teacher;
        this.startDate = startDate;
        this.maxStudents = maxStudents;
        this.status = ClassGroupStatus.OPEN;
    }

    // --- Getters & Setters ---

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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

    /**
     * Tự xác thực các ràng buộc logic của lớp học (OOP - Rich Domain Model).
     */
    public void validate() {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new com.example.edutops.common.exception.BusinessException(
                    com.example.edutops.common.exception.ErrorCode.VALIDATION_FAILED, 
                    "Ngày bắt đầu lớp học phải trước hoặc bằng ngày kết thúc");
        }
    }

    /**
     * Kiểm tra sĩ số lớp học có vượt quá giới hạn hay không (OOP - Rich Domain Model).
     */
    public void checkCapacity(long currentActiveCount) {
        if (currentActiveCount >= this.maxStudents) {
            throw com.example.edutops.common.exception.BusinessException.withDetail(
                    com.example.edutops.common.exception.ErrorCode.CLASS_GROUP_FULL, 
                    this.code + " (Max: " + this.maxStudents + ")"
            );
        }
    }

    /**
     * Lấy tên giáo viên phụ trách một cách an toàn (OOP - Encapsulation).
     */
    public String getTeacherName() {
        if (this.teacher != null && this.teacher.getUser() != null) {
            return this.teacher.getUser().getFullName();
        }
        return "Giáo viên chưa phân công";
    }
}
