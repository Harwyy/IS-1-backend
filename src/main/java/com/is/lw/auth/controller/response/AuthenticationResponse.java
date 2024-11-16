package com.is.lw.auth.controller.response;

import com.is.lw.model.enums.Status;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private Status status;
    private String token;
    private String message;

}
