package com.is.lw.controller.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationAddRequest {

    @JsonProperty("name")
    String name;
    @JsonProperty("x")
    Long x;
    @JsonProperty("y")
    Double y;
    @JsonProperty("z")
    int z;

}
