package com.is.lw.auth.model;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Builder
@Schema(description = "Summary of a user containing limited information.")
public class UserSummary {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
}
