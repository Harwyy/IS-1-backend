package com.is.lw.core.controller.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.core.model.Coordinates;
import com.is.lw.core.model.Discipline;
import com.is.lw.core.model.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabWorkAddRequest{

    @JsonProperty("name")
    String name;
    @JsonProperty("coordinates")
    Coordinates coordinates;
    @JsonProperty("description")
    String description;
    @JsonProperty("difficulty")
    String difficulty;
    @JsonProperty("discipline")
    Discipline discipline;
    @JsonProperty("minimalPoint")
    Float minimalPoint;
    @JsonProperty("author")
    Person author;

}
