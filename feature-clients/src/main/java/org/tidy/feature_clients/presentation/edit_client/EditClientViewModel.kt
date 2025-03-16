package org.tidy.feature_clients.presentation.edit_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.model.Localization
import org.tidy.feature_clients.domain.repository.ClientRepository

class EditClientViewModel(
    private val repository: ClientRepository // Injetado via DI
) : ViewModel() {

    private val _state = MutableStateFlow(EditClientState())
    val state: StateFlow<EditClientState> = _state

    fun onAction(action: EditClientAction) {
        when (action) {
            is EditClientAction.LoadClient -> {
                viewModelScope.launch {
                    repository.getClientDetails(action.clientId.toLong())?.let { client ->
                        val localization = Localization(
                            latitude = client.latitude ?: 0.0,
                            longitude = client.longitude ?: 0.0
                        )
                        _state.value = EditClientState(
                            id = client.id,
                            razaoSocial = client.razaoSocial.toString(),
                            nomeFantasia = client.nomeFantasia.toString(),
                            cnpj = client.cnpj.toString(),
                            estado = client.estado.toString(),
                            cidade = client.cidade.toString(),
                            rota = client.rota.toString(),
                            localizacao = localization, // ou formate conforme necessário
                            empresasTrabalhadas = client.empresasTrabalhadas ?: emptyList()
                        )
                    }
                }
            }

            is EditClientAction.OnRazaoSocialChange ->
                _state.value = _state.value.copy(razaoSocial = action.value)

            is EditClientAction.OnNomeFantasiaChange ->
                _state.value = _state.value.copy(nomeFantasia = action.value)

            is EditClientAction.OnCnpjChange ->
                _state.value = _state.value.copy(cnpj = action.value)

            is EditClientAction.OnEstadoChange ->
                _state.value = _state.value.copy(estado = action.value)

            is EditClientAction.OnCidadeChange ->
                _state.value = _state.value.copy(cidade = action.value)

            is EditClientAction.OnRotaChange ->
                _state.value = _state.value.copy(rota = action.value)

            is EditClientAction.OnLocalizacaoChange ->
                _state.value = _state.value.copy(localizacao = action.value)

            is EditClientAction.OnEmpresasTrabalhadasChange ->
                _state.value = _state.value.copy(empresasTrabalhadas = action.empresas)

            EditClientAction.SaveClient -> saveClient()
            EditClientAction.SyncClient -> {
                viewModelScope.launch {
                    repository.syncClients()
                    onAction(EditClientAction.LoadClient(_state.value.id.toString()))
                }
            }
        }
    }

    private fun saveClient() {
        viewModelScope.launch {
            try {
                val client = Client(
                    id = _state.value.id,
                    codigoDitrator = "",
                    codigoCasaDosRolamentos = "",
                    codigoRomarMann = "",
                    nomeFantasia = _state.value.nomeFantasia,
                    razaoSocial = _state.value.razaoSocial,
                    rota = _state.value.rota,
                    cidade = _state.value.cidade,
                    estado = _state.value.estado,
                    empresasTrabalhadas = _state.value.empresasTrabalhadas,
                    longitude = _state.value.localizacao?.longitude,
                    latitude = _state.value.localizacao?.latitude,
                    cnpj = _state.value.cnpj
                )
                repository.updateClient(client)
                _state.value = _state.value.copy(successMessage = "Cliente atualizado com sucesso")
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = "Erro ao atualizar cliente")
            }
        }
    }
}
