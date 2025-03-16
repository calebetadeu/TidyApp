package org.tidy.feature_clients.data.repository


import org.tidy.feature_clients.core.Result
import org.tidy.feature_clients.data.local.ClientDao
import org.tidy.feature_clients.data.local.toDomain
import org.tidy.feature_clients.data.local.toEntity
import org.tidy.feature_clients.data.remote.RetrofitClientDataSource
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repository.ClientRepository

class ClientRepositoryImpl(
    private val remoteDataSource: RetrofitClientDataSource,
    private val clientDao: ClientDao
) : ClientRepository {

    override suspend fun getClients(skip: Int, limit: Int): List<Client> {
        // Tenta buscar os clientes via API
        val remoteResult = remoteDataSource.getClients(skip, limit)
        return if (remoteResult is Result.Success) {
            val remoteClients = remoteResult.data
            // Atualiza os dados locais
            clientDao.insertClients(remoteClients.map { it.toEntity() })
            remoteClients.map { it.toEntity().toDomain() }
        } else {
            // Caso erro remoto, carrega os dados armazenados
            clientDao.getAllClients().map { it.toDomain() }
        }
    }

    override suspend fun filterClients(
        razaoSocial: String?,
        estado: String?,
        cidade: String?
    ): List<Client> {
        val remoteResult = remoteDataSource.filterClients(razaoSocial, estado, cidade)
        return if (remoteResult is Result.Success) {
            val filtered = remoteResult.data
            // Opcional: atualizar os dados locais com os filtrados
            clientDao.insertClients(filtered.map { it.toEntity() })
            filtered.map { it.toEntity().toDomain() }
        } else {
            // Caso de erro, pode aplicar filtro local (caso os dados estejam disponíveis)
            clientDao.getAllClients().filter { client ->
                (razaoSocial == null || client.razaoSocial?.contains(razaoSocial, ignoreCase = true) == true) &&
                        (estado == null || client.estado.equals(estado, ignoreCase = true)) &&
                        (cidade == null || client.cidade.equals(cidade, ignoreCase = true))
            }.map { it.toDomain() }
        }
    }

    override suspend fun createClient(client: Client): Client {
        // Primeiro, cria remotamente
        val dto = client.toEntity().let { it.toDomain().let { domain ->
            // Converter de Domain para DTO (implemente se necessário)
            // Neste exemplo, usamos a função inversa toEntity() para obter ClientEntity e depois converter para ClientDto
            // Você pode implementar uma função de mapeamento domain -> dto separada
            org.tidy.feature_clients.data.remote.ClientDto(
                id = domain.id,
                codigo_ditrator = domain.codigoDitrator,
                codigo_casa_dos_rolamentos = domain.codigoCasaDosRolamentos,
                codigo_romar_mann = domain.codigoRomarMann,
                nome_fantasia = domain.nomeFantasia,
                razao_social = domain.razaoSocial,
                rota = domain.rota,
                cidade = domain.cidade,
                estado = domain.estado.toString(),
                empresas_trabalhadas = domain.empresasTrabalhadas,
                latitude = domain.latitude!!,
                longitude = domain.longitude
            )
        } }
        val remoteResult = remoteDataSource.createClient(dto)
        return if (remoteResult is Result.Success) {
            val createdDto = remoteResult.data
            // Salva no Room
            clientDao.insertClient(createdDto.toEntity())
            createdDto.toEntity().toDomain()
        } else {
            throw Exception("Erro ao criar cliente remotamente")
        }
    }

    override suspend fun getClientDetails(clientId: Long): Client? {
        val remoteResult = remoteDataSource.getClientDetails(clientId)
        return if (remoteResult is Result.Success) {
            val clientDto = remoteResult.data
            // Atualiza ou insere no Room
            clientDao.insertClient(clientDto.toEntity())
            clientDto.toEntity().toDomain()
        } else {
            // Se houver erro, tenta buscar localmente
            clientDao.getClientById(clientId)?.toDomain()
        }
    }

    override suspend fun updateClient(client: Client) {
        val dto = org.tidy.feature_clients.data.remote.ClientDto(
            id = client.id,
            codigo_ditrator = client.codigoDitrator,
            codigo_casa_dos_rolamentos = client.codigoCasaDosRolamentos,
            codigo_romar_mann = client.codigoRomarMann,
            nome_fantasia = client.nomeFantasia,
            razao_social = client.razaoSocial,
            rota = client.rota,
            cidade = client.cidade,
            estado = client.estado.toString(),
            empresas_trabalhadas = client.empresasTrabalhadas,
            latitude = client.latitude,
            longitude = client.longitude,
            cnpj = client.cnpj
        )
        val remoteResult = remoteDataSource.updateClient(client.id, dto)
        if (remoteResult is Result.Success) {
            // Atualiza o Room
            clientDao.updateClient(dto.toEntity())
        } else {
            // Se falhar a atualização remota, trate o erro ou marque o registro para sincronização futura
        }
    }

    override suspend fun deleteClient(clientId: Long) {
        val remoteResult = remoteDataSource.deleteClient(clientId)
        if (remoteResult is Result.Success) {
            clientDao.getClientById(clientId)?.let { clientDao.deleteClient(it) }
        } else {
            // Trate o erro conforme sua estratégia offline‑first
        }
    }

    override suspend fun getLocations(): List<org.tidy.feature_clients.data.remote.LocationDto> {
        val remoteResult = remoteDataSource.getLocations()
        return if (remoteResult is Result.Success) {
            remoteResult.data
        } else {
            emptyList() // ou uma lista padrão
        }
    }
    override suspend fun syncClients() {
        // Tenta buscar todos os clientes do remoto e atualizar o Room
        val remoteResult = remoteDataSource.getClients()
        if (remoteResult is Result.Success) {
            val remoteClients = remoteResult.data
            // Atualiza o banco local com os dados remotos
            clientDao.insertClients(remoteClients.map { it.toEntity() })
        }
        // Se ocorrer erro, pode ser registrada uma tentativa de sincronização posterior
    }
}