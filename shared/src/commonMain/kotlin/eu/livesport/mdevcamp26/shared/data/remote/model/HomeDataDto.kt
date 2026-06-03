package eu.livesport.mdevcamp26.shared.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class HomeDataDto(
    val header: String,
    val title: String,
    val subtitle: String,
    val kickoff: String,
    val stadium: String,
    val groups: List<WcGroupDto>,
)

@Serializable
internal data class WcGroupDto(
    val name: String,
    val countries: List<CountryDto>,
)

@Serializable
internal data class CountryDto(
    val code: String,
    val name: String,
    val ranking: Int,
    val appearances: Int,
    val flagUrl: String? = null,
)
