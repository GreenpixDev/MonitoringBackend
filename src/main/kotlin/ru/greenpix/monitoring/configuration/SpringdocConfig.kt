package ru.greenpix.monitoring.configuration

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration
import ru.greenpix.monitoring.const.SchemeConst

@Configuration
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = SchemeConst.SCHEME_NAME,
    bearerFormat = "JWT",
    scheme = "bearer")
class SpringdocConfig