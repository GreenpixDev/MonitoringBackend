package ru.greenpix.monitoring.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import ru.greenpix.monitoring.properties.WebClientProperties
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig(
    private val properties: WebClientProperties,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun httpClient(): HttpClient
    = HttpClient.create()
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.connectTimeout)
        .doOnConnected { connection ->
            connection
                .addHandlerLast(ReadTimeoutHandler(properties.readTimeout, TimeUnit.MILLISECONDS))
                .addHandlerLast(WriteTimeoutHandler(properties.writeTimeout, TimeUnit.MILLISECONDS))
        }

    @Bean
    fun webClient(): WebClient
    = WebClient.builder()
        .exchangeStrategies(
            ExchangeStrategies.builder().codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(properties.bufferSize)
                configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
            }.build()
        )
        .clientConnector(ReactorClientHttpConnector(httpClient()))
        .build()
}