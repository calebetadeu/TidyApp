package org.tidy.feature_auth.presentation.login

sealed class LoginAction {
    data class OnEmailChange(val email: String) : LoginAction()
    data class OnPasswordChange(val password: String) : LoginAction()
    object OnLoginClick : LoginAction()
    object OnRegisterClick : LoginAction()
    object OnForgotPasswordClick : LoginAction()
}