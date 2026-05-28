package com.example.edutops.course.service;

import com.example.edutops.common.service.BaseService;
import com.example.edutops.course.dto.CourseCreateRequest;
import com.example.edutops.course.dto.CourseResponse;
import com.example.edutops.course.dto.CourseUpdateRequest;

import java.util.UUID;

public interface CourseService extends BaseService<CourseCreateRequest, CourseUpdateRequest, CourseResponse> {

    /**
     * Lấy thông tin chi tiết một khóa học theo mã khóa học.
     *
     * @param code Mã khóa học
     * @return Thông tin khóa học Response DTO
     */
    CourseResponse getByCode(String code);
}
