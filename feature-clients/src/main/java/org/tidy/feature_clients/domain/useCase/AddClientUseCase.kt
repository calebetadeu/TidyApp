package org.tidy.feature_clients.domain.useCase

import org.tidy.core.domain.ClientError
import org.tidy.core.domain.Result
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.repositories.ClientRepository

class AddClientUseCase(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(client: Client): Result<Unit, ClientError> {
        return repository.addClient(client)
    }
}