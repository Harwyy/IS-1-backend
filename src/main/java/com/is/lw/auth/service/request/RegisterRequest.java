package com.is.lw.auth.service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.is.lw.auth.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for register")
public class RegisterRequest {

    @Schema(description = "Username of the user (min length = 3)", example = "user1234", required = true)
    @JsonProperty("username")
    private String username;

    @Schema(description = "Password of the user (min length = 8)", example = "P@ssw0rd", required = true)
    @JsonProperty("password")
    private String password;

    @Schema(description = "Role of the user", example = "ADMIN", required = true)
    @JsonProperty("role")
    private Role role;

}
