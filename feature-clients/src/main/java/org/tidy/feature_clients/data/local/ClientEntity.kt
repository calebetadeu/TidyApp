import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tidy.feature_clients.data.local.Converters
import org.tidy.feature_clients.domain.model.Client


@Entity(tableName = "clients")
@TypeConverters(Converters::class) // 🔥 Garante que os conversores estão sendo aplicados
data class ClientEntity(
    @PrimaryKey val id: String,
    val codigoTidy: Int?,
    val nomeFantasia: String?,
    val razaoSocial: String,
    val rota: String?,
    val cidade: String?,
    val estado: String,
    val cnpj: String?,
//    val latitude: Double?,
//    val longitude: Double?,
    val empresasTrabalhadas: List<String>,

    // 🔥 Novos campos que foram adicionados recentemente
    val codigoCasaDosRolamentos: Int?,
    val codigoDitrator: Int?,
    val codigoIndagril: Int?,
    val codigoPrimus: Int?,
    val codigoRomarMann: Int?,
    val latitude: Double?,
    val longitude: Double?
)
fun ClientEntity.toDomain(): Client {
    return Client(
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cnpj = cnpj,
        cidade = cidade,
//        latitude = latitude,
//        longitude = longitude,
        estado = estado,
        empresasTrabalhadas = empresasTrabalhadas,
        codigoCasaDosRolamentos = codigoCasaDosRolamentos,
        codigoDitrator = codigoDitrator,
        codigoIndagril = codigoIndagril,
        codigoPrimus = codigoPrimus,
        codigoRomarMann = codigoRomarMann,
        latitude = latitude,
        longitude = longitude
    )
}