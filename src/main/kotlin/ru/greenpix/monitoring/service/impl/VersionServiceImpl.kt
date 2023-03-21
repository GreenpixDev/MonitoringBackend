package ru.greenpix.monitoring.service.impl

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.greenpix.monitoring.database.repository.VersionRepository
import ru.greenpix.monitoring.model.Version
import ru.greenpix.monitoring.model.VersionType
import ru.greenpix.monitoring.service.VersionService

@Service
class VersionServiceImpl(
    private val versionRepository: VersionRepository
) : VersionService {

    private var cachedVersions: Map<Int, Version> = emptyMap()
    private var cachedVersionsByProtocol: Map<Int, Version> = emptyMap()

    override fun getVersion(id: Int): Version?
    = cachedVersions[id]

    override fun getVersionByProtocol(protocol: Int): Version?
    = cachedVersionsByProtocol[protocol]

    override fun getAllVersions(): List<Version>
    = cachedVersions.values.toList()

    //@PostConstruct
    @Scheduled(fixedDelay = 60000)
    private fun invalidateCache() = runBlocking {
        val versions = versionRepository.findAll()
            .map {
                Version(
                    id = it.id!!,
                    type = VersionType.valueOf("${it.type}_edition".uppercase()),
                    name = it.name,
                    protocol = it.protocol
                )
            }
            .toList()
        cachedVersions = versions.associateBy { it.id }
        cachedVersionsByProtocol = versions.associateBy { it.protocol }
    }
}