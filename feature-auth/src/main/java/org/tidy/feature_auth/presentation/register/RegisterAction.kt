package org.tidy.feature_auth.presentation.register

sealed class RegisterAction {
    data class OnEmailChange(val email: String) : RegisterAction()
    data class OnPasswordChange(val password: String) : RegisterAction()
    data class OnConfirmPasswordChange(val confirmPassword: String) : RegisterAction()
    object OnRegisterClick : RegisterAction()
}