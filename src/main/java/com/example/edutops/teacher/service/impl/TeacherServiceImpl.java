package com.example.edutops.teacher.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.subject.entity.Subject;
import com.example.edutops.subject.repository.SubjectRepository;
import com.example.edutops.teacher.dto.TeacherCreateRequest;
import com.example.edutops.teacher.dto.TeacherResponse;
import com.example.edutops.teacher.dto.TeacherUpdateRequest;
import com.example.edutops.teacher.entity.Teacher;
import com.example.edutops.teacher.repository.TeacherRepository;
import com.example.edutops.teacher.service.TeacherService;
import com.example.edutops.user.entity.User;
import com.example.edutops.user.entity.UserRole;
import com.example.edutops.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.edutops.common.annotation.Audit;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class TeacherServiceImpl 
        extends BaseServiceImpl<TeacherCreateRequest, TeacherUpdateRequest, TeacherResponse, Teacher> 
        implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;

    public TeacherServiceImpl(TeacherRepository teacherRepository, 
                              UserRepository userRepository, 
                              SubjectRepository subjectRepository,
                              EntityMapper entityMapper,
                              PasswordEncoder passwordEncoder) {
        super(teacherRepository);
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.entityMapper = entityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    @Audit(action = "CREATE_TEACHER", entity = "Teacher")
    public TeacherResponse create(TeacherCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw BusinessException.withDetail(ErrorCode.EMAIL_ALREADY_EXISTS, request.getEmail());
        }

        // 1. Tạo tài khoản User liên kết cho giáo viên
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.TEACHER);
        user.setActive(true);
        User savedUser = userRepository.save(user);

        // 2. Tạo profile Teacher
        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setPhoneNumber(request.getPhoneNumber());
        teacher.setBio(request.getBio());

        // 3. Liên kết các môn học giảng dạy
        teacher.setSubjects(resolveSubjects(request.getSubjectPublicIds()));

        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToResponse(savedTeacher);
    }

    @Override
    @Transactional
    @Audit(action = "UPDATE_TEACHER", entity = "Teacher")
    public TeacherResponse update(UUID publicId, TeacherUpdateRequest request) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        // Cập nhật họ tên của User liên kết
        User user = teacher.getUser();
        user.setFullName(request.getFullName());
        userRepository.save(user);

        // Cập nhật thông tin profile Teacher
        teacher.setPhoneNumber(request.getPhoneNumber());
        teacher.setBio(request.getBio());

        // Cập nhật các môn học giảng dạy mới
        teacher.setSubjects(resolveSubjects(request.getSubjectPublicIds()));

        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToResponse(savedTeacher);
    }

    @Override
    @Transactional
    @Audit(action = "DELETE_TEACHER", entity = "Teacher")
    public void delete(UUID publicId) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        User user = teacher.getUser();

        // Xóa profile Teacher trước
        teacherRepository.delete(teacher);

        // Xóa tài khoản User tương ứng để tránh rác
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherResponse getByUserPublicId(UUID userPublicId) {
        Teacher teacher = teacherRepository.findByUserPublicId(userPublicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, userPublicId));
        return convertToResponse(teacher);
    }

    @Override
    protected Teacher convertToEntity(TeacherCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected TeacherResponse convertToResponse(Teacher entity) {
        return entityMapper.toTeacherResponse(entity);
    }

    @Override
    protected void updateEntityFromRequest(Teacher entity, TeacherUpdateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm update()");
    }

    /**
     * Resolve danh sách subject public IDs thành Set<Subject> entities.
     * Tái sử dụng giữa create() và update() để tránh duplicate code.
     */
    private Set<Subject> resolveSubjects(Set<String> subjectPublicIds) {
        Set<Subject> subjects = new HashSet<>();
        if (subjectPublicIds != null && !subjectPublicIds.isEmpty()) {
            for (String subPublicId : subjectPublicIds) {
                Subject subject = subjectRepository.findByPublicId(UUID.fromString(subPublicId))
                        .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, subPublicId));
                subjects.add(subject);
            }
        }
        return subjects;
    }
}
