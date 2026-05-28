package com.example.edutops.subject.entity;

import com.example.edutops.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * Môn học — VD: Toán, Lý, IELTS, TOEIC, Giao tiếp...
 * <p>
 * Giáo viên có thể dạy nhiều môn (M:N với Teacher).
 * Mỗi khóa học (Course) thuộc về 1 môn.
 * </p>
 */
@Entity
@Table(name = "subjects")
@SQLDelete(sql = "UPDATE subjects SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Subject extends BaseEntity {

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // --- Constructors ---

    public Subject() {
    }

    public Subject(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
