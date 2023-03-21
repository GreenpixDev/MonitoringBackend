package ru.greenpix.monitoring.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("public.server")
data class ServerEntity(
    @Id
    private var id: UUID? = null,
    val userId: UUID,
    val name: String,
    val host: String,
    val ip: String,
    val port: Int,
    val online: Boolean,
    val onlinePlayers: Int,
    val maxPlayers: Int,
    val favicon: String?,
    val versionId: Int?,
    val creationTimestamp: LocalDateTime
) : Persistable<UUID> {

    @Transient
    private val isNew: Boolean = id == null

    init {
        id = id ?: UUID.randomUUID()
    }

    override fun getId(): UUID = id!!

    override fun isNew(): Boolean = isNew

}