package org.tidy.feature_clients.domain.useCase

import kotlinx.coroutines.flow.Flow
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repositories.ClientRepository

class GetFilteredClientsUseCase(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(rota: String, cidade: String, empresa: String): Flow<List<Client>> {
        return clientRepository.getFilteredClients(rota, cidade, empresa)
    }
}