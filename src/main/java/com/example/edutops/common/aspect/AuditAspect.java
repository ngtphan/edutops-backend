package com.example.edutops.common.aspect;

import com.example.edutops.common.annotation.Audit;
import com.example.edutops.common.entity.AuditLog;
import com.example.edutops.common.service.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Aspect tự động xử lý ghi nhận Audit Log bằng AOP.
 */
@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    public AuditAspect(AuditLogService auditLogService, ObjectMapper objectMapper) {
        this.auditLogService = auditLogService;
        this.objectMapper = objectMapper;
    }

    /**
     * Chặn xung quanh các phương thức có gắn @Audit để đo lường thời gian và kết quả thực thi.
     */
    @Around("@annotation(audit)")
    public Object profile(ProceedingJoinPoint pjp, Audit audit) throws Throwable {
        long start = System.currentTimeMillis();
        String status = "SUCCESS";
        String errorMessage = null;
        Object result = null;

        try {
            result = pjp.proceed(); // Thực thi nghiệp vụ chính
            return result;
        } catch (Throwable t) {
            status = "FAILED";
            errorMessage = t.getMessage();
            throw t; // Ném lại Exception để GlobalExceptionHandler xử lý
        } finally {
            long duration = System.currentTimeMillis() - start;
            createAndSaveLog(pjp, audit, status, errorMessage, duration);
        }
    }

    private void createAndSaveLog(ProceedingJoinPoint pjp, Audit audit, String status, String errorMessage, long duration) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setAction(audit.action());
            auditLog.setEntityName(audit.entity());
            auditLog.setStatus(status);
            auditLog.setErrorMessage(errorMessage);
            auditLog.setDuration(duration);

            // 1. Lấy thông tin tài khoản đăng nhập (Username)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                auditLog.setUsername(auth.getName());
            } else {
                auditLog.setUsername("ANONYMOUS");
            }

            // 2. Lấy Địa chỉ IP của Client
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                auditLog.setIpAddress(ip);
            }

            // 3. Serialize DTO request body làm Payload
            Object[] args = pjp.getArgs();
            if (args != null && args.length > 0) {
                try {
                    // Để bảo mật và hiệu năng, chỉ serialize tham số DTO đầu tiên
                    auditLog.setPayload(objectMapper.writeValueAsString(args[0]));
                } catch (Exception e) {
                    auditLog.setPayload("Error serialization: " + e.getMessage());
                }
            }

            // 4. Lưu bất đồng bộ xuống Database
            auditLogService.saveLog(auditLog);
        } catch (Exception e) {
            // Nuốt lỗi ghi log để không làm sập giao dịch chính của người dùng
            log.error("Lỗi khi ghi nhận nhật ký hoạt động (Audit Log): ", e);
        }
    }
}
