package com.is.lw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}
