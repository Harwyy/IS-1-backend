package com.is.lw.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Cacheable(false)
@Table(name = "file_log")
public class FileLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", unique = true, nullable = false)
    @JsonProperty("id")
    @Schema(description = "Unique identifier of the file log entry.", example = "1")
    private Long id;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    @Schema(description = "The status of adding items.", example = "False")
    private Boolean status;

    @Column(name = "count_of_elements", nullable = false)
    @JsonProperty("count")
    @Schema(description = "Number of objects added.", example = "10")
    private Long count;

    @Column(name = "created_at", nullable = false)
    @JsonProperty("createdAt")
    @Schema(description = "The timestamp when the file was processed.", example = "2023-11-27T12:34:56")
    private LocalDateTime createdAt;

    @Column(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    @Schema(description = "Username of the person who performed the operation.", example = "23")
    private Long createdBy;

}
