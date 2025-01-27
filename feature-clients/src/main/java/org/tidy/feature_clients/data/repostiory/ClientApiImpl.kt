package org.tidy.feature_clients.data.repostiory


import kotlinx.coroutines.flow.Flow
import org.tidy.core.domain.ClientError
import org.tidy.feature_clients.data.remote.ClientApi
import org.tidy.feature_clients.data.remote.ClientDto
import org.tidy.feature_clients.data.remote.FirebaseClientServiceImpl
import org.tidy.core.domain.Result

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

    override suspend fun getClientById(clientId: Int): Result<ClientDto, ClientError> {
        return firebaseService.getClientById(clientId) // 🔥 Busca cliente no Firestore com Result
    }
}
//ClientApiImpl