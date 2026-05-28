package com.example.edutops.auth.controller;

import com.example.edutops.auth.dto.GoogleLoginRequest;
import com.example.edutops.auth.dto.TokenResponse;
import com.example.edutops.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "API quản lý Đăng nhập & Xác thực hệ thống")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    @Operation(summary = "Đăng nhập bằng mã Google ID Token từ client")
    public ResponseEntity<TokenResponse> loginWithGoogle(
            @Valid @RequestBody GoogleLoginRequest request) {
        TokenResponse response = authService.loginWithGoogle(request);
        return ResponseEntity.ok(response);
    }
}
