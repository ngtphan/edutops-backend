package com.example.edutops.subject.controller;

import com.example.edutops.common.controller.BaseController;
import com.example.edutops.subject.dto.SubjectRequest;
import com.example.edutops.subject.dto.SubjectResponse;
import com.example.edutops.subject.service.SubjectService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subjects")
@Tag(name = "Subjects", description = "API quản lý danh mục Môn học")
public class SubjectController extends BaseController<SubjectRequest, SubjectRequest, SubjectResponse> {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        super(subjectService);
        this.subjectService = subjectService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo mới môn học (Chỉ ADMIN)")
    public ResponseEntity<SubjectResponse> create(@Valid @RequestBody SubjectRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cập nhật môn học theo ID (Chỉ ADMIN)")
    public ResponseEntity<SubjectResponse> update(
            @PathVariable UUID publicId, 
            @Valid @RequestBody SubjectRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa môn học theo ID (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lấy thông tin môn học theo mã code (Tất cả người dùng đã đăng nhập)")
    public ResponseEntity<SubjectResponse> getByCode(@PathVariable String code) {
        SubjectResponse response = subjectService.getByCode(code);
        return ResponseEntity.ok(response);
    }
}
