package com.example.edutops.subject.service;

import com.example.edutops.common.service.BaseService;
import com.example.edutops.subject.dto.SubjectRequest;
import com.example.edutops.subject.dto.SubjectResponse;

public interface SubjectService extends BaseService<SubjectRequest, SubjectRequest, SubjectResponse> {
    
    /**
     * Lấy chi tiết môn học dựa trên mã môn học (code).
     *
     * @param code Mã môn học
     * @return Dữ liệu Response DTO môn học
     */
    SubjectResponse getByCode(String code);
}
