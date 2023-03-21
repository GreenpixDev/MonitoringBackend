package ru.greenpix.monitoring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "monitoring")
data class MonirotingProperties @ConstructorBinding constructor(
    val serverSecondsLimit: Int
)