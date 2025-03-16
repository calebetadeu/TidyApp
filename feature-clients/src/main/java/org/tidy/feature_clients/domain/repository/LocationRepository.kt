package org.tidy.feature_clients.domain.repository

import org.tidy.feature_clients.data.remote.LocationDto

interface LocationRepository {
    suspend fun getLocations(): List<LocationDto>
}