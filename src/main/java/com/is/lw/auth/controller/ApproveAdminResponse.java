package com.is.lw.auth.controller;

import com.is.lw.auth.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApproveAdminResponse {

    private Status status;
    private String message;

}