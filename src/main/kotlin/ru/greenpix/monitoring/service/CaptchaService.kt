package ru.greenpix.monitoring.service

interface CaptchaService {

    suspend fun validate(response: String, clientIp: String)

}