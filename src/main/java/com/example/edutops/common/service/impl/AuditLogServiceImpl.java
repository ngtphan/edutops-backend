package com.example.edutops.common.service.impl;

import com.example.edutops.common.entity.AuditLog;
import com.example.edutops.common.repository.AuditLogRepository;
import com.example.edutops.common.service.AuditLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lớp triển khai AuditLogService hỗ trợ lưu bất đồng bộ.
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Async // Chạy bất đồng bộ
    @Transactional
    public void saveLog(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }
}
