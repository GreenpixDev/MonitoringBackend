package ru.greenpix.monitoring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "spring.mail.jwt")
data class EmailJwtProperties @ConstructorBinding constructor(
    val secret: String,
    val expiration: Long
)