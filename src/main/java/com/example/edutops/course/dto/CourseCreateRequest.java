package com.example.edutops.course.dto;

import com.example.edutops.course.entity.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class CourseCreateRequest {

    @NotBlank(message = "{course.code.notblank}")
    @Size(max = 20, message = "{course.code.size}")
    private String code;

    @NotBlank(message = "{course.name.notblank}")
    @Size(max = 150, message = "{course.name.size}")
    private String name;

    @NotNull(message = "{course.subject.notnull}")
    private UUID subjectPublicId;

    private String description;

    @Positive(message = "{course.totalsessions.positive}")
    private int totalSessions;

    @NotNull(message = "{course.fee.notnull}")
    @PositiveOrZero(message = "{course.fee.positive}")
    private BigDecimal fee;

    @NotNull(message = "{course.status.notnull}")
    private CourseStatus status = CourseStatus.ACTIVE;

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

    public UUID getSubjectPublicId() {
        return subjectPublicId;
    }

    public void setSubjectPublicId(UUID subjectPublicId) {
        this.subjectPublicId = subjectPublicId;
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
}
