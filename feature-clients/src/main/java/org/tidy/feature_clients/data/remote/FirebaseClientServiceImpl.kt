package org.tidy.feature_clients.data.remote


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.tidy.core.domain.ClientError
import org.tidy.core.domain.Result
class FirebaseClientServiceImpl : ClientApi {

    private val database = FirebaseFirestore.getInstance()
    private val clientsCollection = database.collection("clients") // 🔹 Referência ao Firestore

    override fun getClients(): Flow<List<ClientDto>> = callbackFlow {
        val listener = clientsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val clients = snapshot?.documents?.mapNotNull { doc ->
                try {
                    doc.toObject<ClientDto>()?.copy(
                        codigoTidy = doc.id.toIntOrNull() ?: 0 // 🔹 Garante conversão segura do ID
                    )
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            trySend(clients).isSuccess
        }

        awaitClose { listener.remove() }
    }

    override suspend fun addClient(client: ClientDto): Result<Unit, ClientError> {
        return try {
            clientsCollection.document(client.codigoTidy.toString()).set(client).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ClientError.DatabaseError(e.message ?: "Erro desconhecido ao adicionar cliente"))
        }
    }

    override suspend fun updateClient(client: ClientDto): Result<Unit, ClientError> {
        return try {
            val updateMap = mutableMapOf<String, Any>()

            updateMap["Codigo Tidy"] = client.codigoTidy
            updateMap["Razão Social"] = client.razaoSocial
            updateMap["Cidade"] = client.cidade ?: ""
            updateMap["Estado"] = client.estado
            updateMap["Rota"] = client.rota ?: "Sem Rota"
            updateMap["Empresas Trabalhadas"] = client.empresasTrabalhadas
            updateMap["CNPJ"] = client.cnpj ?: ""
            updateMap["Nome Fantasia"] = client.nomeFantasia ?: ""
            updateMap["Latitude"] = client.latitude ?: 0.0
            updateMap["Longitude"] = client.longitude ?: 0.0

            clientsCollection.document(client.codigoTidy.toString()).set(updateMap, SetOptions.merge()).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ClientError.DatabaseError(e.message ?: "Erro desconhecido ao atualizar cliente"))
        }
    }

    override suspend fun getClientById(clientId: Int): Result<ClientDto, ClientError> {
        return try {
            val document = clientsCollection.document(clientId.toString()).get().await()
            if (document.exists()) {
                document.toObject(ClientDto::class.java)?.copy(codigoTidy = clientId)
                    ?.let { Result.Success(it) } ?: Result.Error(ClientError.NotFound) // ✅ Agora não precisa de `()`
            } else {
                Result.Error(ClientError.NotFound) // ✅ Agora não precisa de `()`
            }
        } catch (e: Exception) {
            Result.Error(ClientError.DatabaseError(e.message ?: "Erro desconhecido ao atualizar cliente")) // ✅ Agora não precisa de `()`
        }
    }
}