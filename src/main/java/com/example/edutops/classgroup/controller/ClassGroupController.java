package com.example.edutops.classgroup.controller;

import com.example.edutops.classgroup.dto.ClassGroupCreateRequest;
import com.example.edutops.classgroup.dto.ClassGroupUpdateRequest;
import com.example.edutops.classgroup.dto.ClassGroupResponse;
import com.example.edutops.classgroup.service.ClassGroupService;
import com.example.edutops.common.controller.BaseController;
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
@RequestMapping("/api/v1/class-groups")
@Tag(name = "ClassGroups", description = "API quản lý Lớp học")
public class ClassGroupController extends BaseController<ClassGroupCreateRequest, ClassGroupUpdateRequest, ClassGroupResponse> {

    public ClassGroupController(ClassGroupService classGroupService) {
        super(classGroupService);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER')")
    @Operation(summary = "Tạo mới lớp học (ADMIN hoặc CLASS_MANAGER)")
    public ResponseEntity<ClassGroupResponse> create(@Valid @RequestBody ClassGroupCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER')")
    @Operation(summary = "Cập nhật lớp học theo ID (ADMIN hoặc CLASS_MANAGER)")
    public ResponseEntity<ClassGroupResponse> update(
            @PathVariable UUID publicId,
            @Valid @RequestBody ClassGroupUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa lớp học theo ID (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF', 'TEACHER')")
    @Operation(summary = "Xem chi tiết lớp học (ADMIN, CLASS_MANAGER, STAFF, TEACHER)")
    public ResponseEntity<ClassGroupResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF', 'TEACHER')")
    @Operation(summary = "Lấy danh sách tất cả lớp học (ADMIN, CLASS_MANAGER, STAFF, TEACHER)")
    public ResponseEntity<List<ClassGroupResponse>> getAll() {
        return super.getAll();
    }
}
