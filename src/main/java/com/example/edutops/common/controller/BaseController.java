package com.example.edutops.common.controller;

import com.example.edutops.common.service.BaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

/**
 * Controller Generic cung cấp sẵn 5 REST API CRUD chuẩn.
 *
 * @param <CRE> DTO Request dùng cho tạo mới
 * @param <UPD> DTO Request dùng cho cập nhật
 * @param <RES> DTO Response trả về
 */
public abstract class BaseController<CRE, UPD, RES> {

    protected final BaseService<CRE, UPD, RES> service;

    protected BaseController(BaseService<CRE, UPD, RES> service) {
        this.service = service;
    }

    /**
     * API Tạo mới bản ghi.
     */
    @PostMapping
    public ResponseEntity<RES> create(@Valid @RequestBody CRE request) {
        RES created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * API Cập nhật bản ghi theo public ID (UUID).
     */
    @PutMapping("/{publicId}")
    public ResponseEntity<RES> update(
            @PathVariable UUID publicId, 
            @Valid @RequestBody UPD request) {
        RES updated = service.update(publicId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * API Lấy chi tiết bản ghi theo public ID (UUID).
     */
    @GetMapping("/{publicId}")
    public ResponseEntity<RES> getByPublicId(@PathVariable UUID publicId) {
        RES response = service.getByPublicId(publicId);
        return ResponseEntity.ok(response);
    }

    /**
     * API Lấy danh sách toàn bộ bản ghi.
     */
    @GetMapping
    public ResponseEntity<List<RES>> getAll() {
        List<RES> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    /**
     * API Xóa bản ghi theo public ID (UUID).
     */
    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        service.delete(publicId);
        return ResponseEntity.noContent().build();
    }
}
