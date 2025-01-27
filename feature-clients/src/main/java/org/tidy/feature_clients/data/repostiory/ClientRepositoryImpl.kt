package org.tidy.feature_clients.data.repostiory

import kotlinx.coroutines.flow.*
import org.tidy.core.domain.ClientError
import org.tidy.core.domain.onSuccess
import org.tidy.feature_clients.data.local.ClientDao
import org.tidy.feature_clients.data.remote.ClientApi
import org.tidy.feature_clients.data.remote.toDomain
import org.tidy.feature_clients.data.remote.toEntity
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.model.toDto
import org.tidy.feature_clients.domain.model.toEntity
import org.tidy.feature_clients.domain.repositories.ClientRepository
import toDomain
import  org.tidy.core.domain.Result
import org.tidy.core.domain.map

class ClientRepositoryImpl(
    private val clientDao: ClientDao,
    private val clientApi: ClientApi,
) : ClientRepository {

    override fun getClients(): Flow<List<Client>> = flow {
        try {
            val remoteClients = clientApi.getClients().firstOrNull() ?: emptyList()
            val clientEntities = remoteClients.map { it.toEntity() }
            clientDao.insertClients(clientEntities) // 🔥 Atualiza banco local
            emit(clientEntities.map { it.toDomain() }) // 🔥 Emite para UI
        } catch (e: Exception) {
            emit(clientDao.getAllClients().firstOrNull()?.map { it.toDomain() }
                ?: emptyList()) // 🔥 Busca local caso falhe
        }
    }

    override fun getFilteredClients(
        rota: String,
        cidade: String,
        empresa: String
    ): Flow<List<Client>> = flow {
        try {
            // 🔹 Busca primeiro no banco local
            val localClients = clientDao.getFilteredClients(rota, cidade, empresa)
                .firstOrNull()?.map { it.toDomain() } ?: emptyList()

            emit(localClients) // 🔥 Emite os dados locais primeiro

            // 🔹 Se não encontrar localmente, busca no Firestore
            if (localClients.isEmpty()) {
                val remoteClients = clientApi.getClients().firstOrNull() ?: emptyList()
                val filteredRemoteClients = remoteClients.filter { client ->
                    (rota.isEmpty() || client.rota == rota) &&
                            (cidade.isEmpty() || client.cidade == cidade) &&
                            (empresa.isEmpty() || client.empresasTrabalhadas.contains(empresa))
                }

                // 🔥 Salva os clientes filtrados no banco local para futuras buscas offline
                clientDao.insertClients(filteredRemoteClients.map { it.toEntity() })

                emit(filteredRemoteClients.map { it.toDomain() }) // 🔥 Retorna a versão `Client`
            }
        } catch (e: Exception) {
            println("Erro ao filtrar clientes: ${e.message}")
            emit(emptyList()) // 🔥 Se houver erro, retorna lista vazia
        }
    }

    override suspend fun syncClients() {
        try {
            val remoteClients = clientApi.getClients().firstOrNull()
            remoteClients?.let {
                clientDao.insertClients(it.map { client -> client.toEntity() }) // 🔥 Insere localmente
            }
        } catch (e: Exception) {
            println("Erro ao sincronizar clientes: ${e.message}") // Log de erro
        }
    }

    override suspend fun addClient(client: Client): Result<Unit, ClientError> {
        return clientApi.addClient(client.toDto()).onSuccess {
            clientDao.insertClient(client.toEntity())
        }
    }

    override suspend fun updateClient(client: Client): Result<Unit, ClientError> {
        return clientApi.updateClient(client.toDto()).onSuccess {
            clientDao.updateClient(client.toEntity()) // 🔥 Atualiza localmente
        }
    }

    override suspend fun getClientById(clientId: Int): Client? {
        return clientDao.getClientById(clientId)?.toDomain() // 🔥 Busca no banco local
    }

    override suspend fun getClientByIdRemote(clientId: Int): Result<Client, ClientError> {
        return clientApi.getClientById(clientId).map {
            it.toDomain()

        }
    }
}