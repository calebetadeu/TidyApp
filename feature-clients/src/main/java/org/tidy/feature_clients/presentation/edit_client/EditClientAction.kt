package org.tidy.feature_clients.presentation.edit_client

import org.tidy.feature_clients.domain.model.Localization

sealed class EditClientAction {
    data class LoadClient(val clientId: String) : EditClientAction()
    data class OnRazaoSocialChange(val value: String) : EditClientAction()
    data class OnNomeFantasiaChange(val value: String) : EditClientAction()
    data class OnCnpjChange(val value: String) : EditClientAction()
    data class OnEstadoChange(val value: String) : EditClientAction()
    data class OnCidadeChange(val value: String) : EditClientAction()
    data class OnRotaChange(val value: String) : EditClientAction()
    data class OnLocalizacaoChange(val value: Localization) : EditClientAction()
    data class OnEmpresasTrabalhadasChange(val empresas: List<String>) : EditClientAction()
    object SaveClient : EditClientAction()
    object SyncClient : EditClientAction()
}