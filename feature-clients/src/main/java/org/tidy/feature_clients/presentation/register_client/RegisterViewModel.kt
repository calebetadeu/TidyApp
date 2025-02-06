package org.tidy.feature_clients.presentation.register_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tidy.core.domain.onError
import org.tidy.core.domain.onSuccess
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.useCase.AddClientUseCase

class RegisterClientViewModel(
    private val addClientUseCase: AddClientUseCase,
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
            is RegisterClientAction.OnRotaChange -> _state.update { it.copy(rota = action.value) }
            is RegisterClientAction.OnLocalizacaoChange -> _state.update { it.copy(localizacao = action.value) }
            is RegisterClientAction.OnEmpresasTrabalhadasChange -> updateEmpresas(
                action.empresa,
                action.isSelected
            )

            RegisterClientAction.OnRegisterClick -> registerClient()
        }
    }

    /**
     * 🔥 **Atualiza a lista de empresas trabalhadas**
     */
    private fun updateEmpresas(empresa: String, isSelected: Boolean) {
        _state.update {
            val empresas = it.empresasTrabalhadas.toMutableList()
            if (isSelected) empresas.add(empresa) else empresas.remove(empresa)
            it.copy(empresasTrabalhadas = empresas)
        }
    }

    /**
     * 🚀 **Realiza o cadastro do cliente**
     */
    private fun registerClient() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            val (latitude, longitude) = parseLatLong(state.value.localizacao)

            val client = Client(
                nomeFantasia = state.value.nomeFantasia.takeIf { it.isNotBlank() },
                razaoSocial = state.value.razaoSocial, // 🔥 Garante que não seja vazio
                rota = state.value.rota, // 🔥
                cnpj = state.value.cnpj.takeIf { it.isNotBlank() },
                cidade = state.value.cidade,
                estado = state.value.estado,
                empresasTrabalhadas = state.value.empresasTrabalhadas,
                latitude = latitude,
                longitude = longitude,
            )

            val result = addClientUseCase(client)

            result
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "✅ Cliente cadastrado com sucesso!",
                            errorMessage = null
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = null,
                            errorMessage = "❌ Erro ao cadastrar cliente: "
                        )
                    }
                }
        }
    }

    /**
     * 🔥 **Gera um código Tidy único baseado no timestamp**
     */
    private fun generateUniqueCodigoTidy(): Int {
        return (System.currentTimeMillis() % 90000 + 10000).toInt()
    }

    /**
     * 🔥 **Converte localização (string) para latitude e longitude**
     */
    private fun parseLatLong(localizacao: String): Pair<Double?, Double?> {
        val parts = localizacao.split(", ")
        return if (parts.size == 2) {
            val lat = parts[0].toDoubleOrNull()
            val lon = parts[1].toDoubleOrNull()
            lat to lon
        } else {
            null to null
        }
    }
}