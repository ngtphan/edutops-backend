package com.example.edutops.classgroup.service.impl;

import com.example.edutops.classgroup.dto.ClassGroupCreateRequest;
import com.example.edutops.classgroup.dto.ClassGroupResponse;
import com.example.edutops.classgroup.dto.ClassGroupUpdateRequest;
import com.example.edutops.classgroup.entity.ClassGroup;
import com.example.edutops.classgroup.entity.ClassGroupStatus;
import com.example.edutops.classgroup.repository.ClassGroupRepository;
import com.example.edutops.classgroup.service.ClassGroupService;
import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.course.entity.Course;
import com.example.edutops.course.repository.CourseRepository;
import com.example.edutops.teacher.entity.Teacher;
import com.example.edutops.teacher.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ClassGroupServiceImpl
        extends BaseServiceImpl<ClassGroupCreateRequest, ClassGroupUpdateRequest, ClassGroupResponse, ClassGroup>
        implements ClassGroupService {

    private final ClassGroupRepository classGroupRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final EntityMapper entityMapper;

    public ClassGroupServiceImpl(ClassGroupRepository classGroupRepository,
                                 CourseRepository courseRepository,
                                 TeacherRepository teacherRepository,
                                 EntityMapper entityMapper) {
        super(classGroupRepository);
        this.classGroupRepository = classGroupRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional
    public ClassGroupResponse create(ClassGroupCreateRequest request) {
        if (classGroupRepository.existsByCode(request.getCode())) {
            throw BusinessException.withDetail(ErrorCode.CLASS_GROUP_CODE_ALREADY_EXISTS, request.getCode());
        }

        Course course = courseRepository.findByPublicId(request.getCoursePublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getCoursePublicId()));

        Teacher teacher = teacherRepository.findByPublicId(request.getTeacherPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getTeacherPublicId()));

        validateDateRange(request.getStartDate(), request.getEndDate());

        ClassGroup classGroup = new ClassGroup();
        classGroup.setCode(request.getCode());
        classGroup.setCourse(course);
        classGroup.setTeacher(teacher);
        classGroup.setStartDate(request.getStartDate());
        classGroup.setEndDate(request.getEndDate());
        classGroup.setMaxStudents(request.getMaxStudents());
        classGroup.setStatus(ClassGroupStatus.OPEN);

        ClassGroup saved = classGroupRepository.save(classGroup);
        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public ClassGroupResponse update(UUID publicId, ClassGroupUpdateRequest request) {
        ClassGroup classGroup = classGroupRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        if (!classGroup.getCode().equalsIgnoreCase(request.getCode())
                && classGroupRepository.existsByCodeAndPublicIdNot(request.getCode(), publicId)) {
            throw BusinessException.withDetail(ErrorCode.CLASS_GROUP_CODE_ALREADY_EXISTS, request.getCode());
        }

        Course course = courseRepository.findByPublicId(request.getCoursePublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getCoursePublicId()));

        Teacher teacher = teacherRepository.findByPublicId(request.getTeacherPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getTeacherPublicId()));

        validateDateRange(request.getStartDate(), request.getEndDate());

        classGroup.setCode(request.getCode());
        classGroup.setCourse(course);
        classGroup.setTeacher(teacher);
        classGroup.setStartDate(request.getStartDate());
        classGroup.setEndDate(request.getEndDate());
        classGroup.setMaxStudents(request.getMaxStudents());
        classGroup.setStatus(request.getStatus());

        ClassGroup saved = classGroupRepository.save(classGroup);
        return convertToResponse(saved);
    }

    @Override
    protected ClassGroup convertToEntity(ClassGroupCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected ClassGroupResponse convertToResponse(ClassGroup entity) {
        return entityMapper.toClassGroupResponse(entity);
    }

    @Override
    protected void updateEntityFromRequest(ClassGroup entity, ClassGroupUpdateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm update()");
    }

    /**
     * Kiểm tra ngày bắt đầu phải trước hoặc bằng ngày kết thúc.
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED,
                    "Ngày bắt đầu lớp học phải trước hoặc bằng ngày kết thúc");
        }
    }
}
