package eu.livesport.mdevcamp26.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import eu.livesport.mdevcamp26.R
import eu.livesport.mdevcamp26.domain.repository.model.Country
import eu.livesport.mdevcamp26.domain.repository.model.WcGroup
import eu.livesport.mdevcamp26.ui.theme.WcTheme

@Composable
fun GroupCard(
    group: WcGroup,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val errorColor = MaterialTheme.colorScheme.error
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val groupLabel = stringResource(R.string.group_label)
            Text(
                text = buildAnnotatedString {
                    withStyle(MaterialTheme.typography.displaySmall.toSpanStyle().copy(color = onSurface)) {
                        append("$groupLabel ")
                    }
                    withStyle(MaterialTheme.typography.displaySmall.toSpanStyle().copy(color = errorColor)) {
                        append(group.name)
                    }
                },
            )
            Spacer(modifier = Modifier.height(14.dp))
            group.countries.chunked(2).forEachIndexed { rowIndex, pair ->
                if (rowIndex > 0) Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    pair.forEachIndexed { colIndex, country ->
                        CountryButton(
                            country = country,
                            onClick = dropUnlessResumed { onCountryClick(country.code) },
                            modifier = Modifier
                                .weight(1f)
                                .then(if (colIndex > 0) Modifier.padding(start = 8.dp) else Modifier),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupCardPreview() {
    WcTheme {
        GroupCard(
            group = WcGroup(
                name = "C",
                countries = listOf(
                    Country("ARG", "Argentina", 1, 18),
                    Country("POL", "Poland", 26, 9),
                    Country("MEX", "Mexico", 15, 17),
                )
            ),
            onCountryClick = {}
        )
    }
}
