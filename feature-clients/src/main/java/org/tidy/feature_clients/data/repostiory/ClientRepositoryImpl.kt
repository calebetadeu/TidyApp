package org.tidy.feature_clients.data.repostiory

import android.util.Log
import kotlinx.coroutines.flow.*
import org.tidy.feature_clients.data.local.ClientDao
import org.tidy.feature_clients.data.remote.ClientApi
import org.tidy.feature_clients.data.remote.toDomain
import org.tidy.feature_clients.data.remote.toEntity
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.model.toDto
import org.tidy.feature_clients.domain.model.toEntity
import org.tidy.feature_clients.domain.repositories.ClientRepository
import toDomain

class ClientRepositoryImpl(
    private val clientDao: ClientDao,
    private val clientApi: ClientApi,
) : ClientRepository {
    override fun getClients(): Flow<List<Client>> = flow {
        try {
            val remoteClients = clientApi.getClients().firstOrNull() ?: emptyList()

            if (remoteClients.isNotEmpty()) {
                clientDao.insertClients(remoteClients.map { it.toEntity() }) // Atualiza o banco local
                emit(remoteClients.map { it.toDomain() }) // Emite os dados buscados online
            } else {
                Log.w("ClientRepositoryImpl", "Nenhum cliente remoto encontrado. Buscando localmente...")
            }
        } catch (e: Exception) {
            Log.e("ClientRepositoryImpl", "Erro ao buscar clientes remotos: ${e.message}. Buscando localmente...", e)
        }

        val localClients = clientDao.getAllClients().firstOrNull() ?: emptyList()
        emit(localClients.map { it.toDomain() }) // Emite os dados locais caso os remotos falhem
    }.distinctUntilChanged() // 🔥 Evita reemissões repetitivas!
    override fun getFilteredClients(
        rota: String,
        cidade: String,
        empresa: String
    ): Flow<List<Client>> = flow {
        try {
            emit(clientDao.getFilteredClients(rota, cidade, empresa)
                .map { list -> list.map { it.toDomain() } }
                .firstOrNull() ?: emptyList()
            )
        } catch (e: Exception) {
            Log.e("ClientRepositoryImpl", "Erro ao filtrar clientes: ${e.message}", e)
        }
    }

    override suspend fun syncClients() {
        try {
            val remoteClients = clientApi.getClients().firstOrNull() ?: emptyList()
            clientDao.insertClients(remoteClients.map { it.toEntity() }) // Insere no banco local
            Log.d("ClientRepositoryImpl", "Clientes sincronizados com sucesso.")
        } catch (e: Exception) {
            Log.e("ClientRepositoryImpl", "Erro ao sincronizar clientes: ${e.message}", e)
        }
    }

    override suspend fun addClient(client: Client) {
        try {
            clientDao.insertClient(client.toEntity()) // Adiciona localmente
            clientApi.addClient(client.toDto()) // Sincroniza com Firebase
            Log.d("ClientRepositoryImpl", "Cliente adicionado com sucesso: $client")
        } catch (e: Exception) {
            Log.e("ClientRepositoryImpl", "Erro ao adicionar cliente: ${e.message}", e)
        }
    }

    override suspend fun updateClient(client: Client) {
        try {
            clientDao.updateClient(client.toEntity()) // Atualiza localmente
            clientApi.updateClient(client.toDto()) // Sincroniza com Firebase
            Log.d("ClientRepositoryImpl", "Cliente atualizado com sucesso: $client")
        } catch (e: Exception) {
            Log.e("ClientRepositoryImpl", "Erro ao atualizar cliente: ${e.message}", e)
        }
    }

    override suspend fun getClientById(clientId: Int): Client? {
        return try {
            clientDao.getClientById(clientId)?.toDomain()
        } catch (e: Exception) {
            Log.e("ClientRepositoryImpl", "Erro ao buscar cliente por ID: ${e.message}", e)
            null
        }
    }

}