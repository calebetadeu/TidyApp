import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.tidy.core.domain.onError
import org.tidy.core.domain.onSuccess
import org.tidy.feature_auth.domain.AuthError
import org.tidy.feature_auth.domain.LoginUseCase
import org.tidy.feature_auth.domain.toUiModel
import org.tidy.feature_auth.presentation.login.LoginAction
import org.tidy.feature_auth.presentation.login.LoginState

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> _state.update { it.copy(email = action.email) }
            is LoginAction.OnPasswordChange -> _state.update { it.copy(password = action.password) }
            is LoginAction.OnLoginClick -> login()
            else -> Unit
        }
    }

    private fun login() {
        val email = state.value.email
        val password = state.value.password

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            loginUseCase(email, password)
                .onSuccess { user ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
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