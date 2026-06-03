package eu.livesport.mdevcamp26.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.livesport.mdevcamp26.ui.theme.WcMicroLabel
import eu.livesport.mdevcamp26.ui.theme.WcStatValueMedium
import eu.livesport.mdevcamp26.ui.theme.WcStatValueSmall
import eu.livesport.mdevcamp26.ui.theme.WcTheme

@Composable
internal fun StatBlockInverted(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = label,
            style = WcMicroLabel,
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
        )
        Text(
            text = value,
            style = WcStatValueMedium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
internal fun StatTile(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))
            .padding(14.dp),
    ) {
        Text(
            text = label,
            style = WcMicroLabel,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = WcStatValueSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatBlockInvertedPreview() {
    WcTheme {
        StatBlockInverted(label = "BEST FINISH", value = "WINNER (1978, 1986, 2022)")
    }
}

@Preview(showBackground = true)
@Composable
private fun StatTilePreview() {
    WcTheme {
        StatTile(label = "STAR · 2026", value = "LIONEL MESSI")
    }
}
