package org.tidy.feature_clients.presentation.clients_list

sealed class ClientListAction {
    data class LoadClients(val forceRefresh: Boolean = false) : ClientListAction()
    object SyncClients : ClientListAction()
    data class FilterClients(val rota: String, val cidade: String, val empresa: String) : ClientListAction()
}