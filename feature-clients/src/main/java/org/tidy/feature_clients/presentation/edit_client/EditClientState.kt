package org.tidy.feature_clients.presentation.edit_client

import org.tidy.feature_clients.domain.model.Client


data class EditClientState(
    val client: Client? = null,
    val razaoSocial: String = "",
    val nomeFantasia: String = "",
    val cnpj: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,  // 🚀 Adicionado para exibir mensagens de sucesso
    val errorMessage: String? = null     // 🚀 Adicionado para exibir mensagens de erro
)