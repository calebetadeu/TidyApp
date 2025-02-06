package org.tidy.feature_clients.data.repostiory

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.domain.repositories.LocationRepository

class LocationRepositoryImpl : LocationRepository{
    override suspend fun getLocations(): List<LocationDto> {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("locations")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val dto = doc.toObject(LocationDto::class.java)
            dto?.apply {
                estado = doc.id
                listaCidades = doc.get("cidades") as? List<String> ?: emptyList()
            }
        }
    }

}