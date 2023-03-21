package ru.greenpix.monitoring.api

import retrofit2.http.POST
import retrofit2.http.Query
import ru.greenpix.monitoring.api.dto.GoogleResponseDto

interface RecaptchaApi {

    @POST("/recaptcha/api/siteverify")
    suspend fun verify(
        @Query("secret") secret: String,
        @Query("response") response: String,
        @Query("remoteip") remoteIp: String,
    ): GoogleResponseDto

}