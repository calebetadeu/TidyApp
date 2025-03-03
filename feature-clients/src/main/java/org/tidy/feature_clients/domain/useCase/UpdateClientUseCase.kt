package org.tidy.feature_clients.domain.useCase


import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repositories.ClientRepository

class UpdateClientUseCase(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(client: Client) {
        repository.updateClient(client)
    }
}