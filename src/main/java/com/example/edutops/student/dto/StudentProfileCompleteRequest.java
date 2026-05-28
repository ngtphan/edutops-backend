package com.example.edutops.student.dto;

import com.example.edutops.common.validation.PhoneNumber;
import com.example.edutops.student.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

/**
 * DTO chứa thông tin học viên nhập lên để hoàn tất hồ sơ cá nhân sau khi đăng nhập Google.
 */
public class StudentProfileCompleteRequest {

    @NotBlank(message = "{student.phone.notblank}")
    @PhoneNumber(message = "{student.phone.invalid}")
    private String phoneNumber;

    @NotNull(message = "{student.dob.notnull}")
    @Past(message = "{student.dob.past}")
    private LocalDate dateOfBirth;

    @NotNull(message = "{student.gender.notnull}")
    private Gender gender;

    // --- Getters & Setters ---

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
