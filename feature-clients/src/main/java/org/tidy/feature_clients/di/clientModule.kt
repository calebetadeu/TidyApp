package org.tidy.feature_clients.di

import EditClientViewModel
import com.google.firebase.database.FirebaseDatabase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tidy.feature_clients.data.local.ClientDatabase
import org.tidy.feature_clients.data.remote.ClientApi
import org.tidy.feature_clients.data.remote.FirebaseClientService
import org.tidy.feature_clients.data.repostiory.ClientRepositoryImpl
import org.tidy.feature_clients.data.repostiory.ClientApiImpl
import org.tidy.feature_clients.domain.repositories.ClientRepository
import org.tidy.feature_clients.domain.useCase.AddClientUseCase
import org.tidy.feature_clients.domain.useCase.GetClientByIdUseCase
import org.tidy.feature_clients.domain.useCase.GetClientsUseCase
import org.tidy.feature_clients.domain.useCase.GetFilteredClientsUseCase
import org.tidy.feature_clients.domain.useCase.SyncClientsUseCase
import org.tidy.feature_clients.domain.useCase.UpdateClientUseCase
import org.tidy.feature_clients.presentation.clients_list.ClientListViewModel
import org.tidy.feature_clients.presentation.register_client.RegisterClientViewModel

val clientModule = module {
    single { FirebaseDatabase.getInstance() }

    single { ClientDatabase.getDatabase(get()) }
    single { get<ClientDatabase>().clientDao() }
    single<ClientApi> { ClientApiImpl(get()) }
    single { FirebaseClientService() }

    single<ClientRepository> {


        ClientRepositoryImpl(
            clientDao = get(),
            clientApi = get(),
        )
    }

    // Use Cases
    factory { GetClientsUseCase(get()) }
    factory { GetFilteredClientsUseCase(get()) }
    factory { SyncClientsUseCase(get()) }
    factory { AddClientUseCase(get()) }
    factory { GetClientByIdUseCase(get()) } // 🔥 Adicionando no Koin
    factory { UpdateClientUseCase(get()) }


    // ViewModels
    viewModelOf(::ClientListViewModel)
    viewModelOf(::RegisterClientViewModel)
    viewModelOf(::EditClientViewModel)
}
