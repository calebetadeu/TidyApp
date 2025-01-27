package org.tidy.feature_clients.domain.useCase

import org.tidy.core.domain.Result
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repositories.ClientRepository

class GetClientByIdUseCase(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(clientId: Int): Client? {
        // 🔥 Tenta buscar localmente primeiro
        val localClient = clientRepository.getClientById(clientId)
        if (localClient != null) return localClient

        // 🔥 Busca no Firestore e trata o resultado
        return when (val remoteClientResult = clientRepository.getClientByIdRemote(clientId)) {
            is org.tidy.core.domain.Result.Success -> {
                val remoteClient = remoteClientResult.data
                clientRepository.updateClient(remoteClient) // 🔥 Atualiza localmente
                remoteClient
            }
            is Result.Error -> {
                println("Erro ao buscar cliente remoto: ${remoteClientResult.error}")
                null // Retorna `null` se falhar
            }
        }
    }
}