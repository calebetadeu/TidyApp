package org.tidy.feature_clients.data.local

import ClientEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE rota LIKE :rota AND cidade LIKE :cidade AND empresasTrabalhadas LIKE :empresa")
    fun getFilteredClients(rota: String, cidade: String, empresa: String): Flow<List<ClientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClients(clients: List<ClientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientEntity)

    @Update
    suspend fun updateClient(client: ClientEntity) // 🚀 Adi


    @Query("SELECT * FROM clients WHERE codigoTidy = :clientId LIMIT 1")
    suspend fun getClientById(clientId: Int): ClientEntity?

}