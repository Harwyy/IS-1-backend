package com.is.lw.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.is.lw.auth.model.User;
import com.is.lw.core.model.enums.Color;
import com.is.lw.core.model.enums.Country;
import com.is.lw.core.validator.annotation.ValidEnum;
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
@Table(name = "person")
@Cacheable(false)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", unique = true, nullable = false)
    private Long id;

    @NotNull()
    @NotBlank()
    @Column(nullable = false, columnDefinition = "VARCHAR(256) check (TRIM(name) <> '')")
    private String name;

    @ValidEnum(enumClass = Color.class, message = "Invalid color value.")
    private String color;

    @NotNull
    @ValidEnum(enumClass = Color.class, message = "Invalid color value.")
    @Column(name = "hair_color", nullable = false)
    private String hairColor;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    private Location location;

    @Min(value = 1)
    @Column(columnDefinition = "BIGINT CHECK (weight >= 1)")
    private Long weight;

    @ValidEnum(enumClass = Country.class, message = "Invalid country value.")
    private String nationality;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @JsonIgnore
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @JsonIgnore
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.createdBy == null) {
            this.createdBy = getCurrentUser();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = getCurrentUser();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}
