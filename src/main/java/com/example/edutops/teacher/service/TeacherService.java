package com.example.edutops.teacher.service;

import com.example.edutops.common.service.BaseService;
import com.example.edutops.teacher.dto.TeacherCreateRequest;
import com.example.edutops.teacher.dto.TeacherResponse;
import com.example.edutops.teacher.dto.TeacherUpdateRequest;

import java.util.UUID;

public interface TeacherService extends BaseService<TeacherCreateRequest, TeacherUpdateRequest, TeacherResponse> {

    /**
     * Tìm hồ sơ giáo viên dựa trên ID tài khoản liên kết (User).
     *
     * @param userPublicId ID công khai dạng UUID của tài khoản User
     * @return Dữ liệu Response DTO giáo viên
     */
    TeacherResponse getByUserPublicId(UUID userPublicId);
}
