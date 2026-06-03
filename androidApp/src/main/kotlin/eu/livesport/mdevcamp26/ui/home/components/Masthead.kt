package eu.livesport.mdevcamp26.ui.home.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.livesport.mdevcamp26.R
import eu.livesport.mdevcamp26.ui.theme.InterFamily
import eu.livesport.mdevcamp26.ui.theme.WcHeroDisplay
import eu.livesport.mdevcamp26.ui.theme.WcMicroLabel
import eu.livesport.mdevcamp26.ui.theme.WcTheme

@Composable
fun Masthead(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    visibleCount: Int,
    header: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    val onBg = MaterialTheme.colorScheme.onBackground
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 16.dp, bottom = 18.dp, start = 20.dp, end = 20.dp),
    ) {
        Text(
            text = header.uppercase(),
            style = WcMicroLabel,
            color = onBg.copy(alpha = 0.55f),
        )
        Spacer(modifier = Modifier.height(22.dp))
        val mastheadTitle = title.uppercase()
        Text(
            text = buildAnnotatedString {
                withStyle(WcHeroDisplay.toSpanStyle().copy(color = onBg)) {
                    append(mastheadTitle)
                }
                withStyle(WcHeroDisplay.toSpanStyle().copy(color = MaterialTheme.colorScheme.error)) {
                    append(".")
                }
            },
            style = WcHeroDisplay
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = subtitle,
            fontFamily = InterFamily,
            fontSize = 15.sp,
            color = onBg.copy(alpha = 0.55f),
            lineHeight = 22.sp,
        )
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text(stringResource(R.string.search_placeholder), fontFamily = InterFamily) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                Text(
                    text = "$visibleCount/48",
                    style = WcMicroLabel,
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MastheadLightPreview() {
    WcTheme(darkTheme = false) {
        Masthead(
            searchQuery = "",
            onQueryChange = {},
            visibleCount = 48,
            header = "Header",
            title = "Title",
            subtitle = "Subtitle",
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MastheadDarkPreview() {
    WcTheme(darkTheme = true) {
        Masthead(
            searchQuery = "",
            onQueryChange = {},
            visibleCount = 48,
            header = "Header",
            title = "Title",
            subtitle = "Subtitle",
        )
    }
}
