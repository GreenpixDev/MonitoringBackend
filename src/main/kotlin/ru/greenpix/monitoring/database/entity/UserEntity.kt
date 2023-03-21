package ru.greenpix.monitoring.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("public.user")
data class UserEntity(
    @Id
    private var id: UUID? = null,
    val email: String,
    val password: String,
    val active: Boolean,
    val banned: Boolean,
    val verifyToken: String?,
    val registrationTimestamp: LocalDateTime,
    val restoreToken: String?,
    val restoreTimestamp: LocalDateTime?,
) : Persistable<UUID> {

    @Transient
    private var isNew: Boolean = id == null

    init {
        id = id ?: UUID.randomUUID()
    }

    override fun getId(): UUID = id!!

    override fun isNew(): Boolean = isNew

    fun new(): UserEntity {
        this.isNew = true
        return this
    }
}