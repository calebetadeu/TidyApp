package org.tidy.feature_clients.data.remote

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseClientService : ClientApi {

    private val database =
        FirebaseDatabase.getInstance("https://tidy-50d22-default-rtdb.firebaseio.com/")
    private val clientsRef = database.getReference("clients")

    override fun getClients(): Flow<List<ClientDto>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val clients = snapshot.children.mapNotNull { child ->
                    try {
                        child.getValue(ClientDto::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
                trySend(clients).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        clientsRef.addValueEventListener(listener)
        awaitClose { clientsRef.removeEventListener(listener) }
    }

    override suspend fun addClient(client: ClientDto) {
        clientsRef.child(client.codigoTidy.toString()).setValue(client).await()
    }

    override suspend fun updateClient(client: ClientDto) {
        try {
            clientsRef.child(client.codigoTidy.toString()).updateChildren(client.toMap()).await()
            Log.d("FirebaseClientService", "Cliente atualizado com sucesso: $client")
        } catch (e: Exception) {
            Log.e("FirebaseClientService", "Erro ao atualizar cliente: ${e.message}", e)
        }
    }
}