package org.tidy.feature_auth.domain

import org.tidy.core.domain.Result



class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User, AuthError> {
        // Validação de campos
        if (email.isBlank() || password.isBlank()) {
            return Result.Error(AuthError.InvalidCredentials)
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error(AuthError.InvalidEmailFormat)
        }
        if (password.length < 6) {
            return Result.Error(AuthError.WeakPassword)
        }

        return authRepository.login(email, password)
    }
}