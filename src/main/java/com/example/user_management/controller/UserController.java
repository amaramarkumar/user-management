package com.example.user_management.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.user_management.dto.UserRequest;
import com.example.user_management.dto.UserResponse;
import com.example.user_management.service.UserService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(
                title = "User Management API",
                version = "v1",
                description = "REST API for managing users (CRUD operations).",
                contact = @Contact(name = "User Management Team", email = "support@example.com"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")))
@Tag(name = "Users", description = "Create, read, update and delete users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user",
            description = "Registers a new user. The email must be unique.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email already in use", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request,
                                               UriComponentsBuilder uriBuilder) {
        UserResponse created = userService.create(request);
        URI location = uriBuilder.path("/api/users/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "List all users", description = "Returns all registered users.")
    @ApiResponse(responseCode = "200", description = "List of users returned")
    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @Operation(summary = "Get a user by id", description = "Returns a single user by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public UserResponse findById(
            @Parameter(description = "Identifier of the user", example = "1") @PathVariable Long id) {
        return userService.findById(id);
    }

    @Operation(summary = "Update an existing user",
            description = "Updates the name and email of an existing user. The email must remain unique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email already in use", content = @Content)
    })
    @PutMapping("/{id}")
    public UserResponse update(
            @Parameter(description = "Identifier of the user", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifier of the user", example = "1") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
