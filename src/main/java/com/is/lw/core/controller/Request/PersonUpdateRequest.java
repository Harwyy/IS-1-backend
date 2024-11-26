package com.is.lw.core.controller.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.core.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonUpdateRequest {

    @JsonProperty("id")
    Long id;
    @JsonProperty("name")
    String name;
    @JsonProperty("color")
    String color;
    @JsonProperty("hairColor")
    String hairColor;
    @JsonProperty("location")
    Location location;
    @JsonProperty("weight")
    Long weight;
    @JsonProperty("nationality")
    String nationality;

}
