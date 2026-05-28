package com.example.edutops.common.security;

import com.example.edutops.student.repository.StudentRepository;
import com.example.edutops.teacher.repository.TeacherRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Lớp tiện ích hỗ trợ phân quyền kiểm tra chính chủ (Domain-specific Authorization).
 * Giúp giảm thiểu lỗi cú pháp SpEL trên Controller và tăng tính bảo trì mở rộng.
 */
@Component("securityUtils")
public class SecurityUtils {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public SecurityUtils(@Lazy TeacherRepository teacherRepository, @Lazy StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Kiểm tra xem người dùng đang đăng nhập có phải là ADMIN hoặc là chính chủ của tài nguyên hay không.
     *
     * @param targetPublicId ID công khai của tài nguyên cần kiểm tra
     * @return true nếu là ADMIN hoặc chính chủ tài nguyên, ngược lại false
     */
    public boolean isSelfOrAdmin(UUID targetPublicId) {
        if (targetPublicId == null) {
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 1. Nếu người dùng là ADMIN -> Luôn cho phép
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }

        // 2. So sánh targetPublicId với publicId trích xuất từ JWT token
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String tokenPublicId = jwt.getClaimAsString("publicId");
            return targetPublicId.toString().equalsIgnoreCase(tokenPublicId);
        }

        return false;
    }

    /**
     * Kiểm tra xem người dùng đang đăng nhập có phải là ADMIN hoặc là chính chủ sở hữu hồ sơ Giáo viên hay không.
     */
    public boolean isSelfOrAdminTeacher(UUID teacherPublicId) {
        if (teacherPublicId == null) {
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String tokenPublicId = jwt.getClaimAsString("publicId");
            if (tokenPublicId == null) return false;

            return teacherRepository.findByPublicId(teacherPublicId)
                    .map(teacher -> teacher.getUser() != null && 
                                    teacher.getUser().getPublicId().toString().equalsIgnoreCase(tokenPublicId))
                    .orElse(false);
        }

        return false;
    }

    /**
     * Kiểm tra xem người dùng đang đăng nhập có phải là ADMIN hoặc là chính chủ sở hữu hồ sơ Học viên hay không.
     */
    public boolean isSelfOrAdminStudent(UUID studentPublicId) {
        if (studentPublicId == null) {
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String tokenPublicId = jwt.getClaimAsString("publicId");
            if (tokenPublicId == null) return false;

            return studentRepository.findByPublicId(studentPublicId)
                    .map(student -> student.getUser() != null && 
                                    student.getUser().getPublicId().toString().equalsIgnoreCase(tokenPublicId))
                    .orElse(false);
        }

        return false;
    }
}
