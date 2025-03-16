package org.tidy.feature_clients.presentation.client_list

sealed class ClientListAction {
    data class FilterClients(val estado: String, val cidade: String, val razaoSocial: String) : ClientListAction()
    object SyncClients : ClientListAction()
}