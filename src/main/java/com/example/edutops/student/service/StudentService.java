package com.example.edutops.student.service;

import com.example.edutops.common.service.BaseService;
import com.example.edutops.student.dto.StudentCreateRequest;
import com.example.edutops.student.dto.StudentResponse;
import com.example.edutops.student.dto.StudentUpdateRequest;

import java.util.UUID;

public interface StudentService extends BaseService<StudentCreateRequest, StudentUpdateRequest, StudentResponse> {

    /**
     * Tìm kiếm thông tin học viên dựa trên public ID tài khoản liên kết (User).
     *
     * @param userPublicId ID công khai dạng UUID của tài khoản User
     * @return Dữ liệu Response DTO học viên
     */
    StudentResponse getByUserPublicId(UUID userPublicId);
}
