package org.tidy.feature_clients.presentation.register_list

data class RegisterClientState(
    val razaoSocial: String = "",
    val nomeFantasia: String = "",
    val cnpj: String = "",
    val estado: String = "",
    val cidade: String = "",
    val rota: String = "",
    val localizacao: String = "",
    val empresasTrabalhadas: List<String> = emptyList(),
    val listaEmpresas: List<String> = listOf("Casa Dos Rolamentos", "Ditrator", "Indagril", "Agromann", "Romar Mann", "Primus", "Smart Crops"),
    val isFormValid: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)