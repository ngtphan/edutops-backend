package com.example.edutops.student.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class StudentServiceImpl 
        extends BaseServiceImpl<StudentCreateRequest, StudentUpdateRequest, StudentResponse, Student> 
        implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        super(studentRepository);
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public StudentResponse create(StudentCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, 
                    "Email '" + request.getEmail() + "' đã được sử dụng bởi tài khoản khác");
        }

        // 1. Tạo tài khoản User liên kết cho học viên
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash("HASHED_" + request.getPassword()); // Giả lập băm mật khẩu
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
    public StudentResponse update(UUID publicId, StudentUpdateRequest request) {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy học viên với ID: " + publicId));

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
    public void delete(UUID publicId) {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy học viên với ID: " + publicId));

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
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy học viên ứng với tài khoản có ID: " + userPublicId));
        return convertToResponse(student);
    }

    @Override
    @Transactional
    public StudentResponse completeProfile(UUID userPublicId, com.example.edutops.student.dto.StudentProfileCompleteRequest request) {
        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy tài khoản có ID: " + userPublicId));

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
        // Hàm này không được gọi trực tiếp vì ta đã override hàm 'create' 
        // để xử lý nghiệp vụ tạo liên kết User - Student phức tạp hơn.
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected StudentResponse convertToResponse(Student entity) {
        StudentResponse response = new StudentResponse();
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
        response.setDateOfBirth(entity.getDateOfBirth());
        response.setGender(entity.getGender());
        return response;
    }

    @Override
    protected void updateEntityFromRequest(Student entity, StudentUpdateRequest request) {
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setDateOfBirth(request.getDateOfBirth());
        entity.setGender(request.getGender());
    }
}
