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

    /**
     * Hoàn tất thông tin hồ sơ học viên cho người dùng đăng nhập bằng Google.
     *
     * @param userPublicId ID công khai dạng UUID của tài khoản User
     * @param request Dữ liệu nhập lên để hoàn tất hồ sơ
     * @return Dữ liệu Response DTO học viên sau khi lưu thành công
     */
    StudentResponse completeProfile(UUID userPublicId, com.example.edutops.student.dto.StudentProfileCompleteRequest request);
}
