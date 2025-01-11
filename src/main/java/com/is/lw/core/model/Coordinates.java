package com.is.lw.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.auth.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coordinates")
@Cacheable(false)
@Schema(description = "Entity representing geographical coordinates.")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordinates_id", unique = true, nullable = false)
    @JsonProperty("id")
    @Schema(description = "Unique identifier of the coordinates.", example = "1", required = true)
    private Long id;

    @JsonProperty("x")
    @Schema(description = "X-coordinate of the location.", example = "12.34")
    private float x;

    @NotNull
    @Max(value = 540, message = "Y-coordinate must be less than or equal to 540.")
    @Column(columnDefinition = "BIGINT NOT NULL CHECK (y <= 540)")
    @JsonProperty("y")
    @Schema(description = "Y-coordinate of the location. Must be less than or equal to 540.", example = "123", required = true)
    private Long y;


    @NotNull
    @Column(nullable = false)
    @JsonProperty("updateable")
    @Schema(description = "Indicates if the coordinates  can be updated by an admin.", example = "true", required = true)
    private Boolean updateable;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    @Schema(description = "The user who created this coordinates.", hidden = true)
    private User createdBy;
}
