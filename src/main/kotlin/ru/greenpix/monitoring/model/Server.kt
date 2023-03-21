package ru.greenpix.monitoring.model

import ru.greenpix.monitoring.dto.ServerDto
import ru.greenpix.monitoring.dto.ServerStatusDto
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

data class Server(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val status: ServerStatus,
    val host: String,
    val ip: String,
    val port: Int,
    val version: Version?,
    val iconBase64: String?,
    val creationTimestamp: LocalDateTime
) {

    fun toDto() = ServerDto(
        name = name,
        address = host + if (port != 25565) ":$port" else "",
        status = ServerStatusDto(
            type = status.type,
            onlinePlayers = status.onlinePlayers,
            maxPlayers = status.maxPlayers
        ),
        icon = iconBase64,
        version = version?.toDto(),
        lifetime = Duration.between(creationTimestamp, LocalDateTime.now()).seconds
    )
}
