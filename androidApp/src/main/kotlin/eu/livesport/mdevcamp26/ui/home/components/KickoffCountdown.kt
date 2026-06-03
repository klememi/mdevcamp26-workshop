package eu.livesport.mdevcamp26.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.livesport.mdevcamp26.R
import eu.livesport.mdevcamp26.ui.theme.WcCountdownDigit
import eu.livesport.mdevcamp26.ui.theme.WcCountdownUnits
import eu.livesport.mdevcamp26.ui.theme.WcKickoffLabel
import eu.livesport.mdevcamp26.ui.theme.WcMicroLabel
import eu.livesport.mdevcamp26.ui.theme.WcTheme
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

@Composable
fun KickoffCountdown(
    kickoffMs: Long,
    stadium: String,
    modifier: Modifier = Modifier,
) {
    var remainingMs by remember { mutableLongStateOf(kickoffMs - System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (remainingMs > 0) {
            delay(1000.milliseconds)
            remainingMs = kickoffMs - System.currentTimeMillis()
        }
    }
    val safe = remainingMs.coerceAtLeast(0L)
    val days = safe / 86_400_000L
    val hours = (safe % 86_400_000L) / 3_600_000L
    val mins = (safe % 3_600_000L) / 60_000L
    val secs = (safe % 60_000L) / 1_000L

    val onPrimary = MaterialTheme.colorScheme.onPrimary
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.kickoff_in).uppercase(),
                    style = WcKickoffLabel,
                    color = onPrimary,
                )
                Text(
                    text = stadium.uppercase(),
                    style = WcMicroLabel,
                    color = onPrimary.copy(alpha = 0.7f),
                )
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CountdownUnit(
                    value = "%02d".format(days),
                    label = "DAYS",
                    color = onPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = ":",
                    style = WcCountdownDigit,
                    color = onPrimary,
                    modifier = Modifier.align(Alignment.Top)
                )
                CountdownUnit(
                    value = "%02d".format(hours),
                    label = "HRS",
                    color = onPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = ":",
                    style = WcCountdownDigit,
                    color = onPrimary,
                    modifier = Modifier.align(Alignment.Top)
                )
                CountdownUnit(
                    value = "%02d".format(mins),
                    label = "MIN",
                    color = onPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = ":",
                    style = WcCountdownDigit,
                    color = onPrimary,
                    modifier = Modifier.align(Alignment.Top)
                )
                CountdownUnit(
                    value = "%02d".format(secs),
                    label = "SEC",
                    color = onPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CountdownUnit(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = value,
            style = WcCountdownDigit,
            color = color,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            style = WcCountdownUnits,
            color = color.copy(alpha = 0.55f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KickoffCountdownPreview() {
    WcTheme(darkTheme = false) {
        KickoffCountdown(kickoffMs = 1749672000000L, stadium = "Estadio Azteca")
    }
}
