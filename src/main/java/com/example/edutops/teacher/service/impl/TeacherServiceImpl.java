package com.example.edutops.teacher.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.subject.dto.SubjectResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl 
        extends BaseServiceImpl<TeacherCreateRequest, TeacherUpdateRequest, TeacherResponse, Teacher> 
        implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, 
                              UserRepository userRepository, 
                              SubjectRepository subjectRepository) {
        super(teacherRepository);
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional
    public TeacherResponse create(TeacherCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, 
                    "Email '" + request.getEmail() + "' đã được sử dụng bởi tài khoản khác");
        }

        // 1. Tạo tài khoản User liên kết cho giáo viên
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash("HASHED_" + request.getPassword()); // Giả lập băm mật khẩu
        user.setRole(UserRole.TEACHER);
        user.setActive(true);
        User savedUser = userRepository.save(user);

        // 2. Tạo profile Teacher
        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setPhoneNumber(request.getPhoneNumber());
        teacher.setBio(request.getBio());

        // 3. Liên kết các môn học giảng dạy
        if (request.getSubjectPublicIds() != null && !request.getSubjectPublicIds().isEmpty()) {
            Set<Subject> subjects = new HashSet<>();
            for (String subPublicId : request.getSubjectPublicIds()) {
                Subject subject = subjectRepository.findByPublicId(UUID.fromString(subPublicId))
                        .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                                "Không tìm thấy môn học giảng dạy có ID: " + subPublicId));
                subjects.add(subject);
            }
            teacher.setSubjects(subjects);
        }

        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToResponse(savedTeacher);
    }

    @Override
    @Transactional
    public TeacherResponse update(UUID publicId, TeacherUpdateRequest request) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy giáo viên với ID: " + publicId));

        // Cập nhật họ tên của User liên kết
        User user = teacher.getUser();
        user.setFullName(request.getFullName());
        userRepository.save(user);

        // Cập nhật thông tin profile Teacher
        teacher.setPhoneNumber(request.getPhoneNumber());
        teacher.setBio(request.getBio());

        // Cập nhật các môn học giảng dạy mới
        Set<Subject> subjects = new HashSet<>();
        if (request.getSubjectPublicIds() != null && !request.getSubjectPublicIds().isEmpty()) {
            for (String subPublicId : request.getSubjectPublicIds()) {
                Subject subject = subjectRepository.findByPublicId(UUID.fromString(subPublicId))
                        .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                                "Không tìm thấy môn học giảng dạy có ID: " + subPublicId));
                subjects.add(subject);
            }
        }
        teacher.setSubjects(subjects);

        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToResponse(savedTeacher);
    }

    @Override
    @Transactional
    public void delete(UUID publicId) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy giáo viên với ID: " + publicId));

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
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy giáo viên ứng với tài khoản có ID: " + userPublicId));
        return convertToResponse(teacher);
    }

    @Override
    protected Teacher convertToEntity(TeacherCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected TeacherResponse convertToResponse(Teacher entity) {
        TeacherResponse response = new TeacherResponse();
        response.setPublicId(entity.getPublicId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getUser() != null) {
            response.setUserPublicId(entity.getUser().getPublicId());
            response.setEmail(entity.getUser().getEmail());
            response.setFullName(entity.getUser().getFullName());
            response.setActive(entity.getUser().isActive());
        }

        response.setPhoneNumber(entity.getPhoneNumber());
        response.setBio(entity.getBio());

        // Map danh sách môn học sang SubjectResponse DTOs
        if (entity.getSubjects() != null) {
            Set<SubjectResponse> subjectResponses = entity.getSubjects().stream()
                    .map(sub -> {
                        SubjectResponse subRes = new SubjectResponse();
                        subRes.setPublicId(sub.getPublicId());
                        subRes.setCreatedAt(sub.getCreatedAt());
                        subRes.setUpdatedAt(sub.getUpdatedAt());
                        subRes.setCode(sub.getCode());
                        subRes.setName(sub.getName());
                        subRes.setDescription(sub.getDescription());
                        return subRes;
                    })
                    .collect(Collectors.toSet());
            response.setSubjects(subjectResponses);
        }

        return response;
    }

    @Override
    protected void updateEntityFromRequest(Teacher entity, TeacherUpdateRequest request) {
        // Tương tự, đã xử lý trong hàm update custom
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm update()");
    }
}
