package ru.greenpix.monitoring.service

import io.jsonwebtoken.Claims
import kotlinx.coroutines.Job
import java.util.*

interface EmailService {

    fun getAllClaimsFromToken(token: String): Claims

    fun generateToken(userId: UUID): String

    fun sendConfirmationMail(targetEmail: String, token: String): Job

    fun sendRestoreMail(targetEmail: String, token: String): Job

}