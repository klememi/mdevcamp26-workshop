package eu.livesport.mdevcamp26.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import eu.livesport.mdevcamp26.R
import eu.livesport.mdevcamp26.shared.domain.model.CountryDetail
import eu.livesport.mdevcamp26.ui.theme.WcHeroDisplay
import eu.livesport.mdevcamp26.ui.theme.WcMicroLabel
import eu.livesport.mdevcamp26.ui.theme.WcStatValueLarge
import eu.livesport.mdevcamp26.ui.theme.WcTheme

@Composable
internal fun DetailHero(detail: CountryDetail, onBackClick: () -> Unit) {
    val ctx = LocalContext.current
    val heroImageRequest = remember(detail.flagUrl) {
        ImageRequest.Builder(ctx).data(detail.flagUrl).crossfade(true).build()
    }
    val accentInk = MaterialTheme.colorScheme.onPrimary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 28.dp),
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(accentInk.copy(alpha = 0.12f), CircleShape),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_button_description),
                tint = accentInk,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        if (detail.group.isNotBlank()) {
            Text(
                text = "Group ${detail.group} · \"${detail.nick}\"".uppercase(),
                style = WcMicroLabel,
                color = accentInk.copy(alpha = 0.5f),
            )
        }
        Text(
            text = detail.name.uppercase(),
            style = WcHeroDisplay,
            color = accentInk,
        )
        Spacer(modifier = Modifier.height(28.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AsyncImage(
                model = heroImageRequest,
                contentDescription = detail.name,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(accentInk.copy(alpha = 0.12f)),
                error = ColorPainter(accentInk.copy(alpha = 0.12f)),
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
            ) {
                MiniStat(label = stringResource(R.string.fifa_rank_label), value = "#${detail.ranking}")
                MiniStat(label = stringResource(R.string.appearances_label), value = "${detail.appearances}×")
            }
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, modifier: Modifier = Modifier) {
    val accentInk = MaterialTheme.colorScheme.onPrimary
    Column(
        modifier = modifier
    ) {
        Text(
            text = label.uppercase(),
            style = WcMicroLabel,
            color = accentInk.copy(alpha = 0.5f),
        )
        Text(
            text = value.uppercase(),
            style = WcStatValueLarge,
            color = accentInk,
        )
    }
}

@Preview
@Composable
private fun DetailHeroPreview() {
    WcTheme {
        DetailHero(
            detail = CountryDetail(
                group = "C",
                code = "ARG",
                name = "Argentina",
                nick = "La Albiceleste",
                appearances = 18,
                best = "Winner (1978, 1986, 2022)",
                ranking = 1,
                topScorer = "Lionel Messi",
                star = "Lionel Messi",
                moment = "Winning the 2022 World Cup",
                fact = "Argentina has appeared in six World Cup finals.",
                groupCountries = emptyList(),
                flagUrl = null,
            ),
            onBackClick = {},
        )
    }
}
