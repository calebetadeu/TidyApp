package org.tidy.feature_auth.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tidy.feature_auth.domain.use_cases.AuthUseCase

class AuthViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        checkAuthentication()
    }

    private fun checkAuthentication() {
        viewModelScope.launch {
            authUseCase.isAuthenticated.collect { isAuthenticated ->
                authUseCase.userEmail.collect { email ->
                    _state.update {
                        it.copy(
                            isAuthenticated = isAuthenticated,
                            userEmail = email
                        )
                    }
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authUseCase.logout()
            _state.update { AuthState(isAuthenticated = false) }
        }
    }
}