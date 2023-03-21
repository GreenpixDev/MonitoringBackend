package ru.greenpix.monitoring.service

import ru.greenpix.monitoring.model.Server
import java.util.UUID

interface ServerService {

    suspend fun getServers(): List<Server>

    suspend fun createServer(
        userId: UUID,
        serverAddress: String
    ): Server

}