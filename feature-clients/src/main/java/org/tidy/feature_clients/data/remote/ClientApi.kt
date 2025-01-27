package org.tidy.feature_clients.data.remote

import kotlinx.coroutines.flow.Flow
import org.tidy.core.domain.ClientError
import org.tidy.core.domain.Result

interface ClientApi {
    fun getClients(): Flow<List<ClientDto>>
    suspend fun addClient(client: ClientDto): Result<Unit, ClientError>
    suspend fun updateClient(client: ClientDto): Result<Unit, ClientError>
    suspend fun getClientById(clientId: Int): Result<ClientDto, ClientError>
}