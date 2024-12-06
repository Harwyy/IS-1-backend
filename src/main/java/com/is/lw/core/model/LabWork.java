package com.is.lw.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.auth.model.User;
import com.is.lw.core.model.enums.Difficulty;
import com.is.lw.core.validator.annotation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lab_work")
@Cacheable(false)
@Schema(description = "Entity representing a laboratory work.")
public class LabWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_work_id", unique = true, nullable = false)
    @JsonProperty("id")
    @Schema(description = "Unique identifier of the laboratory work.", example = "1", required = true)
    private Long id;

    @NotBlank()
    @Column(nullable = false, columnDefinition = "VARCHAR(256) check (TRIM(name) <> '')")
    @JsonProperty("name")
    @Schema(description = "Name of the laboratory work.", example = "Physics Experiment", required = true)
    private String name;

    @NotNull
    @OneToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    @JsonProperty("coordinates")
    @Schema(description = "Coordinates associated with this lab work.", required = true)
    private Coordinates coordinates;

    @Column(name = "created_at", nullable = false)
    @JsonProperty("createdAt")
    @Schema(description = "The timestamp when the lab work was created.", example = "2023-11-27T12:34:56")
    private LocalDateTime createdAt;

    @NotBlank
    @Column(length = 1840, nullable = false)
    @JsonProperty("description")
    @Schema(description = "Description of the laboratory work.", example = "An experiment to analyze gravitational acceleration.", required = true)
    private String description;

    @ValidEnum(enumClass = Difficulty.class, message = "Invalid difficulty value.")
    @Column(name = "difficulty")
    @JsonProperty("difficulty")
    @Schema(description = "The difficulty level of the lab work.", example = "HARD", required = false)
    private String difficulty;

    @ManyToOne()
    @JoinColumn(name = "discipline_id")
    @JsonProperty("discipline")
    @Schema(description = "Discipline associated with the lab work.", required = false)
    private Discipline discipline;

    @NotNull
    @Column(name = "minimal_point")
    @JsonProperty("minimalPoint")
    @Schema(description = "Minimal point required to pass the lab work.", example = "50.0", required = true)
    private Float minimalPoint;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonProperty("author")
    @Schema(description = "Person who authored the lab work.", required = false)
    private Person author;

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

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
