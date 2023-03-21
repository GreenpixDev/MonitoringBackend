package ru.greenpix.monitoring.dto

import jakarta.validation.constraints.Size

data class VerifyRestoreDto(
    val token: String,
    @field:Size(min = 6, max = 50)
    val password: String
)