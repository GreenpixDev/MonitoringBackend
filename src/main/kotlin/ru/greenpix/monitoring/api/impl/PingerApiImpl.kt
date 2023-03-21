package ru.greenpix.monitoring.api.impl

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.util.UriComponentsBuilder
import retrofit2.Response
import ru.greenpix.monitoring.api.PingerApi
import ru.greenpix.monitoring.api.dto.PingStatusDto
import ru.greenpix.monitoring.properties.BackendsProperties

@Component
class PingerApiImpl(
    private val webClient: WebClient,
    private val backendsProperties: BackendsProperties
) : PingerApi {

    override suspend fun ping(address: String): PingStatusDto = webClient
        .get()
        .uri {
            UriComponentsBuilder.fromUriString("${backendsProperties.pingerUrl}/ping")
                .queryParam("address", address)
                .build()
                .toUri()
        }
        .retrieve()
        .awaitBody()
}