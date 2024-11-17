package com.is.lw.model;

import com.is.lw.model.enums.Difficulty;
import com.is.lw.validator.annotation.ValidEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @NotNull
    @Column(length = 1840, nullable = false)
    private String description;

    @ValidEnum(enumClass = Difficulty.class, message = "Invalid difficulty value.")
    private String difficulty;

    @ManyToOne()
    private Discipline discipline;

    @NotNull
    @Min(value = 1)
    @Column(name = "minimal_point", columnDefinition = "double precision NOT NULL CHECK (minimal_point > 0)")
    private Float minimalPoint;

    @ManyToOne
    private Person author;

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }

}
