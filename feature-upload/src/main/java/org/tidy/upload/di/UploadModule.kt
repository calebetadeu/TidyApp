package org.tidy.upload.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tidy.upload.data.remote.UploadService
import org.tidy.upload.data.repository.ReportRepositoryImpl
import org.tidy.upload.domain.ReportRepository
import org.tidy.upload.domain.ReportUseCase
import org.tidy.upload.presentation.ReportViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val uploadModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8001/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    viewModelOf(::ReportViewModel)
    single<UploadService> {
        get<Retrofit>().create(UploadService::class.java)
    }
    single<ReportRepository> {
        ReportRepositoryImpl(get())
    }
    factory { ReportUseCase(get()) }
}
