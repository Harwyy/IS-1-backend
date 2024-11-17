package com.is.lw.controller.Response;

import com.is.lw.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesAddOrUpdateResponse {
    Status status;
    String message;
}
