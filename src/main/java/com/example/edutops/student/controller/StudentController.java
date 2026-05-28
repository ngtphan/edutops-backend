package com.example.edutops.student.controller;

import com.example.edutops.common.controller.BaseController;
import com.example.edutops.student.dto.StudentCreateRequest;
import com.example.edutops.student.dto.StudentProfileCompleteRequest;
import com.example.edutops.student.dto.StudentResponse;
import com.example.edutops.student.dto.StudentUpdateRequest;
import com.example.edutops.student.service.StudentService;
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
@RequestMapping("/api/v1/students")
@Tag(name = "Students", description = "API quản lý thông tin Học viên")
public class StudentController extends BaseController<StudentCreateRequest, StudentUpdateRequest, StudentResponse> {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        super(studentService);
        this.studentService = studentService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Tạo mới profile học sinh (ADMIN hoặc STAFF)")
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or @securityUtils.isSelfOrAdminStudent(#publicId)")
    @Operation(summary = "Cập nhật thông tin profile học sinh (ADMIN, STAFF hoặc chính chủ học sinh)")
    public ResponseEntity<StudentResponse> update(
            @PathVariable UUID publicId, 
            @Valid @RequestBody StudentUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa học sinh theo ID (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'CLASS_MANAGER', 'STAFF') or @securityUtils.isSelfOrAdminStudent(#publicId)")
    @Operation(summary = "Xem chi tiết hồ sơ học sinh (ADMIN, TEACHER, CLASS_MANAGER, STAFF hoặc chính chủ học sinh)")
    public ResponseEntity<StudentResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'CLASS_MANAGER', 'STAFF')")
    @Operation(summary = "Lấy danh sách tất cả học sinh (ADMIN, TEACHER, CLASS_MANAGER, STAFF)")
    public ResponseEntity<List<StudentResponse>> getAll() {
        return super.getAll();
    }

    @GetMapping("/user/{userPublicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'CLASS_MANAGER', 'STAFF') or @securityUtils.isSelfOrAdmin(#userPublicId)")
    @Operation(summary = "Lấy thông tin học viên theo public ID tài khoản liên kết (ADMIN, TEACHER, CLASS_MANAGER, STAFF hoặc chính chủ)")
    public ResponseEntity<StudentResponse> getByUserPublicId(@PathVariable UUID userPublicId) {
        StudentResponse response = studentService.getByUserPublicId(userPublicId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{userPublicId}/complete-profile")
    @PreAuthorize("@securityUtils.isSelfOrAdmin(#userPublicId)")
    @Operation(summary = "Hoàn tất thông tin profile học sinh (Chính chủ hoặc ADMIN)")
    public ResponseEntity<StudentResponse> completeProfile(
            @PathVariable UUID userPublicId,
            @Valid @RequestBody StudentProfileCompleteRequest request) {
        StudentResponse response = studentService.completeProfile(userPublicId, request);
        return ResponseEntity.ok(response);
    }
}

