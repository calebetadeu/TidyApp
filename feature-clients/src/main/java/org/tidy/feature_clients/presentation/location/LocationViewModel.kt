package org.tidy.feature_clients.presentation.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.domain.repository.LocationRepository

class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _locations = MutableStateFlow<List<LocationDto>>(emptyList())
    val locations: StateFlow<List<LocationDto>> = _locations

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            _locations.value = repository.getLocations()
        }
    }
}