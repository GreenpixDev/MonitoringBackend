package ru.greenpix.monitoring.database.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.greenpix.monitoring.database.entity.UserEntity
import java.util.UUID

interface UserRepository : CoroutineCrudRepository<UserEntity, UUID> {

    suspend fun existsByIdAndBanned(id: UUID, banned: Boolean): Boolean

    suspend fun findByEmail(email: String): UserEntity?

    suspend fun existsByEmail(email: String): Boolean

}