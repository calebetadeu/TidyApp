package org.tidy.feature_clients.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey val id: Long,
    val codigoDitrator: String?,
    val codigoCasaDosRolamentos: String?,
    val codigoRomarMann: String?,
    val nomeFantasia: String?,
    val razaoSocial: String?,
    val cnpj:String?,
    val rota: String?,
    val cidade: String?,
    val estado: String?,
    val empresasTrabalhadas: List<String>?,
    val latitude: Double?,
    val longitude: Double?
)