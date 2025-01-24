package org.tidy.feature_auth.domain.use_cases

import kotlinx.coroutines.flow.Flow
import org.tidy.feature_auth.domain.repositories.AuthRepository

class AuthUseCase(private val authRepository: AuthRepository) {

    // Expor o estado de autenticação
    val isAuthenticated: Flow<Boolean> = authRepository.isAuthenticated

    // Obter o e-mail do usuário
    val userEmail: Flow<String?> = authRepository.userEmail

    // Salvar usuário autenticado
    suspend fun saveUser(email: String) = authRepository.saveUser(email)

    // Logout do usuário
    suspend fun logout() = authRepository.logout()
}