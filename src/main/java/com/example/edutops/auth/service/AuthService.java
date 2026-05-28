package com.example.edutops.auth.service;

import com.example.edutops.auth.dto.GoogleLoginRequest;
import com.example.edutops.auth.dto.TokenResponse;

public interface AuthService {

    /**
     * Xác thực thông tin qua Google ID Token và sinh token JWT hệ thống.
     *
     * @param request Yêu cầu đăng nhập chứa Google ID Token
     * @return Dữ liệu token JWT và thông tin người dùng
     */
    TokenResponse loginWithGoogle(GoogleLoginRequest request);
}
