package ru.greenpix.monitoring.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
class GatewayTimeoutException : Exception()