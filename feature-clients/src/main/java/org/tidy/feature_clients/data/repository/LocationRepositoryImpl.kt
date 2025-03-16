package org.tidy.feature_clients.data.repository

import org.tidy.feature_clients.core.Result
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.data.remote.RetrofitClientDataSource
import org.tidy.feature_clients.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val remoteDataSource: RetrofitClientDataSource
) : LocationRepository {
    override suspend fun getLocations(): List<LocationDto> {
        val result = remoteDataSource.getLocations()
        return if (result is Result.Success) {
            result.data
        } else {
            emptyList() // Em caso de erro, retorna uma lista vazia ou implemente tratamento específico
        }
    }
}