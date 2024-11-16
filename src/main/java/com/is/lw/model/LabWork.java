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
import java.util.Set;

@Entity
@Table(name = "lab_work")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable(false)
public class LabWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_work_id")
    private Long id;

    @NotNull()
    @NotBlank()
    private String name;

    @OneToOne
    @JoinColumn(name = "coordinates_id", referencedColumnName = "coordinates_id")
    @NotNull
    private Coordinates coordinates;

    @Column(name = "creation_date")
    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    @Column(length = 1840)
    private String description;

    @ValidEnum(enumClass = Difficulty.class, message = "Invalid difficulty value.")
    private String difficulty;

    @ManyToOne()
    @JoinColumn(name = "discipline_id", referencedColumnName = "discipline_id")
    private Discipline discipline;

    @Column(name = "minimal_point")
    @Min(value = 1)
    @NotNull
    private Float minimalPoint;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person author;

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }
}
