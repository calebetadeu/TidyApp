package org.tidy.feature_clients.presentation.clients_list

import org.tidy.feature_clients.domain.model.Client

data class ClientListState(
    val clients: List<Client> = emptyList(),
    val rotaFilter: String = "",
    val cidadeFilter: String = "",
    val empresaFilter: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)