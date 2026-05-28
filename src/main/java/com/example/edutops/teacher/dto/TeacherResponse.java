package com.example.edutops.teacher.dto;

import com.example.edutops.common.dto.BaseResponse;
import com.example.edutops.subject.dto.SubjectResponse;

import java.util.Set;
import java.util.UUID;

public class TeacherResponse extends BaseResponse {

    private UUID userPublicId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String bio;
    private boolean active;
    private Set<SubjectResponse> subjects;

    // --- Getters & Setters ---

    public UUID getUserPublicId() {
        return userPublicId;
    }

    public void setUserPublicId(UUID userPublicId) {
        this.userPublicId = userPublicId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<SubjectResponse> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectResponse> subjects) {
        this.subjects = subjects;
    }
}
