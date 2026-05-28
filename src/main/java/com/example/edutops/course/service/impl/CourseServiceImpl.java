package com.example.edutops.course.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.course.dto.CourseCreateRequest;
import com.example.edutops.course.dto.CourseResponse;
import com.example.edutops.course.dto.CourseUpdateRequest;
import com.example.edutops.course.entity.Course;
import com.example.edutops.course.repository.CourseRepository;
import com.example.edutops.course.service.CourseService;
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
    private final EntityMapper entityMapper;

    public CourseServiceImpl(CourseRepository courseRepository, 
                             SubjectRepository subjectRepository,
                             EntityMapper entityMapper) {
        super(courseRepository);
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional
    public CourseResponse create(CourseCreateRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw BusinessException.withDetail(ErrorCode.COURSE_CODE_ALREADY_EXISTS, request.getCode());
        }

        Subject subject = subjectRepository.findByPublicId(request.getSubjectPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getSubjectPublicId()));

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

        course.validate(); // Tự động kiểm tra ràng buộc bằng OOP!

        Course savedCourse = courseRepository.save(course);
        return convertToResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse update(UUID publicId, CourseUpdateRequest request) {
        Course course = courseRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        if (!course.getCode().equalsIgnoreCase(request.getCode()) 
                && courseRepository.existsByCodeAndPublicIdNot(request.getCode(), publicId)) {
            throw BusinessException.withDetail(ErrorCode.COURSE_CODE_ALREADY_EXISTS, request.getCode());
        }

        Subject subject = subjectRepository.findByPublicId(request.getSubjectPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getSubjectPublicId()));

        course.setCode(request.getCode());
        course.setName(request.getName());
        course.setSubject(subject);
        course.setDescription(request.getDescription());
        course.setTotalSessions(request.getTotalSessions());
        course.setFee(request.getFee());
        course.setStatus(request.getStatus());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());

        course.validate(); // Tự động kiểm tra ràng buộc bằng OOP!

        Course savedCourse = courseRepository.save(course);
        return convertToResponse(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getByCode(String code) {
        Course course = courseRepository.findByCode(code)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, code));
        return convertToResponse(course);
    }

    @Override
    protected Course convertToEntity(CourseCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected CourseResponse convertToResponse(Course entity) {
        return entityMapper.toCourseResponse(entity);
    }

    @Override
    protected void updateEntityFromRequest(Course entity, CourseUpdateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm update()");
    }

}
