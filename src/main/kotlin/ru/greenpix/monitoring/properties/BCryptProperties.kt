package ru.greenpix.monitoring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "security.bcrypt")
data class BCryptProperties @ConstructorBinding constructor(
    val version: String,
    val strength: Int
)