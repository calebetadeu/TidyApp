package org.tidy.feature_clients.data.remote

import ClientEntity
import org.tidy.feature_clients.data.local.Converters
import org.tidy.feature_clients.domain.model.Client



import com.google.firebase.firestore.PropertyName

data class ClientDto(

    @get:PropertyName("Codigo Tidy") @set:PropertyName("Codigo Tidy")
    var codigoTidy: Int = 0, // 🔥 Código principal do cliente

    @get:PropertyName("Razão Social") @set:PropertyName("Razão Social")
    var razaoSocial: String = "",

    @get:PropertyName("Cidade") @set:PropertyName("Cidade")
    var cidade: String? = "",

    @get:PropertyName("Estado") @set:PropertyName("Estado")
    var estado: String = "",

    @get:PropertyName("Rota") @set:PropertyName("Rota")
    var rota: String? = null, // 🔥 Permitir valores nulos

    @get:PropertyName("Empresas Trabalhadas") @set:PropertyName("Empresas Trabalhadas")
    var empresasTrabalhadas: List<String> = emptyList(),

    @get:PropertyName("CNPJ") @set:PropertyName("CNPJ")
    var cnpj: String? = null, // 🔥 Permitir valores nulos

    @get:PropertyName("Nome Fantasia") @set:PropertyName("Nome Fantasia")
    var nomeFantasia: String? = null, // 🔥 Permitir valores nulos

    @get:PropertyName("Latitude") @set:PropertyName("Latitude")
    var latitude: Double? = null, // 🔥 Permitir valores nulos

    @get:PropertyName("Longitude") @set:PropertyName("Longitude")
    var longitude: Double? = null, // 🔥 Permitir valores nulos

    // **🚀 Adicionando os códigos extras**
    @get:PropertyName("Codigo Casa Dos Rolamentos") @set:PropertyName("Codigo Casa Dos Rolamentos")
    var codigoCasaDosRolamentos: Int? = null,

    @get:PropertyName("Codigo Ditrator") @set:PropertyName("Codigo Ditrator")
    var codigoDitrator: Int? = null,

    @get:PropertyName("Codigo Indagril") @set:PropertyName("Codigo Indagril")
    var codigoIndagril: Int? = null,

    @get:PropertyName("Codigo Primus") @set:PropertyName("Codigo Primus")
    var codigoPrimus: Int? = null,

    @get:PropertyName("Codigo Romar Mann") @set:PropertyName("Codigo Romar Mann")
    var codigoRomarMann: Int? = null
)
fun ClientDto.toDomain(): Client {
    return Client(
        codigoTidy = codigoTidy,
        nomeFantasia =nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude =latitude,
        longitude = longitude,
        cnpj = cnpj,
        empresasTrabalhadas = empresasTrabalhadas,
        codigoCasaDosRolamentos = codigoCasaDosRolamentos,
        codigoDitrator = codigoDitrator,
        codigoIndagril = codigoIndagril,
        codigoPrimus = codigoPrimus,
        codigoRomarMann = codigoRomarMann
    )
}

// Converter DTO para Entity (para salvar no banco)
fun ClientDto.toEntity(): ClientEntity {
    return ClientEntity(
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude =latitude,
        longitude =longitude,
        cnpj = cnpj,
        empresasTrabalhadas = empresasTrabalhadas,
        codigoCasaDosRolamentos = codigoCasaDosRolamentos,
        codigoDitrator = codigoDitrator,
        codigoIndagril = codigoIndagril,
        codigoPrimus = codigoPrimus,
        codigoRomarMann = codigoRomarMann
    )
}
fun ClientDto.toMap(): Map<String, Any?> {
    return mapOf(
        "Cidade" to cidade,
        "Codigo Casa Dos Rolamentos" to codigoCasaDosRolamentos,
        "Codigo Ditrator" to codigoDitrator,
        "Codigo Indagril" to codigoIndagril,
        "Codigo Primus" to codigoPrimus,
        "Codigo Romar Mann" to codigoRomarMann,
        "Codigo Tidy" to codigoTidy,
        "Empresas Trabalhadas" to empresasTrabalhadas,
        "Estado" to estado,
        "Nome Fantasia" to nomeFantasia,
        "Razão Social" to razaoSocial,
        "Rota" to rota,
        "CNPJ" to cnpj,
        "Latitude" to latitude,
        "Longitude" to longitude
    )
}