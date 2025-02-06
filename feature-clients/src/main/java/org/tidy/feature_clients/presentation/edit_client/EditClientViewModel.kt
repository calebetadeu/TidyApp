package org.tidy.feature_clients.presentation.edit_client

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.domain.useCase.GetClientByIdUseCase
import org.tidy.feature_clients.domain.useCase.GetLocationsUseCase
import org.tidy.feature_clients.domain.useCase.UpdateClientUseCase

class EditClientViewModel(
    private val getClientByIdUseCase: GetClientByIdUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val updateClientUseCase: UpdateClientUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EditClientState())
    val state: StateFlow<EditClientState> = _state.asStateFlow()

    private val _locations = mutableStateOf<List<LocationDto>>(emptyList())
    val locations get() = _locations.value

    fun getLocations() {
        viewModelScope.launch {
            _locations.value = getLocationsUseCase()
        }
    }

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
                                cnpj = client.cnpj ?: "",
//                                localizacao = if (client.latitude != null && client.longitude != null) {
//                                    "${client.latitude}, ${client.longitude}"
//                                } else {
//                                    "Localização não definida"
//                                }, // 🔥 Atualizando Localização
                                cidade = client.cidade?:"",
                                estado = client.estado,
                                rota = client.rota?:"",
                                empresasTrabalhadas = client.empresasTrabalhadas
                            )
                        }
                    }
                }
            }

            is EditClientAction.OnRazaoSocialChange -> _state.update { it.copy(razaoSocial = action.value) }
            is EditClientAction.OnNomeFantasiaChange -> _state.update { it.copy(nomeFantasia = action.value) }
            is EditClientAction.OnCnpjChange -> _state.update { it.copy(cnpj = action.value) }
            is EditClientAction.OnLocalizacaoChange -> _state.update { it.copy(localizacao = action.value) } // 🔥 Atualizando Localização
            is EditClientAction.OnCidadeChange -> _state.update { it.copy(cidade = action.value) }
            is EditClientAction.OnEstadoChange -> _state.update { it.copy(estado = action.value) }
            is EditClientAction.OnRotaChange -> _state.update { it.copy(rota = action.value) }
            is EditClientAction.OnEmpresasTrabalhadasChange -> _state.update {
                it.copy(
                    empresasTrabalhadas = action.empresas
                )
            } // 🔥 Atualizando Empresas
            EditClientAction.SaveClient -> saveClient()
            EditClientAction.SyncClient -> syncClient()
        }
    }

    private fun saveClient() {
        viewModelScope.launch {
            state.value.client?.let { client ->
                val updatedClient = client.copy(
                    razaoSocial = state.value.razaoSocial,
                    nomeFantasia = if (state.value.nomeFantasia.isNotEmpty()) state.value.nomeFantasia else client.nomeFantasia,
                    cnpj = if (state.value.cnpj.isNotEmpty()) state.value.cnpj else client.cnpj,
//                    latitude = if (state.value.localizacao.isNotEmpty()) parseLatLong(state.value.localizacao).first else client.latitude,
//                    longitude = if (state.value.localizacao.isNotEmpty()) parseLatLong(state.value.localizacao).second else client.longitude,
                    cidade = state.value.cidade,
                    estado = state.value.estado,
                    rota = state.value.rota,
                    empresasTrabalhadas = state.value.empresasTrabalhadas
                )

                updateClientUseCase(updatedClient) // 🔥 Atualiza no banco local e Firestore

                // 🔥 **Força o recarregamento do cliente após a atualização**
//                val reloadedClient = getClientByIdUseCase(client.id.toString())
//                if (reloadedClient != null) {
//                    _state.update {
//                        it.copy(
//                            client = reloadedClient,
//                            razaoSocial = reloadedClient.razaoSocial,
//                            nomeFantasia = reloadedClient.nomeFantasia ?: "",
//                            cnpj = reloadedClient.cnpj ?: "",
////                            localizacao = if (reloadedClient.latitude != null && reloadedClient.longitude != null) {
////                                "${reloadedClient.latitude}, ${reloadedClient.longitude}"
////                            } else {
////                                "Localização não definida"
////                            },
//                            cidade = reloadedClient.cidade?:"",
//                            estado = reloadedClient.estado,
//                            rota = reloadedClient.rota?:"",
//                            empresasTrabalhadas = reloadedClient.empresasTrabalhadas
//                        )
//                    }
//                }
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

    // 🔥 **Função para converter localizacao para latitude e longitude**
    private fun parseLatLong(localizacao: String): Pair<Double?, Double?> {
        val parts = localizacao.split(", ")
        return if (parts.size == 2) {
            val lat = parts[0].toDoubleOrNull()?.takeIf { it != 0.0 }
            val lon = parts[1].toDoubleOrNull()?.takeIf { it != 0.0 }
            lat to lon
        } else {
            null to null
        }
    }
}