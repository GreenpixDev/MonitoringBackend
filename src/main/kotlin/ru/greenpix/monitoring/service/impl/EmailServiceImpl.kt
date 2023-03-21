package ru.greenpix.monitoring.service.impl

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.mail.internet.InternetAddress
import kotlinx.coroutines.*
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import ru.greenpix.monitoring.model.Address
import ru.greenpix.monitoring.properties.EmailJwtProperties
import ru.greenpix.monitoring.service.EmailService
import java.io.InputStreamReader
import java.util.*

@Service
class EmailServiceImpl(
    private val emailJwtProperties: EmailJwtProperties,
    private val emailSender: JavaMailSender
) : EmailService {

    private val key = Keys.hmacShaKeyFor(emailJwtProperties.secret.toByteArray())

    private val jwtParser: JwtParser = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()

    private val confirmationHtml: String
    private val restoreHtml: String

    init {
        this.confirmationHtml = javaClass.getResourceAsStream("/mail/confirmation.html")
            ?.let { InputStreamReader(it).readText() } ?: throw IllegalArgumentException("Cannot read /mail/confirmation.html")
        this.restoreHtml = javaClass.getResourceAsStream("/mail/restore.html")
            ?.let { InputStreamReader(it).readText() } ?: throw IllegalArgumentException("Cannot read /mail/restore.html")
    }

    override fun sendConfirmationMail(
        targetEmail: String,
        token: String
    ): Job {
        return sendMail(
            targetEmail = targetEmail,
            subject = "Подтвердите адрес электронной почты",
            html = confirmationHtml.replace("\$token", token)
        )
    }

    override fun sendRestoreMail(
        targetEmail: String,
        token: String
    ): Job {
        return sendMail(
            targetEmail = targetEmail,
            subject = "Восстановление пароля",
            html = restoreHtml.replace("\$token", token)
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun sendMail(
        targetEmail: String,
        subject: String,
        html: String
    ): Job {
        val message = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        helper.setFrom(InternetAddress("noreply@new-servers.ru", "Новые сервера Minecraft"))
        helper.setTo(targetEmail)
        helper.setSubject(subject)
        helper.setText(html, true)

        return GlobalScope.launch(Dispatchers.IO) {
            emailSender.send(message)
        }
    }

    override fun getAllClaimsFromToken(token: String): Claims
            = jwtParser.parseClaimsJws(token).body

    override fun generateToken(userId: UUID): String
            = doGenerateToken(emptyMap(), userId, emailJwtProperties.expiration)

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