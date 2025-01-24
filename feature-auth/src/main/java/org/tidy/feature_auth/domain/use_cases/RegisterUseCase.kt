package org.tidy.feature_auth.domain.use_cases

import android.util.Patterns
import org.tidy.core.domain.Result

import org.tidy.feature_auth.domain.AuthError
import org.tidy.feature_auth.domain.model.User
import org.tidy.feature_auth.domain.repositories.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, confirmPassword: String): Result<User, AuthError> {
        // Validação de campos
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return Result.Error(AuthError.InvalidCredentials)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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