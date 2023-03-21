package ru.greenpix.monitoring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "security")
data class SecurityProperties @ConstructorBinding constructor(
    val restoreSecondsLimit: Long
)