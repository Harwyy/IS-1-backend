package com.is.lw.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.auth.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "discipline")
@Cacheable(false)
@Schema(description = "Entity representing an academic discipline.")
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discipline_id", unique = true, nullable = false)
    @JsonProperty("id")
    @Schema(description = "Unique identifier of the discipline.", example = "1", required = true)
    private Long id;

    @NotNull()
    @NotBlank()
    @Column(nullable = false, columnDefinition = "VARCHAR(256) check (TRIM(name) <> '')")
    @JsonProperty("name")
    @Schema(description = "Name of the discipline.", example = "Mathematics", required = true)
    private String name;

    @NotNull
    @Min(value = 1)
    @Column(name = "lecture_hours", columnDefinition = "BIGINT NOT NULL CHECK (lecture_hours >= 0)")
    @JsonProperty("lectureHours")
    @Schema(description = "Number of lecture hours for the discipline.", example = "30", required = true)
    private Long lectureHours;

    @Column(name = "practice_hours", columnDefinition = "INT CHECK (practice_hours >= 0)")
    @JsonProperty("practiceHours")
    @Schema(description = "Number of practice hours for the discipline.", example = "15")
    private int practiceHours;

    @NotNull
    @Min(value = 1)
    @Column(name = "self_study_hours", columnDefinition = "INT NOT NULL CHECK (self_study_hours >= 0)")
    @JsonProperty("selfStudyHours")
    @Schema(description = "Number of self-study hours for the discipline.", example = "20", required = true)
    private Integer selfStudyHours;


    @Column(name = "labs_count")
    @JsonProperty("labsCount")
    @Schema(description = "Number of laboratory sessions for the discipline.", example = "5")
    private long labsCount;

    @NotNull
    @Column(nullable = false)
    @JsonProperty("updateable")
    @Schema(description = "Indicates if the location can be updated by an admin.", example = "true", required = true)
    private Boolean updateable;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    @Schema(description = "The user who created this location.", hidden = true)
    private User createdBy;
}
