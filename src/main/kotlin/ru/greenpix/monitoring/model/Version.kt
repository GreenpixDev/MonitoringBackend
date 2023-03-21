package ru.greenpix.monitoring.model

import ru.greenpix.monitoring.dto.VersionDto

data class Version(
    val id: Int,
    val name: String,
    val type: VersionType,
    val protocol: Int
) {
    fun toDto() = VersionDto(
        id = id,
        name = name,
        type = type
    )
}