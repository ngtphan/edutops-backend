package com.example.edutops.course.controller;

import com.example.edutops.common.controller.BaseController;
import com.example.edutops.course.dto.CourseCreateRequest;
import com.example.edutops.course.dto.CourseResponse;
import com.example.edutops.course.dto.CourseUpdateRequest;
import com.example.edutops.course.service.CourseService;
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
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "API quản lý thông tin Khóa học")
public class CourseController extends BaseController<CourseCreateRequest, CourseUpdateRequest, CourseResponse> {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        super(courseService);
        this.courseService = courseService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo mới khóa học (Chỉ ADMIN)")
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cập nhật khóa học theo ID (Chỉ ADMIN)")
    public ResponseEntity<CourseResponse> update(
            @PathVariable UUID publicId, 
            @Valid @RequestBody CourseUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa khóa học theo ID (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Xem chi tiết khóa học theo ID (Tất cả người dùng đã đăng nhập)")
    public ResponseEntity<CourseResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lấy danh sách tất cả khóa học (Tất cả người dùng đã đăng nhập)")
    public ResponseEntity<List<CourseResponse>> getAll() {
        return super.getAll();
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lấy thông tin khóa học theo mã code (Tất cả người dùng đã đăng nhập)")
    public ResponseEntity<CourseResponse> getByCode(@PathVariable String code) {
        CourseResponse response = courseService.getByCode(code);
        return ResponseEntity.ok(response);
    }
}
