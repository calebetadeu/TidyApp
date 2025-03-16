package org.tidy.feature_clients.domain.repository

import org.tidy.feature_clients.domain.model.Client

interface ClientRepository {
    suspend fun getClients(skip: Int = 0, limit: Int = 100): List<Client>
    suspend fun filterClients(razaoSocial: String?, estado: String?, cidade: String?): List<Client>
    suspend fun createClient(client: Client): Client
    suspend fun getClientDetails(clientId: Long): Client?
    suspend fun updateClient(client: Client)
    suspend fun deleteClient(clientId: Long)
    suspend fun getLocations(): List<org.tidy.feature_clients.data.remote.LocationDto>
    suspend fun syncClients()
}