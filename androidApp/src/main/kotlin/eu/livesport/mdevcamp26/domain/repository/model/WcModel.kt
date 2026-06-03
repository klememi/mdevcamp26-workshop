package eu.livesport.mdevcamp26.domain.repository.model

data class Country(
    val code: String,
    val name: String,
    val ranking: Int,
    val appearances: Int,
    val flagUrl: String? = null,
)

data class WcGroup(
    val name: String,
    val countries: List<Country>,
)

data class HomeData(
    val header: String,
    val title: String,
    val subtitle: String,
    val kickoffMs: Long,
    val stadium: String,
    val groups: List<WcGroup>,
)

data class GroupTeam(
    val name: String,
    val flagUrl: String? = null,
)

data class CountryDetail(
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
    val groupCountries: List<GroupTeam>,
    val flagUrl: String? = null,
)
