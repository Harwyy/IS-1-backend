package com.is.lw.controller.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonAddRequest {

    @JsonProperty("name")
    String name;
    @JsonProperty("color")
    String color;
    @JsonProperty("haircolor")
    String hairColor;
    @JsonProperty("location")
    Location location;
    @JsonProperty("weight")
    Long weight;
    @JsonProperty("nationality")
    String nationality;


}
