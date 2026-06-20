package com.example.user_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload for creating or updating a user")
public class UserRequest {

    @NotBlank(message = "name is required")
    @Schema(description = "Full name of the user", example = "Jane Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid address")
    @Schema(description = "Unique email address of the user", example = "jane.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
