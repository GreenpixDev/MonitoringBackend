package ru.greenpix.monitoring.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.greenpix.monitoring.api.dto.BridgeServerDto

interface BridgeApi {

    @GET("/servers")
    suspend fun getServers(): List<BridgeServerDto>

    @POST("/servers/add")
    suspend fun addServer(@Query("address") address: String)

}