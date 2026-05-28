package com.example.edutops.enrollment.service;

import com.example.edutops.common.service.BaseService;
import com.example.edutops.enrollment.dto.EnrollmentCreateRequest;
import com.example.edutops.enrollment.dto.EnrollmentUpdateRequest;
import com.example.edutops.enrollment.dto.EnrollmentResponse;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService extends BaseService<EnrollmentCreateRequest, EnrollmentUpdateRequest, EnrollmentResponse> {

    List<EnrollmentResponse> getByStudent(UUID studentPublicId);

    List<EnrollmentResponse> getByClassGroup(UUID classGroupPublicId);
}
