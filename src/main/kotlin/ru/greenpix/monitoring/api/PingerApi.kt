package ru.greenpix.monitoring.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.greenpix.monitoring.api.dto.PingStatusDto

interface PingerApi {

    @GET("/ping")
    suspend fun ping(@Query("address") address: String): PingStatusDto

}