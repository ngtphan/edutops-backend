package com.example.edutops.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO phục vụ nghiệp vụ đổi mật khẩu tài khoản.
 */
public class PasswordChangeRequest {

    @NotBlank(message = "{user.password.notblank}")
    @Size(min = 6, message = "{user.password.size}")
    private String newPassword;

    // --- Getters & Setters ---

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
