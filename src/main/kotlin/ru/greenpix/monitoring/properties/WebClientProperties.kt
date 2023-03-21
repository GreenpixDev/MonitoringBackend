package ru.greenpix.monitoring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding


@ConfigurationProperties(prefix = "webclient")
data class WebClientProperties @ConstructorBinding constructor(
    val connectTimeout: Int,
    val readTimeout: Long,
    val writeTimeout: Long,
    val bufferSize: Int
)
