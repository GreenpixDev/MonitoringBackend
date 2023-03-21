package ru.greenpix.monitoring.service

import io.jsonwebtoken.Claims
import ru.greenpix.monitoring.model.User
import java.util.UUID

interface JwtService {

    fun getAllClaimsFromToken(token: String): Claims

    fun generateToken(user: User): String

}