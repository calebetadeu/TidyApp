package org.tidy.feature_auth.domain

import org.tidy.core.domain.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User, AuthError>
    suspend fun register(email: String, password: String): Result<User, AuthError>
    fun logout()
}