package eu.livesport.mdevcamp26.ui.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.livesport.mdevcamp26.R
import eu.livesport.mdevcamp26.shared.domain.model.CountryDetail
import eu.livesport.mdevcamp26.shared.domain.model.GroupTeam
import eu.livesport.mdevcamp26.ui.theme.WcMicroLabel
import eu.livesport.mdevcamp26.ui.theme.WcMomentTitle
import eu.livesport.mdevcamp26.ui.theme.WcTheme

@Composable
internal fun DetailBody(detail: CountryDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
    ) {
        StatBlockInverted(label = stringResource(R.string.best_finish_label), value = detail.best.uppercase())
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            StatTile(
                label = stringResource(R.string.star_2026_label),
                value = detail.star.uppercase(),
                modifier = Modifier.weight(1f).fillMaxHeight(),
            )
            StatTile(
                label = stringResource(R.string.all_time_top_scorer_label),
                value = detail.topScorer.uppercase(),
                modifier = Modifier.weight(1f).fillMaxHeight(),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        PanelCard(
            tag = { InfoTag(bg = MaterialTheme.colorScheme.error, text = stringResource(R.string.moment_tag).uppercase(), textColor = Color.White) },
            content = {
                Text(
                    text = detail.moment.uppercase(),
                    style = WcMomentTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        PanelCard(
            tag = {
                InfoTag(
                    bg = MaterialTheme.colorScheme.primary,
                    text = stringResource(R.string.did_you_know_tag).uppercase(),
                    textColor = MaterialTheme.colorScheme.onPrimary,
                )
            },
            content = {
                Text(
                    text = detail.fact,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
        )
        if (detail.groupCountries.isNotEmpty()) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Also in Group ${detail.group}".uppercase(),
                style = WcMicroLabel,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(start = 2.dp),
            ) {
                items(detail.groupCountries, key = { it.name }) { mate ->
                    GroupmateChip(mate)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailBodyPreview() {
    WcTheme {
        DetailBody(
            detail = CountryDetail(
                group = "A",
                code = "MEX",
                name = "Mexico",
                nick = "El Tri",
                appearances = 17,
                best = "Quarter final",
                ranking = 15,
                topScorer = "Javier Hernández",
                star = "Hirving Lozano",
                moment = "1986 Quarter-final",
                fact = "Only nation to host two WorldCups",
                groupCountries = listOf(GroupTeam("USA")),
                flagUrl = null
            )
        )
    }
}
