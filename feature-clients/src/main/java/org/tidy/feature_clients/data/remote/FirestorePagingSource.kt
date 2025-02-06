package org.tidy.feature_clients.data.remote


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestorePagingSource(
    private val query: Query
) : PagingSource<DocumentSnapshot, ClientDto>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, ClientDto> {
        return try {
            val currentQuery = if (params.key != null) {
                query.startAfter(params.key!!.id)
            } else {
                query
            }

            Log.d("FirestorePaging", "Carregando com loadSize: ${params.loadSize} e key: ${params.key?.id}")
            val snapshot = currentQuery.limit(params.loadSize.toLong()).get().await()
            Log.d("FirestorePaging", "Snapshot retornou ${snapshot.size()} documentos.")

            val lastVisible = snapshot.documents.lastOrNull()
            val clients = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(ClientDto::class.java)?.copy(id = doc.id)
                } catch (e: Exception) {
                    Log.e("FirestorePaging", "Erro ao converter documento: ${e.message}")
                    null
                }
            }
            Log.d("FirestorePaging", "Total de clientes convertidos: ${clients.size}")

            LoadResult.Page(
                data = clients,
                prevKey = null,
                nextKey = if (snapshot.size() < params.loadSize) null else lastVisible
            )
        } catch (e: Exception) {
            Log.e("FirestorePaging", "Erro no load: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, ClientDto>): DocumentSnapshot? {
        return state.pages.firstOrNull()?.prevKey
    }
}