package org.tidy.feature_clients.presentation.edit_client


sealed class EditClientAction {
    data class LoadClient(val clientId: Int) : EditClientAction()
    data class OnRazaoSocialChange(val value: String) : EditClientAction()
    data class OnNomeFantasiaChange(val value: String) : EditClientAction()
    data class OnCnpjChange(val value: String) : EditClientAction()
    data class OnLatitudeChange(val value: Double?) : EditClientAction()
    data class OnLongitudeChange(val value: Double?) : EditClientAction()
    object SaveClient : EditClientAction()
    object SyncClient : EditClientAction()
}