package ru.greenpix.monitoring.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.greenpix.monitoring.dto.*
import ru.greenpix.monitoring.service.CaptchaService
import ru.greenpix.monitoring.service.JwtService
import ru.greenpix.monitoring.service.UserService

@Tag(name = "Подтверждение по токену из почты")
@RestController
@RequestMapping("api/v1/verify")
class VerifyController(
    private val jwtService: JwtService,
    private val userService: UserService
) {

    @Operation(summary = "Подтвердить электронную почту")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", description = "Неверный токен", content = [Content()])
    @PostMapping("email")
    suspend fun verifyEmail(
        @RequestBody verifyEmailDto: VerifyEmailDto
    ): TokenDto {
        return TokenDto(jwtService.generateToken(
            userService.verifyEmail(verifyEmailDto.token)
        ))
    }

    @Operation(summary = "Подтвердить восстановление пароля")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", description = "Неверный токен", content = [Content()])
    @PostMapping("restore")
    suspend fun verifyRestore(
        @RequestBody verifyRestoreDto: VerifyRestoreDto
    ): TokenDto {
        return TokenDto(jwtService.generateToken(
            userService.verifyRestore(verifyRestoreDto.token, verifyRestoreDto.password)
        ))
    }
}