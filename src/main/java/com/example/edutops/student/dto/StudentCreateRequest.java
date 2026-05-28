package com.example.edutops.student.dto;

import com.example.edutops.common.validation.PhoneNumber;
import com.example.edutops.student.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class StudentCreateRequest {

    // --- Thông tin tài khoản User ---

    @NotBlank(message = "{student.email.notblank}")
    @Email(message = "{student.email.invalid}")
    private String email;

    @NotBlank(message = "{student.fullname.notblank}")
    @Size(max = 255, message = "{student.fullname.size}")
    private String fullName;

    @NotBlank(message = "{student.password.notblank}")
    @Size(min = 6, message = "{student.password.size}")
    private String password;

    // --- Thông tin học viên (Student Profile) ---

    @NotBlank(message = "{student.phone.notblank}")
    @PhoneNumber(message = "{student.phone.invalid}")
    private String phoneNumber;

    @NotNull(message = "{student.dob.notnull}")
    @Past(message = "{student.dob.past}")
    private LocalDate dateOfBirth;

    @NotNull(message = "{student.gender.notnull}")
    private Gender gender;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
