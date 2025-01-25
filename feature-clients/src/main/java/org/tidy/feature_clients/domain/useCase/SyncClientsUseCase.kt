package org.tidy.feature_clients.domain.useCase

import org.tidy.feature_clients.domain.repositories.ClientRepository

class SyncClientsUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke() {
        repository.syncClients()
    }
}