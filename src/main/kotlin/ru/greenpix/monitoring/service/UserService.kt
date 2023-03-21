package ru.greenpix.monitoring.service

import ru.greenpix.monitoring.model.User
import java.util.UUID

interface UserService {

    suspend fun signUp(username: String, password: CharSequence): User

    suspend fun signIn(username: String, password: CharSequence): User

    suspend fun restore(username: String)

    suspend fun verifyEmail(token: String): User

    suspend fun verifyRestore(token: String, password: CharSequence): User

}