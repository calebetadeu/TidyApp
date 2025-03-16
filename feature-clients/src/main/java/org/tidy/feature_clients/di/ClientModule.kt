package org.tidy.feature_clients.di

import androidx.room.Room
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tidy.feature_clients.data.local.ClientDatabase
import org.tidy.feature_clients.data.remote.ClientApiService
import org.tidy.feature_clients.data.remote.RetrofitClientDataSource
import org.tidy.feature_clients.data.repository.ClientRepositoryImpl
import org.tidy.feature_clients.data.repository.LocationRepositoryImpl
import org.tidy.feature_clients.domain.repository.ClientRepository
import org.tidy.feature_clients.domain.repository.LocationRepository
import org.tidy.feature_clients.presentation.client_list.ClientListViewModel
import org.tidy.feature_clients.presentation.edit_client.EditClientViewModel
import org.tidy.feature_clients.presentation.location.LocationViewModel
import org.tidy.feature_clients.presentation.register_list.RegisterClientViewModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://tidy-api-b2e9a63fe70c.herokuapp.com/" // Altere para sua URL

val clientModule = module {

    // Retrofit
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Para exibir o corpo da resposta, use Level.BODY
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Configura o OkHttpClient com o interceptor de logging
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().build()
                )
            )
            .build()
    }
    single { get<Retrofit>().create(ClientApiService::class.java) }
    single { RetrofitClientDataSource(get()) }


    // Room Database
    single {
        Room.databaseBuilder(
            androidContext(),
            ClientDatabase::class.java,
            "client_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()

    }
    single { get<ClientDatabase>().clientDao() }

    // Repository
    single<ClientRepository> { ClientRepositoryImpl(get(), get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    viewModelOf(::RegisterClientViewModel)
    viewModelOf(::ClientListViewModel)
    viewModelOf(::LocationViewModel)
    viewModelOf(::EditClientViewModel)
}