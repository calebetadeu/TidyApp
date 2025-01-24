package org.tidy.feature_auth.presentation.auth

data class AuthState(
    val isAuthenticated: Boolean = false,
    val userEmail: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)