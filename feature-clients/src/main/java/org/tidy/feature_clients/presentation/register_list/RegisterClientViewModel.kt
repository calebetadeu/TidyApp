package org.tidy.feature_clients.presentation.register_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repository.ClientRepository

class RegisterClientViewModel(
    private val repository: ClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterClientState())
    val state: StateFlow<RegisterClientState> = _state

    fun onAction(action: RegisterClientAction) {
        when (action) {
            is RegisterClientAction.OnRazaoSocialChange ->
                _state.value = _state.value.copy(razaoSocial = action.value).also { validateForm() }
            is RegisterClientAction.OnNomeFantasiaChange ->
                _state.value = _state.value.copy(nomeFantasia = action.value).also { validateForm() }
            is RegisterClientAction.OnCnpjChange ->
                _state.value = _state.value.copy(cnpj = action.value).also { validateForm() }
            is RegisterClientAction.OnEstadoChange ->
                _state.value = _state.value.copy(estado = action.value).also { validateForm() }
            is RegisterClientAction.OnCidadeChange ->
                _state.value = _state.value.copy(cidade = action.value).also { validateForm() }
            is RegisterClientAction.OnRotaChange ->
                _state.value = _state.value.copy(rota = action.value).also { validateForm() }
            is RegisterClientAction.OnLocalizacaoChange ->
                _state.value = _state.value.copy(localizacao = action.value).also { validateForm() }
            is RegisterClientAction.OnEmpresasTrabalhadasChange -> {
                val current = _state.value.empresasTrabalhadas.toMutableList()
                if (action.isSelected) current.add(action.empresa) else current.remove(action.empresa)
                _state.value = _state.value.copy(empresasTrabalhadas = current)
            }
            RegisterClientAction.OnRegisterClick -> registerClient()
        }
    }

    private fun validateForm() {
        val current = _state.value
        val isValid = current.razaoSocial.isNotBlank() &&
                current.nomeFantasia.isNotBlank() &&
                current.cnpj.isNotBlank() &&
                current.estado.isNotBlank() &&
                current.cidade.isNotBlank() &&
                current.rota.isNotBlank()
        _state.value = current.copy(isFormValid = isValid)
    }

    private fun registerClient() {
        viewModelScope.launch {
            try {
                // Converte o estado atual para o modelo de domínio Client.
                // Note que campos como código podem ser definidos conforme sua regra.
                val client = Client(
                    id = 0L, // Novo cliente; o ID pode ser gerado no servidor
                    codigoDitrator = "",
                    codigoCasaDosRolamentos = "",
                    codigoRomarMann = "",
                    nomeFantasia = _state.value.nomeFantasia,
                    razaoSocial = _state.value.razaoSocial,
                    rota = _state.value.rota,
                    cidade = _state.value.cidade,
                    estado = _state.value.estado,
                    empresasTrabalhadas = _state.value.empresasTrabalhadas,
                    latitude = _state.value.localizacao.toDoubleOrNull() ?: 0.0,
                    longitude = 0.0,
                    cnpj = _state.value.cnpj
                )
                // Cria o cliente (o repositório implementa a estratégia off‑remote/remote‑off)
                repository.createClient(client)
                _state.value = _state.value.copy(successMessage = "Cliente registrado com sucesso")
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = "Erro ao registrar cliente")
            }
        }
    }
}