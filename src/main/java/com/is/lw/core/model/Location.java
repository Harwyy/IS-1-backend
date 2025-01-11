package com.is.lw.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.auth.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "location")
@Cacheable(false)
@Schema(description = "Entity representing a geographical location.")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", unique = true, nullable = false)
    @JsonProperty("id")
    @Schema(description = "Unique identifier of the location.", example = "1", required = true)
    private Long id;

    @NotBlank()
    @Column(nullable = false, length = 256)
    @JsonProperty("name")
    @Schema(description = "Name of the location.", example = "Central Park", required = true)
    private String name;

    @NotNull
    @Column(nullable = false)
    @JsonProperty("coordinateX")
    @Schema(description = "X-coordinate of the location.", example = "12345", required = true)
    private Long coordinateX;

    @NotNull
    @Column(nullable = false)
    @JsonProperty("coordinateY")
    @Schema(description = "Y-coordinate of the location.", example = "67.89", required = true)
    private Double coordinateY;

    @JsonProperty("coordinateZ")
    @Schema(description = "Optional Z-coordinate of the location.", example = "45", required = false)
    private Integer coordinateZ;

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
