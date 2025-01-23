package org.tidy.feature_auth.domain

import org.tidy.core.domain.Result

import org.tidy.feature_auth.domain.AuthError

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, confirmPassword: String): Result<User, AuthError> {
        // Validação de campos
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return Result.Error(AuthError.InvalidCredentials)
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error(AuthError.InvalidEmailFormat)
        }
        if (password.length < 6) {
            return Result.Error(AuthError.WeakPassword)
        }
        if (password != confirmPassword) {
            return Result.Error(AuthError.WeakPassword)
        }

        return authRepository.register(email, password)
    }
}