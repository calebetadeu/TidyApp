package org.tidy.feature_clients.presentation.clients_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.useCase.GetClientsUseCase
import org.tidy.feature_clients.domain.useCase.GetFilteredClientsUseCase
import org.tidy.feature_clients.domain.useCase.SyncClientsUseCase
import kotlin.collections.filter

class ClientListViewModel(
    private val getClientsUseCase: GetClientsUseCase,
    private val getFilteredClientsUseCase: GetFilteredClientsUseCase,
    private val syncClientsUseCase: SyncClientsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ClientListState())
    val state: StateFlow<ClientListState> = _state.asStateFlow()

    private var allClients: List<Client> = emptyList() // 🔹 Armazena todos os clientes carregados

    init {
        loadClients()
    }

    fun onAction(action: ClientListAction) {
        when (action) {
            is ClientListAction.LoadClients -> loadClients(action.forceRefresh)
            is ClientListAction.SyncClients -> syncClients()
            is ClientListAction.FilterClients -> applyFilters(action.rota, action.cidade, action.empresa)
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
            loadClients(forceRefresh = true)
        }
    }

    private fun applyFilters(rota: String, cidade: String, empresa: String) {
        _state.update { it.copy(rotaFilter = rota, cidadeFilter = cidade, empresaFilter = empresa) }

        viewModelScope.launch {
            val filteredClients = allClients.filter { client ->
                (rota.isEmpty() || client.rota?.contains(rota, ignoreCase = true) == true) &&
                        (cidade.isEmpty() || client.cidade?.contains(cidade, ignoreCase = true) == true) &&
                        (empresa.isEmpty() || client.empresasTrabalhadas.any { it.contains(empresa, ignoreCase = true) })
            }

            _state.update { it.copy(clients = filteredClients) }
        }
    }
}