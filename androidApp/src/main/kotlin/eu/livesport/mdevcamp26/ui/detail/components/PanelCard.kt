package eu.livesport.mdevcamp26.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.livesport.mdevcamp26.ui.theme.WcMicroLabel
import eu.livesport.mdevcamp26.ui.theme.WcTheme

@Composable
internal fun PanelCard(
    tag: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
            .padding(16.dp),
    ) {
        tag()
        Spacer(modifier = Modifier.height(10.dp))
        content()
    }
}

@Composable
internal fun InfoTag(bg: Color, text: String, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .padding(horizontal = 9.dp, vertical = 5.dp),
    ) {
        Text(
            text = text,
            style = WcMicroLabel,
            color = textColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PanelCardPreview() {
    WcTheme {
        PanelCard(
            tag = { InfoTag(bg = MaterialTheme.colorScheme.primary, text = "DID YOU KNOW?", textColor = MaterialTheme.colorScheme.onPrimary) },
            content = { Text("Argentina has appeared in six World Cup finals.") }
        )
    }
}
