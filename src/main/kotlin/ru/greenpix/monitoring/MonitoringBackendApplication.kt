package ru.greenpix.monitoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableWebFlux
@EnableScheduling
@EnableR2dbcRepositories(basePackages = ["ru.greenpix.monitoring.database.repository"])
class MonitoringBackendApplication

fun main(args: Array<String>) {
    runApplication<MonitoringBackendApplication>(*args)
}
