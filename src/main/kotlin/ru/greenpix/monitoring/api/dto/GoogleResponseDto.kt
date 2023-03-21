package ru.greenpix.monitoring.api.dto

data class GoogleResponseDto(
    val success: Boolean,
    val challengeTs: String,
    val hostname: String,

)