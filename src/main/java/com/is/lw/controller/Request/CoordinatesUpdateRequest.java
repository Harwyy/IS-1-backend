package com.is.lw.controller.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesUpdateRequest {
    Long id;
    float x;
    Long y;
    int z;
}
