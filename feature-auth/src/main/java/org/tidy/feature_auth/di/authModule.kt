package org.tidy.feature_auth.di


import LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tidy.feature_auth.data.AuthRepositoryImpl
import org.tidy.feature_auth.domain.AuthRepository
import org.tidy.feature_auth.domain.LoginUseCase
import org.tidy.feature_auth.domain.RegisterUseCase

import org.tidy.feature_auth.presentation.register.RegisterViewModel

val authModule = module {
    // Instância única do FirebaseAuth
    single { FirebaseAuth.getInstance() }

    // Injeta a implementação do repositório
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // Caso de uso
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }

    // ViewModel
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}