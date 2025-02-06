package org.tidy.feature_clients.di

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tidy.feature_clients.data.local.ClientDatabase
import org.tidy.feature_clients.data.remote.ClientApi
import org.tidy.feature_clients.data.remote.FirebaseClientServiceImpl
import org.tidy.feature_clients.data.repostiory.ClientApiImpl
import org.tidy.feature_clients.data.repostiory.ClientRepositoryImpl
import org.tidy.feature_clients.data.repostiory.LocationRepositoryImpl
import org.tidy.feature_clients.domain.repositories.ClientRepository
import org.tidy.feature_clients.domain.repositories.LocationRepository
import org.tidy.feature_clients.domain.useCase.AddClientUseCase
import org.tidy.feature_clients.domain.useCase.GetClientByIdUseCase
import org.tidy.feature_clients.domain.useCase.GetClientsPagingUseCase
import org.tidy.feature_clients.domain.useCase.GetClientsUseCase
import org.tidy.feature_clients.domain.useCase.GetFilteredClientsUseCase
import org.tidy.feature_clients.domain.useCase.GetLocationsUseCase
import org.tidy.feature_clients.domain.useCase.SyncClientsUseCase
import org.tidy.feature_clients.domain.useCase.UpdateClientUseCase
import org.tidy.feature_clients.presentation.clients_list.ClientListViewModel
import org.tidy.feature_clients.presentation.edit_client.EditClientViewModel
import org.tidy.feature_clients.presentation.location.LocationsViewModel
import org.tidy.feature_clients.presentation.register_client.RegisterClientViewModel

val clientModule = module {
    single { FirebaseFirestore.getInstance() }
    single { ClientDatabase.getDatabase(get()) }
    single { get<ClientDatabase>().clientDao() }
    single<ClientApi> { ClientApiImpl(get()) }
    single { FirebaseClientServiceImpl() }
    single<LocationRepository> { LocationRepositoryImpl() }

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
    factory { GetLocationsUseCase(get()) }
    factory { GetClientsPagingUseCase(get()) }



    // ViewModels
    viewModelOf(::ClientListViewModel)
    viewModelOf(::RegisterClientViewModel)
    viewModelOf(::EditClientViewModel)
    viewModelOf(::LocationsViewModel)


//    private val getClientsUseCase: GetClientsUseCase,
//    private val getLocationsUseCase: GetLocationsUseCase,
//    private val syncClientsUseCase: SyncClientsUseCase
}
