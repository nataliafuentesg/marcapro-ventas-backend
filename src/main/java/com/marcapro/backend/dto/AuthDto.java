package com.marcapro.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
    ) {}

    public record LoginResponse(
        String token,
        String username,
        String fullName
    ) {}
}
