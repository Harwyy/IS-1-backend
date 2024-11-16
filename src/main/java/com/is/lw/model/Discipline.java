package com.is.lw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "discipline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable(false)
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discipline_id")
    private Long id;

    @NotNull()
    @NotBlank()
    private String name;

    @Column(name = "lecture_hours")
    @NotNull
    @Min(value = 1)
    private Long lectureHours;

    @Column(name = "practice_hours")
    @Min(value = 1)
    private int practiceHours;

    @Column(name = "self_study_hours")
    @NotNull
    @Min(value = 1)
    private Integer selfStudyHours;

    @Column(name = "labs_count")
    @Min(value = 1)
    private long labsCount;
}
