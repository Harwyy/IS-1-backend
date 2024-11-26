package com.is.lw.core.controller.Response;

import com.is.lw.core.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyResponse {
    Status status;
    String message;
}
