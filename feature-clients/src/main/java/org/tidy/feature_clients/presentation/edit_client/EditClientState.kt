package org.tidy.feature_clients.presentation.edit_client

import org.tidy.feature_clients.domain.model.Client


data class EditClientState(
    val client: Client? = null,
    val razaoSocial: String = "",
    val nomeFantasia: String = "",
    val cnpj: String = "",
    val localizacao: String = "Localização não definida", // 🔥 Agora um único campo
    val cidade: String = "",
    val estado: String = "",
    val rota: String = "",
    val empresasTrabalhadas: List<String> = emptyList(), // 🔥 Lista de empresas
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)