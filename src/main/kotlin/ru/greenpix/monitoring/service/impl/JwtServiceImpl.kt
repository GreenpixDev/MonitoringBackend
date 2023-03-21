package ru.greenpix.monitoring.service.impl

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import ru.greenpix.monitoring.const.JwtConst
import ru.greenpix.monitoring.model.User
import ru.greenpix.monitoring.properties.JwtProperties
import ru.greenpix.monitoring.service.JwtService
import java.util.*

@Service
class JwtServiceImpl(
    private val jwtProperties: JwtProperties
) : JwtService {

    private val key = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())

    private val jwtParser: JwtParser = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()

    override fun getAllClaimsFromToken(token: String): Claims
    = jwtParser.parseClaimsJws(token).body

    override fun generateToken(user: User): String
    = doGenerateToken(hashMapOf(JwtConst.CLAIM_ROLE to user.roles), user.id, jwtProperties.expiration)

    private fun doGenerateToken(claims: Map<String, Any>, userId: UUID, expiration: Long): String {
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + expiration * 1000)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userId.toString())
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .signWith(key)
            .compact()
    }
}