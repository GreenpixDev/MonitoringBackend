package ru.greenpix.monitoring.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ServerPublishDto(
    @field:Pattern(regexp = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(:\\d{1,5})?)|((\\w+\\.)+\\w{2,}(:\\d{1,5})?)")
    @field:Size(max = 255)
    @field:NotBlank
    val address: String
)