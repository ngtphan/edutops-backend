package com.example.edutops.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Kích hoạt JPA Auditing để tự động điền createdAt, updatedAt.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
