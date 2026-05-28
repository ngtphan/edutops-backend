package com.example.edutops.user.dto;

import com.example.edutops.common.dto.BaseResponse;
import com.example.edutops.user.entity.UserRole;

public class UserResponse extends BaseResponse {

    private String email;
    private String fullName;
    private UserRole role;
    private boolean active;

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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
