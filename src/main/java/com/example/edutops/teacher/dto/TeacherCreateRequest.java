package com.example.edutops.teacher.dto;

import com.example.edutops.common.validation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class TeacherCreateRequest {

    // --- Thông tin tài khoản User ---

    @NotBlank(message = "{teacher.email.notblank}")
    @Email(message = "{teacher.email.invalid}")
    private String email;

    @NotBlank(message = "{teacher.fullname.notblank}")
    @Size(max = 255, message = "{teacher.fullname.size}")
    private String fullName;

    @NotBlank(message = "{teacher.password.notblank}")
    @Size(min = 6, message = "{teacher.password.size}")
    private String password;

    // --- Thông tin giáo viên (Teacher Profile) ---

    @NotBlank(message = "{teacher.phone.notblank}")
    @PhoneNumber(message = "{teacher.phone.invalid}")
    private String phoneNumber;

    private String bio;

    // Danh sách UUID publicId của các môn học giảng dạy
    private Set<String> subjectPublicIds;

    // --- Getters & Setters ---

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
