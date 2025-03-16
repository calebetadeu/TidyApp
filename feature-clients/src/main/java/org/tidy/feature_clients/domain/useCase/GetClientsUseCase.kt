package org.tidy.feature_clients.domain.useCase

import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repository.ClientRepository

class GetClientsUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(skip: Int = 0, limit: Int = 100): List<Client> {
        return repository.getClients(skip, limit)
    }
}