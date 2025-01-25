package org.tidy.feature_clients.data.repostiory


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.tidy.feature_clients.data.remote.ClientApi
import org.tidy.feature_clients.data.remote.ClientDto

class ClientApiImpl(
    val database: FirebaseDatabase
) : ClientApi {

    private val clientsRef: DatabaseReference = database.getReference("clients")

    override fun getClients(): Flow<List<ClientDto>> = callbackFlow {
        val listener = clientsRef.addValueEventListener(
            object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val clients = snapshot.children.mapNotNull { it.getValue(ClientDto::class.java) }
                    trySend(clients).isSuccess
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    close(error.toException())
                }
            }
        )

        awaitClose { clientsRef.removeEventListener(listener) }
    }

    override suspend fun addClient(client: ClientDto) {
        val newClientRef = clientsRef.push() // Cria um novo nó para o cliente no Firebase
        newClientRef.setValue(client).await()
    }

    override suspend fun updateClient(client: ClientDto) {
        try {
            val clientQuery = clientsRef.orderByChild("Codigo Tidy").equalTo(client.codigoTidy.toDouble()).get().await()

            if (clientQuery.exists()) {
                for (child in clientQuery.children) {
                    child.ref.setValue(client).await()
                }
            } else {
                throw Exception("Cliente não encontrado para atualização")
            }
        } catch (e: Exception) {
            throw Exception("Erro ao atualizar cliente no Firebase: ${e.message}")
        }
    }
}