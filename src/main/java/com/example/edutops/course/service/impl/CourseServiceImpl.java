package com.example.edutops.course.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.course.dto.CourseCreateRequest;
import com.example.edutops.course.dto.CourseResponse;
import com.example.edutops.course.dto.CourseUpdateRequest;
import com.example.edutops.course.entity.Course;
import com.example.edutops.course.repository.CourseRepository;
import com.example.edutops.course.service.CourseService;
import com.example.edutops.subject.dto.SubjectResponse;
import com.example.edutops.subject.entity.Subject;
import com.example.edutops.subject.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseServiceImpl 
        extends BaseServiceImpl<CourseCreateRequest, CourseUpdateRequest, CourseResponse, Course> 
        implements CourseService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    public CourseServiceImpl(CourseRepository courseRepository, SubjectRepository subjectRepository) {
        super(courseRepository);
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional
    public CourseResponse create(CourseCreateRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new BusinessException(ErrorCode.COURSE_CODE_ALREADY_EXISTS, 
                    "Mã khóa học '" + request.getCode() + "' đã tồn tại trong hệ thống");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, 
                    "Ngày bắt đầu khóa học phải trước hoặc bằng ngày kết thúc");
        }

        Subject subject = subjectRepository.findByPublicId(request.getSubjectPublicId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy môn học liên kết có ID: " + request.getSubjectPublicId()));

        Course course = new Course();
        course.setCode(request.getCode());
        course.setName(request.getName());
        course.setSubject(subject);
        course.setDescription(request.getDescription());
        course.setTotalSessions(request.getTotalSessions());
        course.setFee(request.getFee());
        course.setStatus(request.getStatus());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());

        Course savedCourse = courseRepository.save(course);
        return convertToResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse update(UUID publicId, CourseUpdateRequest request) {
        Course course = courseRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy khóa học với ID: " + publicId));

        if (!course.getCode().equalsIgnoreCase(request.getCode()) 
                && courseRepository.existsByCodeAndPublicIdNot(request.getCode(), publicId)) {
            throw new BusinessException(ErrorCode.COURSE_CODE_ALREADY_EXISTS, 
                    "Mã khóa học '" + request.getCode() + "' đã tồn tại trong hệ thống");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, 
                    "Ngày bắt đầu khóa học phải trước hoặc bằng ngày kết thúc");
        }

        Subject subject = subjectRepository.findByPublicId(request.getSubjectPublicId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy môn học liên kết có ID: " + request.getSubjectPublicId()));

        course.setCode(request.getCode());
        course.setName(request.getName());
        course.setSubject(subject);
        course.setDescription(request.getDescription());
        course.setTotalSessions(request.getTotalSessions());
        course.setFee(request.getFee());
        course.setStatus(request.getStatus());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());

        Course savedCourse = courseRepository.save(course);
        return convertToResponse(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getByCode(String code) {
        Course course = courseRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy khóa học với mã: " + code));
        return convertToResponse(course);
    }

    @Override
    protected Course convertToEntity(CourseCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected CourseResponse convertToResponse(Course entity) {
        CourseResponse response = new CourseResponse();
        response.setPublicId(entity.getPublicId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setTotalSessions(entity.getTotalSessions());
        response.setFee(entity.getFee());
        response.setStatus(entity.getStatus());
        response.setStartDate(entity.getStartDate());
        response.setEndDate(entity.getEndDate());

        if (entity.getSubject() != null) {
            SubjectResponse subRes = new SubjectResponse();
            subRes.setPublicId(entity.getSubject().getPublicId());
            subRes.setCreatedAt(entity.getSubject().getCreatedAt());
            subRes.setUpdatedAt(entity.getSubject().getUpdatedAt());
            subRes.setCode(entity.getSubject().getCode());
            subRes.setName(entity.getSubject().getName());
            subRes.setDescription(entity.getSubject().getDescription());
            response.setSubject(subRes);
        }

        return response;
    }

    @Override
    protected void updateEntityFromRequest(Course entity, CourseUpdateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm update()");
    }
}
