package org.tidy.feature_clients.data.remote

import kotlinx.coroutines.flow.Flow

interface ClientApi {
    fun getClients(): Flow<List<ClientDto>>
    suspend fun addClient(client: ClientDto)
    suspend fun updateClient(client: ClientDto)

}