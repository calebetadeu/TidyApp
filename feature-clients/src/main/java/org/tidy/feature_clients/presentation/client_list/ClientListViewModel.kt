package org.tidy.feature_clients.presentation.client_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repository.ClientRepository

class ClientListViewModel(
    private val repository: ClientRepository // Injetado via DI
) : ViewModel() {

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients

    init {
        loadClients()
    }

    fun loadClients(skip: Int = 0, limit: Int = 100) {
        viewModelScope.launch {
            _clients.value = repository.getClients(skip, limit)
        }
    }

    fun onAction(action: ClientListAction) {
        when (action) {
            is ClientListAction.FilterClients -> {
                viewModelScope.launch {
                    _clients.value = repository.filterClients(
                        razaoSocial = action.razaoSocial,
                        estado = action.estado,
                        cidade = action.cidade
                    )
                }
            }
            ClientListAction.SyncClients -> {
                viewModelScope.launch {
                    repository.syncClients()
                    loadClients()
                }
            }
        }
    }
}