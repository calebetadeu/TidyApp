package org.tidy.feature_clients.data.repostiory


import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.tidy.core.domain.ClientError
import org.tidy.core.domain.Result
import org.tidy.feature_clients.data.remote.ClientApi
import org.tidy.feature_clients.data.remote.ClientDto
import org.tidy.feature_clients.data.remote.FirebaseClientServiceImpl
import org.tidy.feature_clients.domain.model.ClientFilters

class ClientApiImpl(
    private val firebaseService: FirebaseClientServiceImpl // 🔥 Usa o serviço do Firebase
) : ClientApi {

    override fun getClients(): Flow<List<ClientDto>> {
        return firebaseService.getClients() // 🔥 Busca clientes do Firestore
    }

    override suspend fun addClient(client: ClientDto): Result<Unit, ClientError> {
        return firebaseService.addClient(client) // 🔥 Salva o cliente no Firestore com Result
    }

    override suspend fun updateClient(client: ClientDto): Result<Unit, ClientError> {
        return firebaseService.updateClient(client) // 🔥 Atualiza o cliente no Firestore com Result
    }

    override suspend fun getClientById(clientId: String): Result<ClientDto, ClientError> {
        return firebaseService.getClientById(clientId) // 🔥 Busca cliente no Firestore com Result
    }
    override fun getClientsPaging(filters: ClientFilters): Flow<PagingData<ClientDto>> {
        return firebaseService.getClientsPaging(filters)
    }
}
//ClientApiImpl