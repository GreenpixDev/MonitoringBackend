package ru.greenpix.monitoring.model

data class ServerStatus(
    val type: ServerStatusType,
    val onlinePlayers: Int,
    val maxPlayers: Int
)