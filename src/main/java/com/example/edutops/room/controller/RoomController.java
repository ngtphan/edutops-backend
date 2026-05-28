package com.example.edutops.room.controller;

import com.example.edutops.common.controller.BaseController;
import com.example.edutops.room.dto.RoomCreateRequest;
import com.example.edutops.room.dto.RoomUpdateRequest;
import com.example.edutops.room.dto.RoomResponse;
import com.example.edutops.room.service.RoomService;
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
@RequestMapping("/api/v1/rooms")
@Tag(name = "Rooms", description = "API quản lý Phòng học")
public class RoomController extends BaseController<RoomCreateRequest, RoomUpdateRequest, RoomResponse> {

    public RoomController(RoomService roomService) {
        super(roomService);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER')")
    @Operation(summary = "Tạo mới phòng học (ADMIN hoặc CLASS_MANAGER)")
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody RoomCreateRequest request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER')")
    @Operation(summary = "Cập nhật phòng học theo ID (ADMIN hoặc CLASS_MANAGER)")
    public ResponseEntity<RoomResponse> update(
            @PathVariable UUID publicId,
            @Valid @RequestBody RoomUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa phòng học theo ID (Chỉ ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        return super.delete(publicId);
    }

    @Override
    @GetMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF', 'TEACHER')")
    @Operation(summary = "Xem chi tiết phòng học (ADMIN, CLASS_MANAGER, STAFF, TEACHER)")
    public ResponseEntity<RoomResponse> getByPublicId(@PathVariable UUID publicId) {
        return super.getByPublicId(publicId);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLASS_MANAGER', 'STAFF', 'TEACHER')")
    @Operation(summary = "Lấy danh sách tất cả phòng học (ADMIN, CLASS_MANAGER, STAFF, TEACHER)")
    public ResponseEntity<List<RoomResponse>> getAll() {
        return super.getAll();
    }
}
