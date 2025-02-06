package org.tidy.feature_clients.presentation.clients_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.model.ClientFilters
import org.tidy.feature_clients.domain.useCase.GetClientsPagingUseCase
import org.tidy.feature_clients.domain.useCase.GetClientsUseCase
import org.tidy.feature_clients.domain.useCase.GetLocationsUseCase
import org.tidy.feature_clients.domain.useCase.SyncClientsUseCase

class ClientListViewModel(
    private val getClientsUseCase: GetClientsUseCase,
    private val getClientsPagingUseCase: GetClientsPagingUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val syncClientsUseCase: SyncClientsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ClientListState())
    val state: StateFlow<ClientListState> = _state.asStateFlow()
    private val _clientFilters = MutableStateFlow(ClientFilters())
    val clientFilters = _clientFilters.asStateFlow()
    private var allClients: List<Client> = emptyList() // 🔹 Armazena todos os clientes carregados
    private val _locations = mutableStateOf<List<LocationDto>>(emptyList())
    val locations get() = _locations.value


    init {
        loadClients()
        getLocations()

    }
    fun getLocations() {
        viewModelScope.launch {
            _locations.value = getLocationsUseCase()
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val clientsPagingFlow: Flow<PagingData<Client>> = _clientFilters
        .flatMapLatest { filters ->
            getClientsPagingUseCase(filters)
        }
        .cachedIn(viewModelScope)

    fun onAction(action: ClientListAction) {
        when (action) {
            is ClientListAction.LoadClients -> loadClients(action.forceRefresh)
            is ClientListAction.SyncClients -> syncClients()
            is ClientListAction.FilterClients -> applyFilters(
                action.estado,
                action.cidade,
                action.razaoSocial
            )
        }
    }

    private fun loadClients(forceRefresh: Boolean = false) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getClientsUseCase()
                .collect { clients ->
                    allClients = clients // 🔹 Mantém a lista original para filtros
                    _state.update { it.copy(clients = clients, isLoading = false) }
                }
        }
    }

    private fun syncClients() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            syncClientsUseCase()
            _clientFilters.update { currentFilters ->
                currentFilters.copy() // Trigger refresh
            }
            loadClients(forceRefresh = true)
        }
    }
    private fun applyFilters(estado: String, cidade: String, razaoSocial: String) {
        _clientFilters.update { currentFilters ->
            currentFilters.copy(
                estado = estado,
                cidade = cidade,
                razaoSocial = razaoSocial
            )
        }
    }

}