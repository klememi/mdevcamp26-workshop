package eu.livesport.mdevcamp26.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import eu.livesport.mdevcamp26.shared.domain.model.GroupTeam
import eu.livesport.mdevcamp26.ui.theme.WcChipLabel
import eu.livesport.mdevcamp26.ui.theme.WcTheme

@Composable
internal fun GroupmateChip(groupTeam: GroupTeam) {
    val ctx = LocalContext.current
    val imageRequest = remember(groupTeam.flagUrl) {
        ImageRequest.Builder(ctx).data(groupTeam.flagUrl).crossfade(true).build()
    }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = imageRequest,
            contentDescription = groupTeam.name,
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.outline),
            error = ColorPainter(MaterialTheme.colorScheme.outline),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = groupTeam.name,
            style = WcChipLabel,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupmateChipPreview() {
    WcTheme {
        GroupmateChip(groupTeam = GroupTeam(name = "Poland", flagUrl = null))
    }
}
