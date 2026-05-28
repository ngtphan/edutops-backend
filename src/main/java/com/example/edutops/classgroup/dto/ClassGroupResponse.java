package com.example.edutops.classgroup.dto;

import com.example.edutops.classgroup.entity.ClassGroupStatus;
import com.example.edutops.common.dto.BaseResponse;
import com.example.edutops.course.dto.CourseResponse;
import com.example.edutops.teacher.dto.TeacherResponse;

import java.time.LocalDate;

public class ClassGroupResponse extends BaseResponse {

    private String code;
    private CourseResponse course;
    private TeacherResponse teacher;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxStudents;
    private ClassGroupStatus status;

    // --- Getters & Setters ---

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CourseResponse getCourse() {
        return course;
    }

    public void setCourse(CourseResponse course) {
        this.course = course;
    }

    public TeacherResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherResponse teacher) {
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
}
