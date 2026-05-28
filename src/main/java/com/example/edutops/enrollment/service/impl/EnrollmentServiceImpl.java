package com.example.edutops.enrollment.service.impl;

import com.example.edutops.classgroup.entity.ClassGroup;
import com.example.edutops.classgroup.repository.ClassGroupRepository;
import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.enrollment.dto.EnrollmentCreateRequest;
import com.example.edutops.enrollment.dto.EnrollmentResponse;
import com.example.edutops.enrollment.dto.EnrollmentUpdateRequest;
import com.example.edutops.enrollment.entity.Enrollment;
import com.example.edutops.enrollment.entity.EnrollmentStatus;
import com.example.edutops.enrollment.repository.EnrollmentRepository;
import com.example.edutops.enrollment.service.EnrollmentService;
import com.example.edutops.student.entity.Student;
import com.example.edutops.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl
        extends BaseServiceImpl<EnrollmentCreateRequest, EnrollmentUpdateRequest, EnrollmentResponse, Enrollment>
        implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ClassGroupRepository classGroupRepository;
    private final EntityMapper entityMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 StudentRepository studentRepository,
                                 ClassGroupRepository classGroupRepository,
                                 EntityMapper entityMapper) {
        super(enrollmentRepository);
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.classGroupRepository = classGroupRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional
    public EnrollmentResponse create(EnrollmentCreateRequest request) {
        Student student = studentRepository.findByPublicId(request.getStudentPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getStudentPublicId()));

        ClassGroup classGroup = classGroupRepository.findByPublicId(request.getClassGroupPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getClassGroupPublicId()));

        if (enrollmentRepository.existsByStudentIdAndClassGroupId(student.getId(), classGroup.getId())) {
            throw new BusinessException(ErrorCode.ENROLLMENT_ALREADY_EXISTS);
        }

        long activeStudents = enrollmentRepository.countActiveStudentsInClass(classGroup.getId());
        if (activeStudents >= classGroup.getMaxStudents()) {
            throw BusinessException.withDetail(ErrorCode.CLASS_GROUP_FULL, 
                    classGroup.getCode() + " (Max: " + classGroup.getMaxStudents() + ")");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setClassGroup(classGroup);
        enrollment.setEnrolledAt(request.getEnrolledAt());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public EnrollmentResponse update(UUID publicId, EnrollmentUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        enrollment.setStatus(request.getStatus());
        Enrollment saved = enrollmentRepository.save(enrollment);
        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByStudent(UUID studentPublicId) {
        return enrollmentRepository.findByStudentPublicId(studentPublicId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByClassGroup(UUID classGroupPublicId) {
        return enrollmentRepository.findByClassGroupPublicId(classGroupPublicId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    protected Enrollment convertToEntity(EnrollmentCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected EnrollmentResponse convertToResponse(Enrollment entity) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setPublicId(entity.getPublicId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setEnrolledAt(entity.getEnrolledAt());
        response.setStatus(entity.getStatus());
        response.setStudent(entityMapper.toStudentResponse(entity.getStudent()));
        response.setClassGroup(entityMapper.toClassGroupResponse(entity.getClassGroup()));
        return response;
    }

    @Override
    protected void updateEntityFromRequest(Enrollment entity, EnrollmentUpdateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm update()");
    }
}
