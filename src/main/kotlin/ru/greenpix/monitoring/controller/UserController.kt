package ru.greenpix.monitoring.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.greenpix.monitoring.dto.RestoreDto
import ru.greenpix.monitoring.dto.SignInDto
import ru.greenpix.monitoring.dto.SignUpDto
import ru.greenpix.monitoring.dto.TokenDto
import ru.greenpix.monitoring.service.CaptchaService
import ru.greenpix.monitoring.service.JwtService
import ru.greenpix.monitoring.service.UserService

@Tag(name = "Пользователи")
@RestController
@RequestMapping("api/v1/users")
class UserController(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val captchaService: CaptchaService
) {

    @Operation(summary = "Зарегистрировать нового пользователя")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "409", description = "Пользователей с данной почтой уже существует", content = [Content()])
    @PostMapping("signup")
    suspend fun signUp(
        @RequestBody @Validated signUpDto: SignUpDto,
        @RequestHeader("g-recaptcha-response") recaptchaResponse: String,
        request: ServerHttpRequest
    ) {
        captchaService.validate(recaptchaResponse, request.remoteAddress!!.address.hostAddress)
        userService.signUp(signUpDto.email, signUpDto.password)
    }

    @Operation(summary = "Аутентифицировать нового пользователя")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", description = "Неверный логин или пароль", content = [Content()])
    @PostMapping("signin")
    suspend fun signIn(
        @RequestBody @Validated signInDto: SignInDto,
        @RequestHeader("g-recaptcha-response") recaptchaResponse: String,
        request: ServerHttpRequest
    ): TokenDto  {
        captchaService.validate(recaptchaResponse, request.remoteAddress!!.address.hostAddress)
        return TokenDto(jwtService.generateToken(
            userService.signIn(signInDto.email, signInDto.password)
        ))
    }

    @Operation(summary = "Восстановить пароль")
    @ApiResponse(responseCode = "200")
    @PostMapping("restore")
    suspend fun restore(
        @RequestBody @Validated restoreDto: RestoreDto,
        @RequestHeader("g-recaptcha-response") recaptchaResponse: String,
        request: ServerHttpRequest
    ) {
        captchaService.validate(recaptchaResponse, request.remoteAddress!!.address.hostAddress)
        userService.restore(restoreDto.email)
    }
}