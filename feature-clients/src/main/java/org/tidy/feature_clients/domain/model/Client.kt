package org.tidy.feature_clients.domain.model

import ClientEntity
import org.tidy.feature_clients.data.remote.ClientDto

data class Client(
    val id: String? = null,
    val codigoTidy: Int? =null,
    val nomeFantasia: String?,
    val razaoSocial: String,
    val rota: String?,
    val cnpj: String?,
    val cidade: String?,
    val estado: String,
//    val latitude: Double?,
//    val longitude: Double?,
    val empresasTrabalhadas: List<String>,
    val codigoCasaDosRolamentos: Int? = 0,
    val codigoDitrator: Int? = 0,
    val codigoIndagril: Int? = 0,
    val codigoPrimus: Int? = 0,
    val codigoRomarMann: Int? = 0,
    val latitude: Double?,
    val longitude: Double?
)

fun Client.toDto(): ClientDto {
    return ClientDto(
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
//        latitude = latitude,
//        longitude = longitude,
        cnpj = cnpj,
        empresasTrabalhadas = empresasTrabalhadas
    )
}

fun Client.toEntity(): ClientEntity {
    return ClientEntity(
        id = id.toString(),
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
//        latitude = latitude,
//        longitude = longitude,
        cnpj = cnpj,
        empresasTrabalhadas = empresasTrabalhadas,
        codigoCasaDosRolamentos = codigoCasaDosRolamentos,
        codigoDitrator = codigoDitrator,
        codigoIndagril = codigoIndagril,
        codigoPrimus = codigoPrimus,
        codigoRomarMann =codigoPrimus,
        latitude = latitude,
        longitude = longitude
    )
}