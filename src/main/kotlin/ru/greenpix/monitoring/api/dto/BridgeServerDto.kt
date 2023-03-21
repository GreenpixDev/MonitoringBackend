package ru.greenpix.monitoring.api.dto

data class BridgeServerDto(
    val name: String,
    val address: String,
    val version: String,
    val timestamp: String,
    val online: Int,
    val max: Int
)