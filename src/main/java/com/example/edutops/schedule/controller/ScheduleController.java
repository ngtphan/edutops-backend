package com.example.edutops.schedule.controller;

import com.example.edutops.common.controller.BaseController;
import com.example.edutops.schedule.dto.ScheduleCreateRequest;
import com.example.edutops.schedule.dto.ScheduleResponse;
import com.example.edutops.schedule.dto.ScheduleUpdateRequest;
import com.example.edutops.schedule.service.ScheduleService;
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
@RequestMapping("/api/v1/schedules")
@Tag(name = "Schedules", description = "API xếp lịch học và Thời khóa biểu")
public class ScheduleController extends BaseController<ScheduleCreateRequest, ScheduleUpdateRequest, ScheduleResponse> {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        super(scheduleService);
        this.scheduleService = scheduleService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER')")
    @Operation(summary = "Tạo mới lịch học / xếp lịch (ADMIN hoặc CLASS_MANAGER)")
    public ResponseEntity<ScheduleResponse> create(@Valid @RequestBody ScheduleCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER')")
    @Operation(summary = "Cập nhật lịch học theo ID (ADMIN hoặc CLASS_MANAGER)")
    public ResponseEntity<ScheduleResponse> update(
            @PathVariable UUID publicId,
            @Valid @RequestBody ScheduleUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa lịch học theo ID (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Xem chi tiết lịch học (Tất cả người dùng đã đăng nhập)")
    public ResponseEntity<ScheduleResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF', 'TEACHER')")
    @Operation(summary = "Lấy danh sách tất cả lịch học (ADMIN, CLASS_MANAGER, STAFF, TEACHER)")
    public ResponseEntity<List<ScheduleResponse>> getAll() {
        return super.getAll();
    }

    @GetMapping("/class-group/{classGroupPublicId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lấy lịch học của một lớp học cụ thể (Tất cả người dùng đã đăng nhập)")
    public ResponseEntity<List<ScheduleResponse>> getByClassGroup(@PathVariable UUID classGroupPublicId) {
        List<ScheduleResponse> list = scheduleService.getByClassGroup(classGroupPublicId);
        return ResponseEntity.ok(list);
    }
}
