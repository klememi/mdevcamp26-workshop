package eu.livesport.mdevcamp26.data.remote

import eu.livesport.mdevcamp26.data.remote.model.CountryDetailDto
import eu.livesport.mdevcamp26.data.remote.model.HomeDataDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal interface WcRemoteDataSource {
    suspend fun getHomeData(): HomeDataDto

    suspend fun getCountryDetail(code: String): CountryDetailDto
}

internal class WcRemoteDataSourceImpl(
    private val client: HttpClient,
) : WcRemoteDataSource {
    override suspend fun getHomeData(): HomeDataDto {
        return client.get(ENDPOINT_MAIN).body()
    }

    override suspend fun getCountryDetail(code: String): CountryDetailDto {
        return client.get(ENDPOINT_DETAIL) {
            parameter(PARAM_CODE, code)
        }.body()
    }

    companion object {
        private const val ENDPOINT_MAIN = "main"
        private const val ENDPOINT_DETAIL = "detail"
        private const val PARAM_CODE = "code"
    }
}
