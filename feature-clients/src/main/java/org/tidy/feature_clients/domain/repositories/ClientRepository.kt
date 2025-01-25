package org.tidy.feature_clients.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.tidy.feature_clients.domain.model.Client

interface ClientRepository {
    fun getClients(): Flow<List<Client>>
    fun getFilteredClients(rota: String, cidade: String, empresa: String): Flow<List<Client>>
    suspend fun syncClients()
    suspend fun addClient(client: Client)
    suspend fun updateClient(client: Client)
    suspend fun getClientById(clientId: Int): Client? // 🔥 Nova função


}