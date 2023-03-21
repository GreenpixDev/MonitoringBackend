package ru.greenpix.monitoring.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.greenpix.monitoring.const.SchemeConst
import ru.greenpix.monitoring.dto.*
import ru.greenpix.monitoring.exception.PublishLimitException
import ru.greenpix.monitoring.exception.UserPublishLimitException
import ru.greenpix.monitoring.service.CaptchaService
import ru.greenpix.monitoring.service.ServerService
import java.util.UUID

@Tag(name = "Minecraft сервера")
@RestController
@RequestMapping("api/v1/servers")
class ServerController(
    private val serverService: ServerService,
    private val captchaService: CaptchaService
) {

    @SecurityRequirement(name = SchemeConst.SCHEME_NAME)
    @Operation(summary = "Получить весь список серверов")
    @GetMapping
    suspend fun getServers(): ResponseEntity<List<ServerDto>> {
        return ResponseEntity.ok(serverService.getServers().map { it.toDto() })
    }

    @SecurityRequirement(name = SchemeConst.SCHEME_NAME)
    @Operation(summary = "Выставить сервер в список серверов")
    @PostMapping("publish")
    suspend fun publishServer(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody @Validated dto: ServerPublishDto,
        @RequestHeader("g-recaptcha-response") recaptchaResponse: String,
        request: ServerHttpRequest
    ): ResponseEntity<*> {
        captchaService.validate(recaptchaResponse, request.remoteAddress!!.address.hostAddress)
        return try {
            serverService.createServer(userId, dto.address)
            ResponseEntity.ok().build<Unit>()
        } catch (e: PublishLimitException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf(
                    "message" to "publish_limit",
                    "seconds" to e.seconds.toString(),
                    "user" to (e is UserPublishLimitException)
                ))
        }
    }
}