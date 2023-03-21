package ru.greenpix.monitoring.database.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.greenpix.monitoring.database.entity.ServerEntity
import java.util.UUID

interface ServerRepository : CoroutineCrudRepository<ServerEntity, UUID> {

    suspend fun findByIpAndPort(ip: String, port: Int): ServerEntity?

    suspend fun findByHost(host: String): ServerEntity?

    @Query("SELECT * FROM public.server s WHERE s.user_id = :userId ORDER BY s.creation_timestamp DESC LIMIT 1")
    suspend fun findLastByUserId(userId: UUID): ServerEntity?

    fun findAllBy(pageable: Pageable): Flow<ServerEntity>

    fun findAllByOnline(online: Boolean, pageable: Pageable): Flow<ServerEntity>

}