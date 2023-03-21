package ru.greenpix.monitoring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "google.recaptcha.key")
data class CaptchaProperties @ConstructorBinding constructor(
    val site: String,
    val secret: String
)