package com.is.lw.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.is.lw.auth.model.User;
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
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discipline_id", unique = true, nullable = false)
    private Long id;

    @NotNull()
    @NotBlank()
    @Column(nullable = false, columnDefinition = "VARCHAR(256) check (TRIM(name) <> '')")
    private String name;

    @NotNull
    @Min(value = 1)
    @Column(name = "lecture_hours", columnDefinition = "BIGINT NOT NULL CHECK (lecture_hours >= 1)")
    private Long lectureHours;

    @Min(value = 1)
    @Column(name = "practice_hours", columnDefinition = "INT CHECK (practice_hours >= 1)")
    private int practiceHours;

    @NotNull
    @Min(value = 1)
    @Column(name = "self_study_hours", columnDefinition = "INT NOT NULL CHECK (self_study_hours >= 1)")
    private Integer selfStudyHours;

    @Min(value = 1)
    @Column(name = "labs_count", columnDefinition = "BIGINT CHECK (labs_count >= 1)")
    private long labsCount;

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
