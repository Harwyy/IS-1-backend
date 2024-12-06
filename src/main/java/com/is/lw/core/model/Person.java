package com.is.lw.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.auth.model.User;
import com.is.lw.core.model.enums.Color;
import com.is.lw.core.model.enums.Country;
import com.is.lw.core.validator.annotation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "person")
@Cacheable(false)
@Schema(description = "Entity representing a person.")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", unique = true, nullable = false)
    @JsonProperty("id")
    @Schema(description = "Unique identifier of the person.", example = "1", required = true)
    private Long id;

    @NotNull()
    @NotBlank()
    @Column(nullable = false, columnDefinition = "VARCHAR(256) check (TRIM(name) <> '')")
    @JsonProperty("name")
    @Schema(description = "Name of the person.", example = "John Doe", required = true)
    private String name;

    @ValidEnum(enumClass = Color.class, message = "Invalid color value.")
    @JsonProperty("color")
    @Schema(description = "Color associated with the person.", example = "Green", required = false)
    private String color;

    @NotNull
    @ValidEnum(enumClass = Color.class, message = "Invalid color value.")
    @Column(name = "hair_color", nullable = false)
    @JsonProperty("hairColor")
    @Schema(description = "Hair color of the person.", example = "Green", required = true)
    private String hairColor;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    @JsonProperty("location")
    @Schema(description = "Location associated with the person.", required = false)
    private Location location;

    @Column(columnDefinition = "BIGINT CHECK (weight >= 0)")
    @JsonProperty("weight")
    @Schema(description = "Weight of the person in kilograms.", example = "75", required = false)
    private Long weight;

    @ValidEnum(enumClass = Country.class, message = "Invalid country value.")
    @JsonProperty("nationality")
    @Schema(description = "Country of nationality of the person.", example = "USA", required = false)
    private String nationality;

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
