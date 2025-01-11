package com.is.lw.core.repository;

import com.is.lw.core.model.FileLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileLogRepository extends JpaRepository<FileLog, Long> {

    List<FileLog> findAllByCreatedBy(Long userId);
}
