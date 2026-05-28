package com.example.edutops.course.dto;

import com.example.edutops.common.dto.BaseResponse;
import com.example.edutops.course.entity.CourseStatus;
import com.example.edutops.subject.dto.SubjectResponse;

import java.math.BigDecimal;

public class CourseResponse extends BaseResponse {

    private String code;
    private String name;
    private String description;
    private int totalSessions;
    private BigDecimal fee;
    private CourseStatus status;
    private SubjectResponse subject;

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

    public SubjectResponse getSubject() {
        return subject;
    }

    public void setSubject(SubjectResponse subject) {
        this.subject = subject;
    }
}
