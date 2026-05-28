package com.example.edutops.user.controller;

import com.example.edutops.common.controller.BaseController;
import com.example.edutops.user.dto.PasswordChangeRequest;
import com.example.edutops.user.dto.UserCreateRequest;
import com.example.edutops.user.dto.UserResponse;
import com.example.edutops.user.dto.UserUpdateRequest;
import com.example.edutops.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API quản lý thông tin Tài khoản người dùng")
public class UserController extends BaseController<UserCreateRequest, UserUpdateRequest, UserResponse> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo mới tài khoản (Chỉ ADMIN)")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cập nhật tài khoản theo ID (Chỉ ADMIN)")
    public ResponseEntity<UserResponse> update(
            @PathVariable UUID publicId, 
            @Valid @RequestBody UserUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa tài khoản theo ID (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xem thông tin chi tiết tài khoản theo ID (Chỉ ADMIN)")
    public ResponseEntity<UserResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lấy danh sách tất cả tài khoản (Chỉ ADMIN)")
    public ResponseEntity<List<UserResponse>> getAll() {
        return super.getAll();
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lấy thông tin tài khoản theo email (Chỉ ADMIN)")
    public ResponseEntity<UserResponse> getByEmail(@PathVariable String email) {
        UserResponse response = userService.getByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{publicId}/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kích hoạt hoặc khóa tài khoản (Chỉ ADMIN)")
    public ResponseEntity<Void> toggleActive(@PathVariable UUID publicId) {
        userService.toggleActive(publicId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{publicId}/change-password")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isSelfOrAdmin(#publicId)")
    @Operation(summary = "Thay đổi mật khẩu tài khoản (ADMIN hoặc chính chủ tài khoản)")
    public ResponseEntity<Void> changePassword(
            @PathVariable UUID publicId,
            @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(publicId, request.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
