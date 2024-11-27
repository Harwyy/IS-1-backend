package com.is.lw.core.service;

import com.is.lw.core.model.AuditLog;
import com.is.lw.core.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logOperation(String operationType, Long userId, String tableName, Long entityId) {
        AuditLog auditLog = AuditLog.builder()
                .operationType(operationType)
                .userId(userId)
                .time(LocalDateTime.now())
                .tableName(tableName)
                .entityId(entityId)
                .build();
        auditLogRepository.save(auditLog);
    }
}
