package ru.greenpix.monitoring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "security.jwt")
data class JwtProperties @ConstructorBinding constructor(
    val secret: String,
    val expiration: Long
)