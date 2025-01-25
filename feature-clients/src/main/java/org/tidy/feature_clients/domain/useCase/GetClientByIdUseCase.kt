package org.tidy.feature_clients.domain.useCase

import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repositories.ClientRepository

class GetClientByIdUseCase(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(clientId: Int): Client? {
        return repository.getClientById(clientId)
    }
}