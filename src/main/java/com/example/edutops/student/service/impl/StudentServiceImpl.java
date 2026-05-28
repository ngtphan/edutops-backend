package com.example.edutops.student.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.student.dto.StudentCreateRequest;
import com.example.edutops.student.dto.StudentResponse;
import com.example.edutops.student.dto.StudentUpdateRequest;
import com.example.edutops.student.entity.Student;
import com.example.edutops.student.repository.StudentRepository;
import com.example.edutops.student.service.StudentService;
import com.example.edutops.user.entity.User;
import com.example.edutops.user.entity.UserRole;
import com.example.edutops.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.edutops.common.annotation.Audit;

import java.util.UUID;

@Service
public class StudentServiceImpl 
        extends BaseServiceImpl<StudentCreateRequest, StudentUpdateRequest, StudentResponse, Student> 
        implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, 
                              UserRepository userRepository,
                              EntityMapper entityMapper,
                              PasswordEncoder passwordEncoder) {
        super(studentRepository);
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    @Audit(action = "CREATE_STUDENT", entity = "Student")
    public StudentResponse create(StudentCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw BusinessException.withDetail(ErrorCode.EMAIL_ALREADY_EXISTS, request.getEmail());
        }

        // 1. Tạo tài khoản User liên kết cho học viên
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.STUDENT);
        user.setActive(true);
        User savedUser = userRepository.save(user);

        // 2. Tạo profile Student
        Student student = new Student();
        student.setUser(savedUser);
        student.setPhoneNumber(request.getPhoneNumber());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        
        Student savedStudent = studentRepository.save(student);
        return convertToResponse(savedStudent);
    }

    @Override
    @Transactional
    @Audit(action = "UPDATE_STUDENT", entity = "Student")
    public StudentResponse update(UUID publicId, StudentUpdateRequest request) {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        // Cập nhật thông tin User liên kết
        User user = student.getUser();
        user.setFullName(request.getFullName());
        userRepository.save(user);

        // Cập nhật thông tin Student
        updateEntityFromRequest(student, request);
        Student savedStudent = studentRepository.save(student);
        
        return convertToResponse(savedStudent);
    }

    @Override
    @Transactional
    @Audit(action = "DELETE_STUDENT", entity = "Student")
    public void delete(UUID publicId) {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        User user = student.getUser();

        // Xóa profile Student trước
        studentRepository.delete(student);

        // Xóa tài khoản User tương ứng để tránh rác
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getByUserPublicId(UUID userPublicId) {
        Student student = studentRepository.findByUserPublicId(userPublicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, userPublicId));
        return convertToResponse(student);
    }

    @Override
    @Transactional
    @Audit(action = "COMPLETE_STUDENT_PROFILE", entity = "Student")
    public StudentResponse completeProfile(UUID userPublicId, com.example.edutops.student.dto.StudentProfileCompleteRequest request) {
        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, userPublicId));

        if (user.getRole() != UserRole.STUDENT) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, 
                    "Chỉ tài khoản học viên mới có quyền hoàn tất hồ sơ học viên");
        }

        if (studentRepository.findByUserPublicId(userPublicId).isPresent()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, 
                    "Hồ sơ học viên đã được hoàn tất trước đó");
        }

        Student student = new Student();
        student.setUser(user);
        student.setPhoneNumber(request.getPhoneNumber());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());

        Student savedStudent = studentRepository.save(student);
        return convertToResponse(savedStudent);
    }

    @Override
    protected Student convertToEntity(StudentCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected StudentResponse convertToResponse(Student entity) {
        return entityMapper.toStudentResponse(entity);
    }

    @Override
    protected void updateEntityFromRequest(Student entity, StudentUpdateRequest request) {
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setDateOfBirth(request.getDateOfBirth());
        entity.setGender(request.getGender());
    }
}
