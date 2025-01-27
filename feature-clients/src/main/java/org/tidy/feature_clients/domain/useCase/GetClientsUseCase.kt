package org.tidy.feature_clients.domain.useCase

import kotlinx.coroutines.flow.Flow
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repositories.ClientRepository

class GetClientsUseCase(private val repository: ClientRepository) {
    operator fun invoke(): Flow<List<Client>> {
        return repository.getClients()
    }
}