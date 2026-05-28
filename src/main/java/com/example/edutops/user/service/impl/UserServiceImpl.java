package com.example.edutops.user.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.user.dto.UserCreateRequest;
import com.example.edutops.user.dto.UserResponse;
import com.example.edutops.user.dto.UserUpdateRequest;
import com.example.edutops.user.entity.User;
import com.example.edutops.user.repository.UserRepository;
import com.example.edutops.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl 
        extends BaseServiceImpl<UserCreateRequest, UserUpdateRequest, UserResponse, User> 
        implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, 
                    "Email '" + request.getEmail() + "' đã tồn tại trong hệ thống");
        }
        return super.create(request);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        User entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy người dùng với email: " + email));
        return convertToResponse(entity);
    }

    @Override
    @Transactional
    public void toggleActive(UUID publicId) {
        User entity = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy người dùng với ID: " + publicId));
        entity.setActive(!entity.isActive());
        userRepository.save(entity);
    }

    @Override
    @Transactional
    public void changePassword(UUID publicId, String newPassword) {
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "Mật khẩu mới phải có ít nhất 6 ký tự");
        }
        User entity = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy người dùng với ID: " + publicId));
        
        // Mô phỏng băm mật khẩu (sau này sẽ dùng BCryptPasswordEncoder)
        entity.setPasswordHash("HASHED_" + newPassword);
        userRepository.save(entity);
    }

    @Override
    protected User convertToEntity(UserCreateRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        // Giả lập băm mật khẩu
        user.setPasswordHash("HASHED_" + request.getPassword());
        user.setRole(request.getRole());
        user.setActive(true);
        return user;
    }

    @Override
    protected UserResponse convertToResponse(User entity) {
        UserResponse response = new UserResponse();
        response.setPublicId(entity.getPublicId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setEmail(entity.getEmail());
        response.setFullName(entity.getFullName());
        response.setRole(entity.getRole());
        response.setActive(entity.isActive());
        return response;
    }

    @Override
    protected void updateEntityFromRequest(User entity, UserUpdateRequest request) {
        entity.setFullName(request.getFullName());
        entity.setActive(request.isActive());
    }
}
