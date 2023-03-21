package ru.greenpix.monitoring.service

import ru.greenpix.monitoring.model.Version

interface VersionService {

    fun getVersion(id: Int): Version?

    fun getVersionByProtocol(protocol: Int): Version?

    fun getAllVersions(): List<Version>

}