package ru.greenpix.monitoring.database.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.greenpix.monitoring.database.entity.VersionEntity

interface VersionRepository : CoroutineCrudRepository<VersionEntity, Int> {
}