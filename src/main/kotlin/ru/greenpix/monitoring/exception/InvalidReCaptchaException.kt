package ru.greenpix.monitoring.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST, reason = "invalid_captcha")
class InvalidReCaptchaException(message: String) : Exception(message)