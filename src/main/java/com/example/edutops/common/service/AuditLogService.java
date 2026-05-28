package com.example.edutops.common.service;

import com.example.edutops.common.entity.AuditLog;

/**
 * Interface dịch vụ quản lý ghi vết hoạt động.
 */
public interface AuditLogService {

    /**
     * Lưu trữ nhật ký audit log một cách bất đồng bộ dưới nền.
     *
     * @param auditLog Đối tượng log cần lưu
     */
    void saveLog(AuditLog auditLog);
}
