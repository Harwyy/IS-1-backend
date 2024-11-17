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
public class CoordinatesAddRequest {

    @JsonProperty("id")
    float x;
    @JsonProperty("y")
    Long y;
    @JsonProperty("z")
    int z;

}
