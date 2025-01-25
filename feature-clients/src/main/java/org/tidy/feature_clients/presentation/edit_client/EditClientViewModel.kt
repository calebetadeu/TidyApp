import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tidy.feature_clients.domain.useCase.GetClientByIdUseCase
import org.tidy.feature_clients.domain.useCase.UpdateClientUseCase
import org.tidy.feature_clients.presentation.edit_client.EditClientAction
import org.tidy.feature_clients.presentation.edit_client.EditClientState

class EditClientViewModel(
    private val getClientByIdUseCase: GetClientByIdUseCase,
    private val updateClientUseCase: UpdateClientUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditClientState())
    val state: StateFlow<EditClientState> = _state.asStateFlow()

    fun onAction(action: EditClientAction) {
        when (action) {
            is EditClientAction.LoadClient -> {
                viewModelScope.launch {
                    val client = getClientByIdUseCase(action.clientId)
                    if (client != null) {
                        _state.update {
                            it.copy(
                                client = client,
                                razaoSocial = client.razaoSocial,
                                nomeFantasia = client.nomeFantasia ?: "",
                                cnpj = client.cnpj?: "",
                                latitude = client.latitude,
                                longitude = client.longitude
                            )
                        }
                    }
                }
            }
            is EditClientAction.OnRazaoSocialChange -> _state.update { it.copy(razaoSocial = action.value) }
            is EditClientAction.OnNomeFantasiaChange -> _state.update { it.copy(nomeFantasia = action.value) }
            is EditClientAction.OnCnpjChange -> _state.update { it.copy(cnpj = action.value) }
            is EditClientAction.OnLatitudeChange -> _state.update { it.copy(latitude = action.value) }
            is EditClientAction.OnLongitudeChange -> _state.update { it.copy(longitude = action.value) }
            EditClientAction.SaveClient -> saveClient()
            EditClientAction.SyncClient -> syncClient()
        }
    }

    private fun saveClient() {
        viewModelScope.launch {
            state.value.client?.let { client ->
                val updatedClient = client.copy(
                    razaoSocial = state.value.razaoSocial,
                    nomeFantasia = state.value.nomeFantasia,
                    cnpj = state.value.cnpj,
                    latitude = state.value.latitude,
                    longitude = state.value.longitude
                )
                updateClientUseCase(updatedClient) // 🔥 Atualiza no banco local e Firebase
            }
        }
    }

    private fun syncClient() {
        viewModelScope.launch {
            state.value.client?.let { client ->
                updateClientUseCase(client) // 🔥 Garante que a sincronização acontece
            }
        }
    }
}