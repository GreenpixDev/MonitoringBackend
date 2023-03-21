package ru.greenpix.monitoring.api.impl

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.util.UriComponentsBuilder
import retrofit2.Response
import ru.greenpix.monitoring.api.BridgeApi
import ru.greenpix.monitoring.api.PingerApi
import ru.greenpix.monitoring.api.dto.BridgeServerDto
import ru.greenpix.monitoring.api.dto.PingStatusDto
import ru.greenpix.monitoring.properties.BackendsProperties

@Component
class BridgeApiImpl(
    private val webClient: WebClient,
    private val backendsProperties: BackendsProperties
) : BridgeApi {

    override suspend fun getServers(): List<BridgeServerDto> = webClient
        .get()
        .uri {
            UriComponentsBuilder.fromUriString("${backendsProperties.bridgeUrl}/servers")
                .build()
                .toUri()
        }
        .retrieve()
        .awaitBody()

    override suspend fun addServer(address: String): Unit = webClient
        .post()
        .uri {
            UriComponentsBuilder.fromUriString("${backendsProperties.bridgeUrl}/servers/add")
                .queryParam("address", address)
                .build()
                .toUri()
        }
        .retrieve()
        .awaitBody()
}