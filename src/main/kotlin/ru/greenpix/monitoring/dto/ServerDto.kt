package ru.greenpix.monitoring.dto

data class ServerDto(
    val name: String,
    val address: String,
    val lifetime: Long,
    val status: ServerStatusDto,
    val icon: String?,
    val version: VersionDto?,
)