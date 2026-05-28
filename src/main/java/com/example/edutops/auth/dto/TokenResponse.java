package com.example.edutops.auth.dto;

/**
 * Response DTO trả về token JWT của hệ thống và thông tin tài khoản cơ bản.
 */
public class TokenResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private String email;
    private String fullName;
    private String role;
    private boolean profileCompleted;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken, String email, String fullName, String role, boolean profileCompleted) {
        this.accessToken = accessToken;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.profileCompleted = profileCompleted;
    }

    // --- Getters & Setters ---

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }
}
