package ru.greenpix.monitoring.service.impl

import io.github.reactivecircus.cache4k.Cache
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import ru.greenpix.monitoring.api.RecaptchaApi
import ru.greenpix.monitoring.exception.InvalidReCaptchaException
import ru.greenpix.monitoring.properties.CaptchaProperties
import ru.greenpix.monitoring.service.CaptchaService
import kotlin.time.Duration.Companion.hours

@Service
class CaptchaServiceImpl(
    private val captchaProperties: CaptchaProperties,
    private val recaptchaApi: RecaptchaApi
) : CaptchaService {

    private companion object {
        const val MAX_ATTEMPT = 3
        val RESPONSE_PATTERN = Regex("[A-Za-z0-9_-]+")
    }

    private val cache = Cache.Builder()
        .expireAfterWrite(4.hours)
        .build<String, Int>()

    override suspend fun validate(response: String, clientIp: String) {
        if (isBlocked(clientIp)) {
            throw InvalidReCaptchaException("Client exceeded maximum number of failed attempts");
        }

        if (!responseSanityCheck(response)) {
            throw InvalidReCaptchaException("Response contains invalid characters");
        }

        val googleResponse = recaptchaApi.verify(captchaProperties.secret, response, clientIp)

        if (!googleResponse.success) {
            reCaptchaFailed(clientIp)
            throw InvalidReCaptchaException("reCaptcha was not successfully validated");
        }
        reCaptchaSucceeded(clientIp)
    }

    private fun responseSanityCheck(response: String): Boolean {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matches(response)
    }

    private fun reCaptchaSucceeded(clientIp: String) {
        cache.invalidate(clientIp)
    }

    private fun reCaptchaFailed(clientIp: String) {
        var attempts: Int = cache.get(clientIp) ?: 0
        attempts++
        cache.put(clientIp, attempts)
    }

    private fun isBlocked(clientIp: String): Boolean {
        return (cache.get(clientIp) ?: 0) >= MAX_ATTEMPT
    }
}