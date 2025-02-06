package org.tidy.feature_clients.domain.repositories

import org.tidy.feature_clients.data.remote.LocationDto


interface LocationRepository {
    suspend fun getLocations(): List<LocationDto>
}

