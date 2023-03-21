package ru.greenpix.monitoring.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.greenpix.monitoring.dto.VersionDto
import ru.greenpix.monitoring.service.VersionService

@Tag(name = "Minecraft версии")
@RestController
@RequestMapping("api/v1/versions")
class VersionController(
    private val versionService: VersionService
) {

    @Operation(summary = "Получить список всевозможных версий")
    @GetMapping
    suspend fun getVersions(): List<VersionDto>
    = versionService.getAllVersions()
        .map {
            VersionDto(
                id = it.id,
                name = it.name,
                type = it.type
            )
        }
}