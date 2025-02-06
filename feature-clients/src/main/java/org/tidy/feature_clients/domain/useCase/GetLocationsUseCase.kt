package org.tidy.feature_clients.domain.useCase

import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.domain.repositories.LocationRepository

class GetLocationsUseCase(private  val locationRepository: LocationRepository) {
    suspend operator fun invoke(): List<LocationDto> {
        return locationRepository.getLocations()
    }
}