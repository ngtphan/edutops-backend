package com.example.edutops.teacher.dto;

import com.example.edutops.common.validation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class TeacherUpdateRequest {

    @NotBlank(message = "{teacher.fullname.notblank}")
    @Size(max = 255, message = "{teacher.fullname.size}")
    private String fullName;

    @NotBlank(message = "{teacher.phone.notblank}")
    @PhoneNumber(message = "{teacher.phone.invalid}")
    private String phoneNumber;

    private String bio;

    private Set<String> subjectPublicIds;

    // --- Getters & Setters ---

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

    public Set<String> getSubjectPublicIds() {
        return subjectPublicIds;
    }

    public void setSubjectPublicIds(Set<String> subjectPublicIds) {
        this.subjectPublicIds = subjectPublicIds;
    }
}
