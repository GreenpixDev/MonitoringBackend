package ru.greenpix.monitoring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "backends")
data class BackendsProperties @ConstructorBinding constructor(
    val pingerUrl: String,
    val bridgeUrl: String
)