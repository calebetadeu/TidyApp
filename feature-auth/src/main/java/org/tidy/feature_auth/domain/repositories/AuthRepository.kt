package org.tidy.feature_auth.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.tidy.core.domain.Result
import org.tidy.feature_auth.domain.AuthError
import org.tidy.feature_auth.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User, AuthError>
    suspend fun register(email: String, password: String): Result<User, AuthError>
    val isAuthenticated: Flow<Boolean>
    val userEmail: Flow<String?>

    suspend fun saveUser(email: String)
    suspend fun logout()
}