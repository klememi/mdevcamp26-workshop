package eu.livesport.mdevcamp26.data.mapper

import eu.livesport.mdevcamp26.data.remote.model.CountryDetailDto
import eu.livesport.mdevcamp26.data.remote.model.CountryDto
import eu.livesport.mdevcamp26.data.remote.model.GroupTeamDto
import eu.livesport.mdevcamp26.data.remote.model.HomeDataDto
import eu.livesport.mdevcamp26.data.remote.model.WcGroupDto
import eu.livesport.mdevcamp26.domain.repository.model.Country
import eu.livesport.mdevcamp26.domain.repository.model.CountryDetail
import eu.livesport.mdevcamp26.domain.repository.model.GroupTeam
import eu.livesport.mdevcamp26.domain.repository.model.HomeData
import eu.livesport.mdevcamp26.domain.repository.model.WcGroup
import kotlin.time.Instant

internal fun CountryDto.toDomain(): Country = Country(
    code = code,
    name = name,
    ranking = ranking,
    appearances = appearances,
    flagUrl = flagUrl,
)

internal fun WcGroupDto.toDomain(): WcGroup = WcGroup(
    name = name,
    countries = countries.map { it.toDomain() },
)

internal fun HomeDataDto.toDomain(): HomeData = HomeData(
    header = header,
    title = title,
    subtitle = subtitle,
    kickoffMs = parseKickoff(kickoff),
    stadium = stadium,
    groups = groups.map { it.toDomain() },
)

internal fun GroupTeamDto.toDomain(): GroupTeam = GroupTeam(
    name = name,
    flagUrl = flagUrl,
)

internal fun CountryDetailDto.toDomain(): CountryDetail = CountryDetail(
    group = group,
    code = code,
    name = name,
    nick = nick,
    appearances = appearances,
    best = best,
    ranking = ranking,
    topScorer = topScorer,
    star = star,
    moment = moment,
    fact = fact,
    groupCountries = groupCountries.map { it.toDomain() },
    flagUrl = flagUrl,
)

// ISO-8601: yyyy-MM-dd'T'HH:mm:ssX (e.g. 2026-06-11T18:00:00Z)
private fun parseKickoff(kickoff: String): Long = try {
    Instant.parse(kickoff).toEpochMilliseconds()
} catch (_: Exception) {
    0L
}
