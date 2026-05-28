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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.edutops.common.annotation.Audit;

import java.util.UUID;

@Service
public class UserServiceImpl 
        extends BaseServiceImpl<UserCreateRequest, UserUpdateRequest, UserResponse, User> 
        implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    @Audit(action = "CREATE_USER", entity = "User")
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw BusinessException.withDetail(ErrorCode.EMAIL_ALREADY_EXISTS, request.getEmail());
        }
        return super.create(request);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        User entity = userRepository.findByEmail(email)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, email));
        return convertToResponse(entity);
    }

    @Override
    @Transactional
    @Audit(action = "TOGGLE_USER_ACTIVE", entity = "User")
    public void toggleActive(UUID publicId) {
        User entity = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));
        entity.setActive(!entity.isActive());
        userRepository.save(entity);
    }

    @Override
    @Transactional
    @Audit(action = "CHANGE_USER_PASSWORD", entity = "User")
    public void changePassword(UUID publicId, String newPassword) {
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "Mật khẩu mới phải có ít nhất 6 ký tự");
        }
        User entity = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));
        
        entity.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(entity);
    }

    @Override
    protected User convertToEntity(UserCreateRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
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

    @Override
    @Transactional
    @Audit(action = "UPDATE_USER", entity = "User")
    public UserResponse update(UUID publicId, UserUpdateRequest request) {
        return super.update(publicId, request);
    }

    @Override
    @Transactional
    @Audit(action = "DELETE_USER", entity = "User")
    public void delete(UUID publicId) {
        super.delete(publicId);
    }
}
