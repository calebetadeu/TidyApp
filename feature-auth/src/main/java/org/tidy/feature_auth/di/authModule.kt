package org.tidy.feature_auth.di


import LoginViewModel
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tidy.feature_auth.data.AuthRepositoryImpl
import org.tidy.feature_auth.domain.repositories.AuthRepository
import org.tidy.feature_auth.domain.use_cases.AuthUseCase
import org.tidy.feature_auth.domain.use_cases.LoginUseCase
import org.tidy.feature_auth.domain.use_cases.RegisterUseCase
import org.tidy.feature_auth.presentation.auth.AuthViewModel

import org.tidy.feature_auth.presentation.register.RegisterViewModel

val authModule = module {
    // Instância única do FirebaseAuth
    single { FirebaseAuth.getInstance() }

    // Injeta a implementação do repositório
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // Caso de uso
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }

    // ViewModel
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)

    // Provedor do DataStore Preferences
    single {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile("auth_prefs")
        }
    }

    // UseCase que lida com a autenticação e verifica o estado do usuário
    single { AuthUseCase(get()) }

    // ViewModel que usa o UseCase
    viewModel { AuthViewModel(get()) }
}