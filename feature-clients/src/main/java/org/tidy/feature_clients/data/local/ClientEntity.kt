import androidx.room.Entity
import androidx.room.PrimaryKey
import org.tidy.feature_clients.data.local.Converters
import org.tidy.feature_clients.domain.model.Client


@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey val codigoTidy: Int,
    val nomeFantasia: String?,
    val razaoSocial: String,
    val rota: String,
    val cidade: String,
    val estado: String,
    val cnpj: String?,
    val latitude: Double?,
    val longitude: Double?,
    val empresasTrabalhadas: String
)
fun ClientEntity.toDomain(): Client {
    return Client(
        codigoTidy = codigoTidy,
        nomeFantasia = nomeFantasia,
        razaoSocial = razaoSocial,
        rota = rota,
        cidade = cidade,
        estado = estado,
        latitude = latitude,
        longitude = longitude,
        cnpj = cnpj,
        empresasTrabalhadas = Converters().toListString(empresasTrabalhadas)
    )
}