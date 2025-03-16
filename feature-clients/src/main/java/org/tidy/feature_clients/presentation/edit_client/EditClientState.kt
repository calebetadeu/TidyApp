package org.tidy.feature_clients.presentation.edit_client

import org.tidy.feature_clients.domain.model.Localization

data class EditClientState(
    val id: Long = 0L,
    val razaoSocial: String = "",
    val nomeFantasia: String = "",
    val cnpj: String = "",
    val estado: String = "",
    val cidade: String = "",
    val rota: String = "",
    val localizacao: Localization? = null,
    val empresasTrabalhadas: List<String> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)