package org.tidy.feature_auth.presentation.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tidy.core.domain.onError
import org.tidy.core.domain.onSuccess
import org.tidy.feature_auth.domain.AuthError
import org.tidy.feature_auth.domain.use_cases.RegisterUseCase
import org.tidy.feature_auth.domain.model.toUiModel


class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnEmailChange -> _state.update { it.copy(email = action.email) }
            is RegisterAction.OnPasswordChange -> _state.update { it.copy(password = action.password) }
            is RegisterAction.OnConfirmPasswordChange -> _state.update { it.copy(confirmPassword = action.confirmPassword) }
            is RegisterAction.OnRegisterClick -> register()
        }
    }

    private fun register() {
        val email = state.value.email
        val password = state.value.password
        val confirmPassword = state.value.confirmPassword

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            registerUseCase(email, password, confirmPassword)
                .onSuccess { user ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRegistered = true,
                            email = user.toUiModel().email.toString()
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapAuthError(error)
                        )
                    }
                }
        }
    }

    private fun mapAuthError(error: AuthError): String {
        return when (error) {
            is AuthError.UserNotFound -> "Usuário não encontrado."
            is AuthError.InvalidCredentials -> "E-mail ou senha inválidos."
            is AuthError.EmailAlreadyInUse -> "Este e-mail já está cadastrado."
            is AuthError.WeakPassword -> "A senha deve ter pelo menos 6 caracteres."
            is AuthError.InvalidEmailFormat -> "Formato de e-mail inválido."
            is AuthError.UnknownError -> "Ocorreu um erro inesperado."
        }
    }
}