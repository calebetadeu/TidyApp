package org.tidy.feature_clients.presentation.register_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.useCase.AddClientUseCase

class RegisterClientViewModel(
    private val addClientUseCase: AddClientUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterClientState())
    val state: StateFlow<RegisterClientState> = _state.asStateFlow()

    fun onAction(action: RegisterClientAction) {
        when (action) {
            is RegisterClientAction.OnRazaoSocialChange -> _state.update { it.copy(razaoSocial = action.value) }
            is RegisterClientAction.OnNomeFantasiaChange -> _state.update { it.copy(nomeFantasia = action.value) }
            is RegisterClientAction.OnCnpjChange -> _state.update { it.copy(cnpj = action.value) }
            is RegisterClientAction.OnCidadeChange -> _state.update { it.copy(cidade = action.value) }
            is RegisterClientAction.OnEstadoChange -> _state.update { it.copy(estado = action.value) }
            is RegisterClientAction.OnRotaChange -> _state.update { it.copy(rota = action.value) } // 🚀 Novo campo
            is RegisterClientAction.OnLatitudeChange -> _state.update { it.copy(latitude = action.value) } // 🚀 Novo campo
            is RegisterClientAction.OnLongitudeChange -> _state.update { it.copy(longitude = action.value) } // 🚀 Novo campo
            is RegisterClientAction.OnRegisterClick -> registerClient()
        }
    }

    private fun registerClient() {
        val client = Client(
            codigoTidy = 0, // ID gerado automaticamente
            razaoSocial = state.value.razaoSocial,
            nomeFantasia = state.value.nomeFantasia,
            cnpj = state.value.cnpj,
            cidade = state.value.cidade,
            estado = state.value.estado,
            rota = state.value.rota, // 🚀 Corrigido!
            latitude = 0.0, // 🚀 Se for nulo, usa 0.0
            longitude = 0.0, // 🚀 Se for nulo, usa 0.0
            empresasTrabalhadas = emptyList()
        )

        viewModelScope.launch {
            addClientUseCase(client)
        }
    }
}