package ru.greenpix.monitoring.dto

import ru.greenpix.monitoring.model.VersionType

data class VersionDto(
    val id: Int,
    val name: String,
    val type: VersionType
)