package ru.greenpix.monitoring.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table

@Table("public.version")
data class VersionEntity(
    @Id
    private var id: Int? = null,
    val protocol: Int,
    val type: String,
    val name: String
) : Persistable<Int> {

    override fun getId(): Int? = id

    override fun isNew(): Boolean = id == null

}