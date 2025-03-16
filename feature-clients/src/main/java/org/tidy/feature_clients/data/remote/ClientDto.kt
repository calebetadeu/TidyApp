package org.tidy.feature_clients.data.remote

data class ClientDto(
    val codigo_ditrator: String?,
    val codigo_casa_dos_rolamentos: String?,
    val codigo_romar_mann: String?,
    val nome_fantasia: String?,
    val razao_social: String?,
    val cnpj: String? = "",
    val rota: String?,
    val cidade: String?,
    val estado: String,
    val empresas_trabalhadas: List<String>?,
    val latitude: Double?,
    val longitude: Double?,
    val id: Long
)
