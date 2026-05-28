package com.example.edutops.common.repository;

import com.example.edutops.common.entity.AuditLog;
import org.springframework.stereotype.Repository;

/**
 * Repository xử lý các giao dịch với Database cho thực thể AuditLog.
 */
@Repository
public interface AuditLogRepository extends BaseRepository<AuditLog> {
}
