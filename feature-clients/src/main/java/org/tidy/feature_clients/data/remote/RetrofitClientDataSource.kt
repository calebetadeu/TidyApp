package org.tidy.feature_clients.data.remote

import org.tidy.core.domain.DataError
import org.tidy.feature_clients.core.Result
import org.tidy.feature_clients.core.safeCall

class RetrofitClientDataSource(
    private val apiService: ClientApiService
) {

    suspend fun getClients(skip: Int = 0, limit: Int = 100): Result<List<ClientDto>, DataError.Remote> {
        return safeCall { apiService.getClients(skip, limit) }
    }

    suspend fun filterClients(
        razaoSocial: String?,
        estado: String?,
        cidade: String?
    ): Result<List<ClientDto>, DataError.Remote> {
        return safeCall { apiService.filterClients(razaoSocial, estado, cidade) }
    }

    suspend fun getLocations(): Result<List<LocationDto>, DataError.Remote> {
        return safeCall { apiService.getLocations() }
    }

    suspend fun createClient(clientDto: ClientDto): Result<ClientDto, DataError.Remote> {
        return safeCall { apiService.createClient(clientDto) }
    }

    suspend fun getClientDetails(clientId: Long): Result<ClientDto, DataError.Remote> {
        return safeCall { apiService.getClientDetails(clientId) }
    }

    suspend fun updateClient(clientId: Long, clientDto: ClientDto): Result<ClientDto, DataError.Remote> {
        return safeCall { apiService.updateClient(clientId, clientDto) }
    }

    suspend fun deleteClient(clientId: Long): Result<ClientDto, DataError.Remote> {
        return safeCall { apiService.deleteClient(clientId) }
    }
}