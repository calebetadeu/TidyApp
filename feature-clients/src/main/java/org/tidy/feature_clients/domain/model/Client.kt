package org.tidy.feature_clients.domain.model

import ClientEntity
import org.tidy.feature_clients.data.remote.ClientDto

data class Client(
    val codigoTidy: Int,
    val nomeFantasia: String?,
    val razaoSocial: String,
    val rota: String,
    val cnpj: String?,
    val cidade: String,
    val estado: String,
    val latitude: Double?,
    val longitude: Double?,
    val empresasTrabalhadas: List<String>
)

fun Client.toDto(): ClientDto {
    return ClientDto(
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude = latitude,
        longitude = longitude,
        empresasTrabalhadas = empresasTrabalhadas
    )
}

fun Client.toEntity(): ClientEntity {
    return ClientEntity(
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude = latitude,
        longitude = longitude,
        empresasTrabalhadas = empresasTrabalhadas?.joinToString(",") ?: "",
        cnpj = cnpj
    )
}