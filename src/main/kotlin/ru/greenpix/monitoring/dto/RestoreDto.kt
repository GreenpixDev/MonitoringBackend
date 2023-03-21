package ru.greenpix.monitoring.dto

import jakarta.validation.constraints.NotBlank

data class RestoreDto(
    @field:NotBlank
    val email: String
)