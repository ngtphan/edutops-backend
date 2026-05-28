package com.example.edutops.teacher.controller;

import com.example.edutops.common.controller.BaseController;
import com.example.edutops.teacher.dto.TeacherCreateRequest;
import com.example.edutops.teacher.dto.TeacherResponse;
import com.example.edutops.teacher.dto.TeacherUpdateRequest;
import com.example.edutops.teacher.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
@Tag(name = "Teachers", description = "API quản lý thông tin Giáo viên")
public class TeacherController extends BaseController<TeacherCreateRequest, TeacherUpdateRequest, TeacherResponse> {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        super(teacherService);
        this.teacherService = teacherService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo mới profile giáo viên (Chỉ ADMIN)")
    public ResponseEntity<TeacherResponse> create(@Valid @RequestBody TeacherCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isSelfOrAdminTeacher(#publicId)")
    @Operation(summary = "Cập nhật thông tin profile giáo viên (ADMIN hoặc chính chủ giáo viên)")
    public ResponseEntity<TeacherResponse> update(
            @PathVariable UUID publicId, 
            @Valid @RequestBody TeacherUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa giáo viên theo ID (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'CLASS_MANAGER', 'STAFF')")
    @Operation(summary = "Xem chi tiết hồ sơ giáo viên (Người dùng đã đăng nhập)")
    public ResponseEntity<TeacherResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'CLASS_MANAGER', 'STAFF')")
    @Operation(summary = "Lấy danh sách tất cả giáo viên (Người dùng đã đăng nhập)")
    public ResponseEntity<List<TeacherResponse>> getAll() {
        return super.getAll();
    }

    @GetMapping("/user/{userPublicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'CLASS_MANAGER', 'STAFF') or @securityUtils.isSelfOrAdmin(#userPublicId)")
    @Operation(summary = "Lấy thông tin giáo viên theo public ID tài khoản liên kết (ADMIN, TEACHER, CLASS_MANAGER, STAFF hoặc chính chủ)")
    public ResponseEntity<TeacherResponse> getByUserPublicId(@PathVariable UUID userPublicId) {
        TeacherResponse response = teacherService.getByUserPublicId(userPublicId);
        return ResponseEntity.ok(response);
    }
}
