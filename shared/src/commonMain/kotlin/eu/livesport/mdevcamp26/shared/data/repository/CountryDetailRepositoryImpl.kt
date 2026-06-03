package eu.livesport.mdevcamp26.shared.data.repository

import eu.livesport.mdevcamp26.shared.data.mapper.toDomain
import eu.livesport.mdevcamp26.shared.data.remote.WcRemoteDataSource
import eu.livesport.mdevcamp26.shared.domain.model.CountryDetail
import eu.livesport.mdevcamp26.shared.domain.model.Result
import eu.livesport.mdevcamp26.shared.domain.repository.CountryDetailRepository
import eu.livesport.mdevcamp26.shared.domain.repository.key.CountryDetailKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class CountryDetailRepositoryImpl(
    private val remoteDataSource: WcRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CountryDetailRepository {

    override fun getData(key: CountryDetailKey): Flow<Result<CountryDetail>> =
        flow {
            val result = try {
                Result.Success(remoteDataSource.getCountryDetail(key.countryCode).toDomain())
            } catch (e: Exception) {
                Result.Error(e)
            }

            emit(result)
        }.flowOn(ioDispatcher)
}