package com.is.lw.core.controller.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesAddRequest {

    @JsonProperty("x")
    float x;
    @JsonProperty("y")
    Long y;
    @JsonProperty("z")
    int z;

}
