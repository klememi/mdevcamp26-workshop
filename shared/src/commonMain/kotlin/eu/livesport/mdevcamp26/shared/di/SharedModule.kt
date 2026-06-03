package eu.livesport.mdevcamp26.shared.di

import eu.livesport.mdevcamp26.shared.data.remote.WcRemoteDataSource
import eu.livesport.mdevcamp26.shared.data.remote.WcRemoteDataSourceImpl
import eu.livesport.mdevcamp26.shared.data.remote.createHttpClient
import eu.livesport.mdevcamp26.shared.data.repository.CountryDetailRepositoryImpl
import eu.livesport.mdevcamp26.shared.data.repository.HomeDataRepositoryImpl
import eu.livesport.mdevcamp26.shared.domain.repository.CountryDetailRepository
import eu.livesport.mdevcamp26.shared.domain.repository.HomeDataRepository
import eu.livesport.mdevcamp26.shared.ui.detail.DetailViewModel
import eu.livesport.mdevcamp26.shared.ui.home.HomeViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    single<HttpClient> { createHttpClient() }
    singleOf(::WcRemoteDataSourceImpl) bind WcRemoteDataSource::class

    single<HomeDataRepository> { HomeDataRepositoryImpl(remoteDataSource = get()) }
    single<CountryDetailRepository> { CountryDetailRepositoryImpl(remoteDataSource = get()) }

    viewModelOf(::HomeViewModel)
    viewModel { params -> DetailViewModel(countryCode = params.get(), repository = get()) }
}
