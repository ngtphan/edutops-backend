package com.example.edutops.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO nhận mã Google ID Token gửi từ client.
 */
public class GoogleLoginRequest {

    @NotBlank(message = "{auth.google.idtoken.notblank}")
    private String idToken;

    // --- Getters & Setters ---

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
