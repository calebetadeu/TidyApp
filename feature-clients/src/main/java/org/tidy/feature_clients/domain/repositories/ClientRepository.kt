package org.tidy.feature_clients.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.tidy.core.domain.ClientError
import org.tidy.core.domain.Result
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.model.ClientFilters

interface
ClientRepository {
    fun getClientsPaging(
        filters: ClientFilters
    ): Flow<PagingData<Client>>
    fun getClients(): Flow<List<Client>>
    fun getFilteredClients(rota: String, cidade: String, empresa: String): Flow<List<Client>>
    suspend fun syncClients()
    suspend fun addClient(client: Client): Result<Unit, ClientError> // 🔥 Atualizado
    suspend fun updateClient(client: Client): Result<Unit, ClientError> // 🔥 Atualizado
    suspend fun getClientById(clientId: String): Client?
    suspend fun getClientByIdRemote(clientId: String): Result<Client, ClientError> // 🔥 Busca segura no Firestore
}