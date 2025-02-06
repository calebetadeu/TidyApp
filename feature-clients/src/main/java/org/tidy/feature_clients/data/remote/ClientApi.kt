package org.tidy.feature_clients.data.remote

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.tidy.core.domain.ClientError
import org.tidy.core.domain.Result
import org.tidy.feature_clients.domain.model.ClientFilters

interface ClientApi {
    fun getClients(): Flow<List<ClientDto>>
    suspend fun addClient(client: ClientDto): Result<Unit, ClientError>
    suspend fun updateClient(client: ClientDto): Result<Unit, ClientError>
    suspend fun getClientById(clientId: String): Result<ClientDto, ClientError>
    fun getClientsPaging(filters: ClientFilters): Flow<PagingData<ClientDto>>
}