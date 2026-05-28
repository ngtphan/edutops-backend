package com.example.edutops.enrollment.controller;

import com.example.edutops.common.controller.BaseController;
import com.example.edutops.enrollment.dto.EnrollmentCreateRequest;
import com.example.edutops.enrollment.dto.EnrollmentResponse;
import com.example.edutops.enrollment.dto.EnrollmentUpdateRequest;
import com.example.edutops.enrollment.service.EnrollmentService;
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
@RequestMapping("/api/v1/enrollments")
@Tag(name = "Enrollments", description = "API quản lý Ghi danh và Xếp lớp")
public class EnrollmentController extends BaseController<EnrollmentCreateRequest, EnrollmentUpdateRequest, EnrollmentResponse> {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        super(enrollmentService);
        this.enrollmentService = enrollmentService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF') or @securityUtils.isSelfOrAdminStudent(#request.studentPublicId)")
    @Operation(summary = "Ghi danh học viên vào lớp học (ADMIN, CLASS_MANAGER, STAFF hoặc chính chủ học viên)")
    public ResponseEntity<EnrollmentResponse> create(@Valid @RequestBody EnrollmentCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER')")
    @Operation(summary = "Cập nhật trạng thái ghi danh học viên (ADMIN hoặc CLASS_MANAGER)")
    public ResponseEntity<EnrollmentResponse> update(
            @PathVariable UUID publicId,
            @Valid @RequestBody EnrollmentUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa ghi danh học viên (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF')")
    @Operation(summary = "Xem chi tiết ghi danh (ADMIN, CLASS_MANAGER, STAFF)")
    public ResponseEntity<EnrollmentResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF')")
    @Operation(summary = "Lấy danh sách tất cả ghi danh (ADMIN, CLASS_MANAGER, STAFF)")
    public ResponseEntity<List<EnrollmentResponse>> getAll() {
        return super.getAll();
    }

    @GetMapping("/student/{studentPublicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF') or @securityUtils.isSelfOrAdminStudent(#studentPublicId)")
    @Operation(summary = "Lấy danh sách ghi danh của một học viên (ADMIN, CLASS_MANAGER, STAFF hoặc chính chủ học viên)")
    public ResponseEntity<List<EnrollmentResponse>> getByStudent(@PathVariable UUID studentPublicId) {
        List<EnrollmentResponse> list = enrollmentService.getByStudent(studentPublicId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/class-group/{classGroupPublicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF', 'TEACHER')")
    @Operation(summary = "Lấy danh sách học viên ghi danh của một lớp học (ADMIN, CLASS_MANAGER, STAFF, TEACHER)")
    public ResponseEntity<List<EnrollmentResponse>> getByClassGroup(@PathVariable UUID classGroupPublicId) {
        List<EnrollmentResponse> list = enrollmentService.getByClassGroup(classGroupPublicId);
        return ResponseEntity.ok(list);
    }
}
