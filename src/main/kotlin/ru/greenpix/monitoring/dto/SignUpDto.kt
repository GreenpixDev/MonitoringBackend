package ru.greenpix.monitoring.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignUpDto(
    @field:Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    @field:Email
    @field:Size(max = 255)
    val email: String,
    @field:Size(min = 6, max = 50)
    val password: String
)