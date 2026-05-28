package com.example.edutops.auth.service.impl;

import com.example.edutops.auth.dto.GoogleLoginRequest;
import com.example.edutops.auth.dto.TokenResponse;
import com.example.edutops.auth.service.AuthService;
import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.security.JwtProvider;
import com.example.edutops.student.repository.StudentRepository;
import com.example.edutops.teacher.repository.TeacherRepository;
import com.example.edutops.user.entity.User;
import com.example.edutops.user.entity.UserRole;
import com.example.edutops.user.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final JwtProvider jwtProvider;

    @Value("${app.google.client-id}")
    private String googleClientId;

    public AuthServiceImpl(UserRepository userRepository, 
                           StudentRepository studentRepository, 
                           TeacherRepository teacherRepository,
                           JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @Transactional
    public TokenResponse loginWithGoogle(GoogleLoginRequest request) {
        try {
            // 1. Xác minh tính hợp lệ của Google ID Token trực tiếp từ Google API
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), 
                    new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken == null) {
                throw new BusinessException(ErrorCode.GOOGLE_AUTH_FAILED, "Mã xác thực Google ID Token không hợp lệ");
            }

            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String fullName = (String) payload.get("name");

            // 2. Tìm kiếm hoặc tự động đăng ký mới tài khoản User tương ứng (Không tạo Student dummy)
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setFullName(fullName != null ? fullName : "Google User");
                newUser.setPasswordHash("OAUTH2_EXTERNAL_ACCOUNT");
                newUser.setRole(UserRole.STUDENT);
                newUser.setActive(true);
                return userRepository.save(newUser);
            });

            // Nếu tài khoản đã bị khóa
            user.validateActiveStatus();

            // 3. Kiểm tra xem profile đã hoàn thành hay chưa
            boolean profileCompleted = true;
            if (user.getRole() == UserRole.STUDENT) {
                profileCompleted = studentRepository.findByUserPublicId(user.getPublicId()).isPresent();
            } else if (user.getRole() == UserRole.TEACHER) {
                profileCompleted = teacherRepository.findByUserPublicId(user.getPublicId()).isPresent();
            }

            // 4. Sinh token JWT của hệ thống EduTopS
            String accessToken = jwtProvider.generateToken(user);

            return new TokenResponse(
                    accessToken, 
                    user.getEmail(), 
                    user.getFullName(), 
                    user.getRole().name(),
                    profileCompleted
            );

        } catch (GeneralSecurityException | IOException e) {
            throw new BusinessException(ErrorCode.GOOGLE_AUTH_FAILED, "Kết nối xác thực Google thất bại: " + e.getMessage());
        }
    }
}
