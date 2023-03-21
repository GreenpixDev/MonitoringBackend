package ru.greenpix.monitoring.dto

import ru.greenpix.monitoring.model.ServerStatusType

data class ServerStatusDto(
    val type: ServerStatusType,
    val onlinePlayers: Int,
    val maxPlayers: Int
)