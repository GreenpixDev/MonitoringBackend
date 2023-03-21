package ru.greenpix.monitoring.service.impl

import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientException
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import retrofit2.HttpException
import ru.greenpix.monitoring.api.PingerApi
import ru.greenpix.monitoring.database.entity.ServerEntity
import ru.greenpix.monitoring.database.repository.ServerRepository
import ru.greenpix.monitoring.database.repository.UserRepository
import ru.greenpix.monitoring.exception.GatewayTimeoutException
import ru.greenpix.monitoring.exception.MinecraftServerUnavailableException
import ru.greenpix.monitoring.exception.ServerPublishLimitException
import ru.greenpix.monitoring.exception.UserPublishLimitException
import ru.greenpix.monitoring.model.Address
import ru.greenpix.monitoring.model.Server
import ru.greenpix.monitoring.model.ServerStatus
import ru.greenpix.monitoring.model.ServerStatusType
import ru.greenpix.monitoring.properties.MonirotingProperties
import ru.greenpix.monitoring.service.ServerService
import ru.greenpix.monitoring.service.VersionService
import java.net.SocketTimeoutException
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.time.Duration.Companion.seconds

@Service
class ServerServiceImpl(
    private val serverRepository: ServerRepository,
    private val userRepository: UserRepository,
    private val versionService: VersionService,
    private val pingerApi: PingerApi,
    private val monirotingProperties: MonirotingProperties
) : ServerService {

    private val cache = Cache.Builder()
        .expireAfterWrite(20.seconds)
        .build<Unit, List<Server>>()

    private companion object {
        val INVALID_SYMBOLS_REGEX = Regex("(§.)|([^а-яА-Яa-zA-Z\\d!&,.:#\$%^&*()\\-_+<>\\\"'/\\\\=№~\\s])")
    }

    override suspend fun getServers(): List<Server> = cache.get(Unit) {
        serverRepository.findAllByOnline(true, PageRequest.of(0, 50, Sort.by("creationTimestamp").descending()))
            .map { it.toModel() }
            .toList()
    }

    override suspend fun createServer(
        userId: UUID,
        serverAddress: String
    ): Server {
        // Проверка, забанен ли игрок
        if (userRepository.existsByIdAndBanned(userId, true)) {
            throw MinecraftServerUnavailableException()
        }

        // Проверка на лимит добавления для игрока
        val lastEntity = serverRepository.findLastByUserId(userId)
        if (lastEntity != null) {
            Duration.between(lastEntity.creationTimestamp, LocalDateTime.now()).seconds.let {
                if (it < monirotingProperties.serverSecondsLimit) {
                    throw UserPublishLimitException(monirotingProperties.serverSecondsLimit - it)
                }
            }
        }

        // Получаем IP
        val address = serverAddress.toAddress() ?: throw MinecraftServerUnavailableException()

        // Проверка на лимит добавления для сервера по хосту
        val hostEntity = serverRepository.findByHost(address.host)
        if (hostEntity != null) {
            Duration.between(hostEntity.creationTimestamp, LocalDateTime.now()).seconds.let {
                if (it < monirotingProperties.serverSecondsLimit) {
                    throw ServerPublishLimitException(monirotingProperties.serverSecondsLimit - it)
                }
            }
        }

        // Проверка на лимит добавления для сервера по IPv4
        val addressEntity = serverRepository.findByIpAndPort(address.ip, address.port)
        if (addressEntity != null) {
            Duration.between(addressEntity.creationTimestamp, LocalDateTime.now()).seconds.let {
                if (it < monirotingProperties.serverSecondsLimit) {
                    throw ServerPublishLimitException(monirotingProperties.serverSecondsLimit - it)
                }
            }
        }

        // Пинг сервера
        val ping = try {
            pingerApi.ping("${address.host}:${address.port}")
        }
        catch (e: WebClientResponseException) {
            throw MinecraftServerUnavailableException()
        }
        catch (e: WebClientRequestException) {
            throw GatewayTimeoutException()
        }

        // Сохранение в БД
        return serverRepository.save(ServerEntity(
            userId = userId,
            host = address.host,
            ip = address.ip,
            port = address.port,
            name = ping.description.normalizeServerName(),
            online = true,
            onlinePlayers = ping.players.online,
            maxPlayers = ping.players.max,
            favicon = ping.favicon,
            versionId = versionService.getVersionByProtocol(ping.version.protocol)?.id,
            creationTimestamp = LocalDateTime.now()
        )).toModel()
    }

    @Scheduled(initialDelay = 0, fixedRate = 10000)
    private fun updateOnlineServers() = runBlocking {
        var onlineServers = 0
        var page = 0
        while (onlineServers < 50 && page < 10) {
            val servers = serverRepository.findAllBy(PageRequest.of(page++, 50, Sort.by("creationTimestamp").descending()))
                .toList()
                .map { async { updateServer(it) } }
                .awaitAll()
            if (servers.isEmpty()) {
                break
            }
            onlineServers += servers.count { it.online }
        }
    }

    private suspend fun updateServer(entity: ServerEntity): ServerEntity {
        // Пинг сервера
        var timeout = false
        val ping = try {
            pingerApi.ping("${entity.host}:${entity.port}")
        }
        catch (e: WebClientResponseException) {
            null
        }
        catch (e: WebClientRequestException) {
            timeout = true
            null
        }

        return serverRepository.save(ServerEntity(
            id = entity.id,
            userId = entity.userId,
            host = entity.host,
            ip = entity.ip,
            port = entity.port,
            name = ping?.description?.normalizeServerName() ?: entity.name,
            online = if (timeout) entity.online else ping != null,
            onlinePlayers = if (timeout) entity.onlinePlayers else ping?.players?.online ?: 0,
            maxPlayers = ping?.players?.max ?: entity.maxPlayers,
            versionId = if (ping != null) {
                versionService.getVersionByProtocol(ping.version.protocol)?.id
            } else {
                entity.versionId
            },
            favicon = ping?.favicon ?: entity.favicon,
            creationTimestamp = entity.creationTimestamp
        ))
    }

    private suspend fun String.toAddress() = Address.of(this)

    private fun ServerEntity.toModel() = Server(
        id = id,
        userId = userId,
        name = name,
        status = ServerStatus(
            type = if (online) ServerStatusType.ONLINE else ServerStatusType.OFFLINE,
            onlinePlayers = onlinePlayers,
            maxPlayers = maxPlayers,
        ),
        host = host,
        ip = ip,
        port = port,
        iconBase64 = favicon,
        version = versionId?.let { version -> versionService.getVersion(version) },
        creationTimestamp = creationTimestamp
    )

    private fun String.normalizeServerName()
    = replace(INVALID_SYMBOLS_REGEX, "").let { name ->
        if (name.isBlank()) "Без названия"
        else if (name.length > 64) name.substring(0 until 64)
        else name
    }
}