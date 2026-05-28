package com.example.edutops.attendance.controller;

import com.example.edutops.attendance.dto.AttendanceCreateRequest;
import com.example.edutops.attendance.dto.AttendanceResponse;
import com.example.edutops.attendance.dto.AttendanceUpdateRequest;
import com.example.edutops.attendance.dto.ClassAttendanceBatchRequest;
import com.example.edutops.attendance.service.AttendanceService;
import com.example.edutops.common.annotation.Audit;
import com.example.edutops.common.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendances")
@Tag(name = "Attendances", description = "API quản lý Điểm danh học viên")
public class AttendanceController extends BaseController<AttendanceCreateRequest, AttendanceUpdateRequest, AttendanceResponse> {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        super(attendanceService);
        this.attendanceService = attendanceService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'TEACHER')")
    @Operation(summary = "Tạo mới bản ghi điểm danh (ADMIN, CLASS_MANAGER hoặc TEACHER)")
    public ResponseEntity<AttendanceResponse> create(@Valid @RequestBody AttendanceCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'TEACHER')")
    @Operation(summary = "Cập nhật trạng thái điểm danh (ADMIN, CLASS_MANAGER hoặc TEACHER)")
    public ResponseEntity<AttendanceResponse> update(
            @PathVariable UUID publicId,
            @Valid @RequestBody AttendanceUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa bản ghi điểm danh (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'TEACHER')")
    @Operation(summary = "Xem chi tiết bản ghi điểm danh (ADMIN, CLASS_MANAGER, TEACHER)")
    public ResponseEntity<AttendanceResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'TEACHER')")
    @Operation(summary = "Lấy danh sách tất cả điểm danh (ADMIN, CLASS_MANAGER, TEACHER)")
    public ResponseEntity<List<AttendanceResponse>> getAll() {
        return super.getAll();
    }

    @GetMapping("/student/{studentPublicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'TEACHER') or @securityUtils.isSelfOrAdminStudent(#studentPublicId)")
    @Operation(summary = "Lấy lịch sử điểm danh của một học viên (ADMIN, CLASS_MANAGER, TEACHER hoặc chính chủ học viên)")
    public ResponseEntity<List<AttendanceResponse>> getByStudent(@PathVariable UUID studentPublicId) {
        List<AttendanceResponse> list = attendanceService.getByStudent(studentPublicId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/schedule/{schedulePublicId}/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'TEACHER')")
    @Operation(summary = "Lấy bảng điểm danh của một buổi học theo ngày cụ thể (ADMIN, CLASS_MANAGER, TEACHER)")
    public ResponseEntity<List<AttendanceResponse>> getByScheduleAndDate(
            @PathVariable UUID schedulePublicId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceResponse> list = attendanceService.getByScheduleAndDate(schedulePublicId, date);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/batch")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'TEACHER')")
    @Operation(summary = "Điểm danh theo lô cho cả lớp học (ADMIN, CLASS_MANAGER hoặc TEACHER)")
    @Audit(action = "SAVE_BATCH_ATTENDANCE", entity = "Attendance")
    public ResponseEntity<List<AttendanceResponse>> saveBatch(@Valid @RequestBody ClassAttendanceBatchRequest request) {
        List<AttendanceResponse> list = attendanceService.saveClassAttendanceBatch(request);
        return ResponseEntity.ok(list);
    }
}
