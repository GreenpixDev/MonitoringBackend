package ru.greenpix.monitoring.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.greenpix.monitoring.const.JwtConst
import ru.greenpix.monitoring.service.JwtService
import java.util.UUID

@Component
class AuthenticationManager(
    private val jwtService: JwtService
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()
        return try {
            val userId = UUID.fromString(jwtService.getAllClaimsFromToken(authToken).subject)
            val claims = jwtService.getAllClaimsFromToken(authToken)
            val rolesMap = claims[JwtConst.CLAIM_ROLE] as List<*>
            mono {
                UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    rolesMap.map { SimpleGrantedAuthority(it.toString()) }
                )
            }
        }
        catch (e: JwtException) {
            Mono.empty()
        }
    }
}