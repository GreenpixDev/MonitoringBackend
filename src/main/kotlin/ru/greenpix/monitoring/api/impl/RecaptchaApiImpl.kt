package ru.greenpix.monitoring.api.impl

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.util.UriComponentsBuilder
import retrofit2.Response
import retrofit2.http.Query
import ru.greenpix.monitoring.api.PingerApi
import ru.greenpix.monitoring.api.RecaptchaApi
import ru.greenpix.monitoring.api.dto.GoogleResponseDto
import ru.greenpix.monitoring.api.dto.PingStatusDto
import ru.greenpix.monitoring.properties.BackendsProperties

@Component
class RecaptchaApiImpl(
    private val webClient: WebClient
) : RecaptchaApi {

    override suspend fun verify(
        secret: String,
        response: String,
        remoteIp: String,
    ): GoogleResponseDto = webClient
        .post()
        .uri {
            UriComponentsBuilder.fromUriString("https://www.google.com/recaptcha/api/siteverify")
                .queryParam("secret", secret)
                .queryParam("response", response)
                .queryParam("remoteip", remoteIp)
                .build()
                .toUri()
        }
        .retrieve()
        .awaitBody()
}