package org.tidy.feature_clients.presentation.register_client


sealed class RegisterClientAction {
    data class OnRazaoSocialChange(val value: String) : RegisterClientAction()
    data class OnNomeFantasiaChange(val value: String) : RegisterClientAction()
    data class OnCnpjChange(val value: String) : RegisterClientAction()
    data class OnCidadeChange(val value: String) : RegisterClientAction()
    data class OnEstadoChange(val value: String) : RegisterClientAction()
    data class OnRotaChange(val value: String) : RegisterClientAction() // 🚀 Adicionado
    data class OnLatitudeChange(val value: Double) : RegisterClientAction() // 🚀 Adicionado
    data class OnLongitudeChange(val value: Double) : RegisterClientAction() // 🚀 Adicionado
    object OnRegisterClick : RegisterClientAction()
}