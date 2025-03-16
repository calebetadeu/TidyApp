package org.tidy.feature_clients.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClientApiService {
    @GET("/api/clients/filter")
    suspend fun filterClients(
        @Query("razao_social") razaoSocial: String? = null,
        @Query("estado") estado: String? = null,
        @Query("cidade") cidade: String? = null
    ): List<ClientDto>

    @GET("/api/clients/locations")
    suspend fun getLocations(): List<LocationDto>

    @GET("/api/clients/")
    suspend fun getClients(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<ClientDto>

    @POST("/api/clients/")
    suspend fun createClient(
        @Body client: ClientDto
    ): ClientDto

    @GET("/api/clients/{client_id}")
    suspend fun getClientDetails(
        @Path("client_id") clientId: Long
    ): ClientDto

    @PUT("/api/clients/{client_id}")
    suspend fun updateClient(
        @Path("client_id") clientId: Long,
        @Body client: ClientDto
    ): ClientDto

    @DELETE("/api/clients/{client_id}")
    suspend fun deleteClient(
        @Path("client_id") clientId: Long
    ): ClientDto

}