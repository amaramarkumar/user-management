package com.example.user_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representation of a user returned by the API")
public class UserResponse {

    @Schema(description = "Generated unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Full name of the user", example = "Jane Doe")
    private String name;

    @Schema(description = "Unique email address of the user", example = "jane.doe@example.com")
    private String email;
}
