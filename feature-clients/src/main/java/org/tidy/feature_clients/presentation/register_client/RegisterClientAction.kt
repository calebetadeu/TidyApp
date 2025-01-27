package org.tidy.feature_clients.presentation.register_client

sealed class RegisterClientAction {
    data class OnRazaoSocialChange(val value: String) : RegisterClientAction()
    data class OnNomeFantasiaChange(val value: String) : RegisterClientAction()
    data class OnCnpjChange(val value: String) : RegisterClientAction()
    data class OnCidadeChange(val value: String) : RegisterClientAction()
    data class OnEstadoChange(val value: String) : RegisterClientAction()
    data class OnRotaChange(val value: String) : RegisterClientAction() // 🚀 Adicionado campo de rota
    data class OnLocalizacaoChange(val value: String) : RegisterClientAction() // 🚀 Adicionado campo de localização
    data class OnEmpresasTrabalhadasChange(val empresa: String, val isSelected: Boolean) : RegisterClientAction()
    object OnRegisterClick : RegisterClientAction()
}