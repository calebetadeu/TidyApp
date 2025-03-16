package org.tidy.feature_clients.domain.model

data class Client(
    val id: Long,
    val codigoDitrator: String?,
    val codigoCasaDosRolamentos: String?,
    val codigoRomarMann: String?,
    val cnpj: String?,
    val nomeFantasia: String?,
    val razaoSocial: String?,
    val rota: String?,
    val cidade: String?,
    val estado: String?,
    val empresasTrabalhadas: List<String>?,
    val latitude: Double?,
    val longitude: Double?
)