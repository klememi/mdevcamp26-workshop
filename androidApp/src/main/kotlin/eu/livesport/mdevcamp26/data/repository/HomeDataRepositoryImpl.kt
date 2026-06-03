package eu.livesport.mdevcamp26.data.repository

import eu.livesport.mdevcamp26.data.mapper.toDomain
import eu.livesport.mdevcamp26.data.remote.WcRemoteDataSource
import eu.livesport.mdevcamp26.domain.repository.model.HomeData
import eu.livesport.mdevcamp26.domain.repository.model.Result
import eu.livesport.mdevcamp26.domain.repository.HomeDataRepository
import eu.livesport.mdevcamp26.domain.repository.key.HomeDataKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class HomeDataRepositoryImpl(
    private val remoteDataSource: WcRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : HomeDataRepository {

    override fun getData(key: HomeDataKey): Flow<Result<HomeData>> =
        flow {
            val result = try {
                Result.Success(remoteDataSource.getHomeData().toDomain())
            } catch (e: Exception) {
                Result.Error(e)
            }

            emit(result)
        }.flowOn(ioDispatcher)
}