package com.is.lw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.is.lw.auth.model.User;
import com.is.lw.model.enums.Difficulty;
import com.is.lw.validator.annotation.ValidEnum;
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
@Table(name = "lab_work")
@Cacheable(false)
public class LabWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_work_id", unique = true, nullable = false)
    private Long id;

    @NotNull()
    @NotBlank()
    @Column(nullable = false, columnDefinition = "VARCHAR(256) check (TRIM(name) <> '')")
    private String name;

    @OneToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @NotNull
    @Column(length = 1840, nullable = false)
    private String description;

    @ValidEnum(enumClass = Difficulty.class, message = "Invalid difficulty value.")
    private String difficulty;

    @ManyToOne()
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @NotNull
    @Min(value = 1)
    @Column(name = "minimal_point", columnDefinition = "double precision NOT NULL CHECK (minimal_point > 0)")
    private Float minimalPoint;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

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
