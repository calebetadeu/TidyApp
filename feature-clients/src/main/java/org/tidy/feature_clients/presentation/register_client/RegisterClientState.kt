package org.tidy.feature_clients.presentation.register_client

data class RegisterClientState(
    val razaoSocial: String = "",
    val nomeFantasia: String = "",
    val cnpj: String = "",
    val cidade: String = "",
    val estado: String = "",
    val rota: String = "", // 🚀 Adicionado
    val latitude: Double? = null, // 🚀 Adicionado
    val longitude: Double? = null // 🚀 Adicionado
) {
    val isFormValid: Boolean
        get() = razaoSocial.isNotBlank() && cnpj.isNotBlank() && cidade.isNotBlank() && estado.isNotBlank()
}