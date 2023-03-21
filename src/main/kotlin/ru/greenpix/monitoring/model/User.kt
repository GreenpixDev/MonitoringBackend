package ru.greenpix.monitoring.model

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val roles: List<Role>
)