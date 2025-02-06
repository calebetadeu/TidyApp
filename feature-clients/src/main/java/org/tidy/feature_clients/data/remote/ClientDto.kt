package org.tidy.feature_clients.data.remote

import ClientEntity
import com.google.firebase.firestore.PropertyName
import org.tidy.feature_clients.domain.model.Client

data class ClientDto(
    val id: String? = null,

    @get:PropertyName("Codigo Tidy") @set:PropertyName("Codigo Tidy")
    var codigoTidy: Int? = null,

    @get:PropertyName("Razão Social") @set:PropertyName("Razão Social")
    var razaoSocial: String = "",

    @get:PropertyName("Cidade") @set:PropertyName("Cidade")
    var cidade: String? = "",

    @get:PropertyName("Estado") @set:PropertyName("Estado")
    var estado: String = "",

    @get:PropertyName("Rota") @set:PropertyName("Rota")
    var rota: String? = null,

    @get:PropertyName("Empresas Trabalhadas") @set:PropertyName("Empresas Trabalhadas")
    var empresasTrabalhadas: List<String> = emptyList(),

    @get:PropertyName("CNPJ") @set:PropertyName("CNPJ")
    var cnpj: String? = null,

    @get:PropertyName("Nome Fantasia") @set:PropertyName("Nome Fantasia")
    var nomeFantasia: String? = null,

    @get:PropertyName("Latitude") @set:PropertyName("Latitude")
    var latitude: Double? = null,

    @get:PropertyName("Longitude") @set:PropertyName("Longitude")
    var longitude: Double? = null,

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
) {
    // 🔥 Construtor sem argumentos necessário para Firestore
    constructor() : this(
        id = null,
        codigoTidy = null,
        razaoSocial = "",
        cidade = "",
        estado = "",
        rota = null,
        empresasTrabalhadas = emptyList(),
        cnpj = null,
        nomeFantasia = null,
        latitude = null,
        longitude = null,
        codigoCasaDosRolamentos = null,
        codigoDitrator = null,
        codigoIndagril = null,
        codigoPrimus = null,
        codigoRomarMann = null
    )

    // 🔥 Conversão segura para evitar erros de tipo ao desserializar
    fun safeConvert(): ClientDto {
        return this.copy(
            codigoTidy = safeInt(codigoTidy),
            latitude = safeDouble(latitude),
            longitude = safeDouble(longitude),
            codigoCasaDosRolamentos = safeInt(codigoCasaDosRolamentos),
            codigoDitrator = safeInt(codigoDitrator),
            codigoIndagril = safeInt(codigoIndagril),
            codigoPrimus = safeInt(codigoPrimus),
            codigoRomarMann = safeInt(codigoRomarMann),
        )
    }

    private fun safeInt(value: Any?): Int? {
        return when (value) {
            is Int -> value
            is String -> value.toIntOrNull()
            is Long -> value.toInt()
            else -> null
        }
    }

    private fun safeDouble(value: Any?): Double? {
        return when (value) {
            is Double -> value
            is String -> value.toDoubleOrNull()
            is Long -> value.toDouble()
            else -> null
        }
    }
}
// 🔹 Converter `ClientDto` para `Client` (para uso no domínio)
fun ClientDto.toDomain(): Client {
    return Client(
        id = id,
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude = latitude,
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

// 🔹 Converter `ClientDto` para `ClientEntity` (para salvar no banco local)
fun ClientDto.toEntity(): ClientEntity {
    return ClientEntity(
        id = id ?: "",
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude = latitude,
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

// 🔹 Converter `ClientDto` para `Map` (para envio ao Firestore)
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