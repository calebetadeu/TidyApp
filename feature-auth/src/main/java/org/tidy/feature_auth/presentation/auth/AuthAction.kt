package org.tidy.feature_auth.presentation.auth

sealed interface AuthAction {
    data object CheckAuth : AuthAction
    data object Logout : AuthAction
}