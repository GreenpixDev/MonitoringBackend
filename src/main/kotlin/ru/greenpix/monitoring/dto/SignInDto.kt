package ru.greenpix.monitoring.dto

import jakarta.validation.constraints.NotBlank

data class SignInDto(
    @field:NotBlank
    val email: String,
    @field:NotBlank
    val password: String
)