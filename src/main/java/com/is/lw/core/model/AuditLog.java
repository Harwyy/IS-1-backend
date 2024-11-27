package com.is.lw.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "audit_log")
@Schema(description = "Entity representing an audit log of operations performed on the system.")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", unique = true, nullable = false)
    @Schema(description = "Unique identifier of the log entry.", example = "1")
    private Long id;

    @Column(name = "operation_type", nullable = false)
    @Schema(description = "Type of the operation performed (e.g., CREATE, UPDATE, DELETE).", example = "CREATE")
    private String operationType;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "Username of the person who performed the operation.", example = "23")
    private Long userId;

    @Column(name = "time", nullable = false)
    @Schema(description = "Timestamp when the operation was performed.", example = "2024-11-27T15:30:00")
    private LocalDateTime time;

    @Column(name = "table_name", nullable = false)
    @Schema(description = "Name of the table where the operation was performed.", example = "location")
    private String tableName;

    @Column(name = "entity_id", nullable = false)
    @Schema(description = "ID of the entity affected by the operation.", example = "42")
    private Long entityId;
}
