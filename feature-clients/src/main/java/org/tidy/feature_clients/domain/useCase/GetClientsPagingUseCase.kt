package org.tidy.feature_clients.domain.useCase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.domain.model.ClientFilters
import org.tidy.feature_clients.domain.repositories.ClientRepository

// Exemplo de use case para obter clientes paginados
class GetClientsPagingUseCase(private val clientRepository: ClientRepository) {
    operator fun invoke(filters: ClientFilters): Flow<PagingData<Client>> {
        return clientRepository.getClientsPaging(filters)
    }
}