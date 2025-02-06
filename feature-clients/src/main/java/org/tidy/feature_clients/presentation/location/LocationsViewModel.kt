package org.tidy.feature_clients.presentation.location

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.domain.useCase.GetLocationsUseCase

class LocationsViewModel(
    private val getLocationsUseCase: GetLocationsUseCase
) : ViewModel() {

    // Estado para armazenar a lista de locations
    private val _locations = mutableStateOf<List<LocationDto>>(emptyList())
    val locations get() = _locations.value

    // Estado para indicar se está carregando as locations (opcional)
    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading.value

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            _isLoading.value = true
            // Chama o use case para buscar as locations
            val result = getLocationsUseCase()
            _locations.value = result
            _isLoading.value = false
        }
    }
}