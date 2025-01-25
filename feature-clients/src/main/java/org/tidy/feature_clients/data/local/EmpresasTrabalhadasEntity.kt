package org.tidy.feature_clients.data.local

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "empresas_trabalhadas",
    primaryKeys = ["codigoTidy", "empresaNome"],
    foreignKeys = [ForeignKey(
        entity = ClientEntity::class,
        parentColumns = ["codigoTidy"],
        childColumns = ["codigoTidy"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EmpresasTrabalhadasEntity(
    val codigoTidy: Int,
    val empresaNome: String
)