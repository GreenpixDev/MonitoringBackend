package ru.greenpix.monitoring.service.impl

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.greenpix.monitoring.database.entity.UserEntity
import ru.greenpix.monitoring.database.repository.UserRepository
import ru.greenpix.monitoring.exception.*
import ru.greenpix.monitoring.model.Role
import ru.greenpix.monitoring.model.User
import ru.greenpix.monitoring.properties.SecurityProperties
import ru.greenpix.monitoring.service.EmailService
import ru.greenpix.monitoring.service.UserService
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val emailService: EmailService,
    private val passwordEncoder: PasswordEncoder,
    private val securityProperties: SecurityProperties
) : UserService {

    override suspend fun signUp(username: String, password: CharSequence): User {
        if (userRepository.existsByEmail(username)) {
            throw DuplicateEmailException()
        }

        val userId = UUID.randomUUID()
        val token = emailService.generateToken(userId)

        val user = userRepository.save(UserEntity(
            id = userId,
            email = username,
            password = passwordEncoder.encode(password),
            active = false,
            banned = false,
            verifyToken = token,
            registrationTimestamp = LocalDateTime.now(),
            restoreToken = null,
            restoreTimestamp = null
        ).new()).toModel()

        emailService.sendConfirmationMail(username, token)
        return user
    }

    override suspend fun signIn(username: String, password: CharSequence): User {
        val entity = userRepository.findByEmail(username)
            ?: throw AuthenticationException()
        if (!passwordEncoder.matches(password, entity.password)) {
            throw AuthenticationException()
        }
        if (entity.banned) {
            throw AuthenticationException()
        }
        if (!entity.active) {
            throw UserInactiveException()
        }
        return User(
            id = entity.id,
            username = entity.email,
            roles = listOf(Role.ROLE_USER)
        )
    }

    override suspend fun restore(username: String) {
        val entity = userRepository.findByEmail(username) ?: return
        if (!entity.active || entity.banned) return
        if (entity.restoreTimestamp != null) {
            Duration.between(entity.restoreTimestamp, LocalDateTime.now()).seconds.let {
                if (it < securityProperties.restoreSecondsLimit) {
                    throw UserRestoreLimitException()
                }
            }
        }

        val user = entity.toModel()
        val token = emailService.generateToken(user.id)

        userRepository.save(UserEntity(
            id = entity.id,
            email = entity.email,
            password = entity.password,
            active = true,
            banned = false,
            verifyToken = entity.verifyToken,
            registrationTimestamp = entity.registrationTimestamp,
            restoreToken = token,
            restoreTimestamp = LocalDateTime.now()
        ))

        emailService.sendRestoreMail(username, token)
    }

    override suspend fun verifyEmail(token: String): User {
        val userId = try {
            emailService.getAllClaimsFromToken(token).subject
        }
        catch (e: ExpiredJwtException) {
            throw ExpiredTokenException()
        }
        catch (e: JwtException) {
            throw InvalidTokenException()
        }

        val entity = userRepository.findById(UUID.fromString(userId)) ?: throw UserNotFoundException()
        if (entity.active || entity.verifyToken != token) {
            throw InvalidTokenException()
        }

        return userRepository.save(UserEntity(
            id = entity.id,
            email = entity.email,
            password = entity.password,
            active = true,
            banned = entity.banned,
            verifyToken = null,
            registrationTimestamp = entity.registrationTimestamp,
            restoreToken = null,
            restoreTimestamp = null
        )).toModel()
    }

    override suspend fun verifyRestore(token: String, password: CharSequence): User {
        val userId = try {
            emailService.getAllClaimsFromToken(token).subject
        }
        catch (e: ExpiredJwtException) {
            throw ExpiredTokenException()
        }
        catch (e: JwtException) {
            throw InvalidTokenException()
        }

        val entity = userRepository.findById(UUID.fromString(userId)) ?: throw UserNotFoundException()
        if (!entity.active) {
            throw UserNotFoundException()
        }
        if (entity.restoreToken != token) {
            throw InvalidTokenException()
        }

        return userRepository.save(UserEntity(
            id = entity.id,
            email = entity.email,
            password = passwordEncoder.encode(password),
            active = true,
            banned = entity.banned,
            verifyToken = entity.verifyToken,
            registrationTimestamp = entity.registrationTimestamp,
            restoreToken = null,
            restoreTimestamp = entity.restoreTimestamp
        )).toModel()
    }

    private fun UserEntity.toModel() = User(
        id = id,
        username = email,
        roles = listOf(Role.ROLE_USER)
    )
}