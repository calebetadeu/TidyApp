package org.tidy.feature_clients.presentation.register_client
data class RegisterClientState(
    val razaoSocial: String = "",
    val nomeFantasia: String = "",
    val cnpj: String = "",
    val cidade: String = "",
    val estado: String = "",
    val rota: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val localizacao: String = "Localização não definida",
    val empresasTrabalhadas: List<String> = emptyList(),
    val listaEmpresas: List<String> = listOf("Ditrator", "Casa Dos Rolamentos", "Romar Mann", "Indagril", "Primus","Smart Crops"),
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    // 🔥 Validação do formulário
    val isFormValid: Boolean
        get() = razaoSocial.isNotBlank() && cidade.isNotBlank() && estado.isNotBlank() && rota.isNotBlank()
}