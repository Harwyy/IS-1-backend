package com.is.lw.model;

import com.is.lw.model.enums.Color;
import com.is.lw.model.enums.Country;
import com.is.lw.validator.annotation.ValidEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable(false)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    @NotNull()
    @NotBlank()
    private String name;

    @ValidEnum(enumClass = Color.class, message = "Invalid color value.")
    private String color;

    @Column(name = "hair_color")
    @ValidEnum(enumClass = Color.class, message = "Invalid color value.")
    @NotNull
    private String hairColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private Location location;

    @Min(value = 1)
    private Long weight;

    @ValidEnum(enumClass = Country.class, message = "Invalid country value.")
    private String nationality;
}
