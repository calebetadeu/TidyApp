package org.tidy.feature_clients.data.remote

import ClientEntity
import org.tidy.feature_clients.data.local.Converters
import org.tidy.feature_clients.domain.model.Client


import com.google.firebase.database.PropertyName

data class ClientDto(

    @get:PropertyName("Cidade") @set:PropertyName("Cidade")
    var cidade: String = "",

    @get:PropertyName("Codigo Casa Dos Rolamentos") @set:PropertyName("Codigo Casa Dos Rolamentos")
    var codigoCasaDosRolamentos: Int? = null,

    @get:PropertyName("Codigo Tidy") @set:PropertyName("Codigo Tidy")
    var codigoTidy: Int = 0,

    @get:PropertyName("Empresas Trabalhadas") @set:PropertyName("Empresas Trabalhadas")
    var empresasTrabalhadas: List<String> = emptyList(),

    @get:PropertyName("Estado") @set:PropertyName("Estado")
    var estado: String = "",

    @get:PropertyName("Razão Social") @set:PropertyName("Razão Social")
    var razaoSocial: String = "",

    @get:PropertyName("Rota") @set:PropertyName("Rota")
    var rota: String = "",

    // 🚀 **NOVOS CAMPOS**
    @get:PropertyName("CNPJ") @set:PropertyName("CNPJ")
    var cnpj: String? = null, // Pode ser nulo se não estiver preenchido

    @get:PropertyName("Latitude") @set:PropertyName("Latitude")
    var latitude: Double? = null, // Localização do cliente


    @get:PropertyName("Longitude") @set:PropertyName("Longitude")
    var longitude: Double? = null ,// Localização do cliente

    @get:PropertyName("Nome Fantasia") @set:PropertyName("Nome Fantasia")
    var nomeFantasia: String? = null, // 🚀 Agora é opcional e pode ser nulo


)
// Converter DTO para Modelo de Domínio
fun ClientDto.toDomain(): Client {
    return Client(
        codigoTidy = codigoTidy,
        nomeFantasia ="",
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude =latitude,
        longitude = longitude,
        cnpj = cnpj,
        empresasTrabalhadas = empresasTrabalhadas
    )
}

// Converter DTO para Entity (para salvar no banco)
fun ClientDto.toEntity(): ClientEntity {
    return ClientEntity(
        codigoTidy = codigoTidy,
        nomeFantasia = "",
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude =latitude,
        longitude =longitude,
        cnpj = cnpj,
        empresasTrabalhadas = Converters().fromListString(empresasTrabalhadas) // Convertendo para String (JSON)
    )
}
fun ClientDto.toMap(): Map<String, Any?> {
    return mapOf(
        "Cidade" to cidade,
        "Codigo Casa Dos Rolamentos" to codigoCasaDosRolamentos,
        "Codigo Tidy" to codigoTidy,
        "Empresas Trabalhadas" to empresasTrabalhadas,
        "Estado" to estado,
        "Razão Social" to razaoSocial,
        "Rota" to rota,
        "CNPJ" to cnpj,
        "Latitude" to latitude,
        "Longitude" to longitude
    )
}