package eu.livesport.mdevcamp26.shared.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class CountryDetailDto(
    val group: String,
    val code: String,
    val name: String,
    val nick: String,
    val appearances: Int,
    val best: String,
    val ranking: Int,
    val topScorer: String,
    val star: String,
    val moment: String,
    val fact: String,
    val groupCountries: List<GroupTeamDto>,
    val flagUrl: String? = null,
)

@Serializable
internal data class GroupTeamDto(
    val name: String,
    val flagUrl: String? = null,
)
