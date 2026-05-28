package com.example.edutops.user.service;

import com.example.edutops.common.service.BaseService;
import com.example.edutops.user.dto.UserCreateRequest;
import com.example.edutops.user.dto.UserResponse;
import com.example.edutops.user.dto.UserUpdateRequest;

import java.util.UUID;

public interface UserService extends BaseService<UserCreateRequest, UserUpdateRequest, UserResponse> {

    /**
     * Tìm kiếm người dùng dựa trên địa chỉ email.
     *
     * @param email Địa chỉ email của người dùng
     * @return Dữ liệu Response DTO người dùng
     */
    UserResponse getByEmail(String email);

    /**
     * Kích hoạt hoặc vô hiệu hóa tài khoản người dùng theo public ID.
     *
     * @param publicId ID công khai dạng UUID
     */
    void toggleActive(UUID publicId);

    /**
     * Thay đổi mật khẩu người dùng theo public ID.
     *
     * @param publicId ID công khai dạng UUID
     * @param newPassword Mật khẩu mới dạng plain text
     */
    void changePassword(UUID publicId, String newPassword);
}
