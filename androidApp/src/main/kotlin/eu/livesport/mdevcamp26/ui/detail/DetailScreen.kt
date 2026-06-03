package eu.livesport.mdevcamp26.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.livesport.mdevcamp26.domain.repository.model.CountryDetail
import eu.livesport.mdevcamp26.domain.repository.model.GroupTeam
import eu.livesport.mdevcamp26.ui.detail.components.DetailBody
import eu.livesport.mdevcamp26.ui.detail.components.DetailHero
import eu.livesport.mdevcamp26.ui.theme.WcTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    countryCode: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = koinViewModel(key = countryCode) { parametersOf(countryCode) },
) {
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()

    DetailContent(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
    )
}

@Composable
private fun DetailContent(
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        when (uiState) {
            is DetailUiState.Loading -> {
                DetailLoadingContent(modifier = Modifier.padding(innerPadding))
            }

            is DetailUiState.Error -> {
                DetailErrorContent(
                    message = uiState.message,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is DetailUiState.Success -> {
                DetailSuccessContent(
                    detail = uiState.detail,
                    onBackClick = onBackClick,
                    contentPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun DetailLoadingContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DetailErrorContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DetailSuccessContent(
    detail: CountryDetail,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        DetailHero(detail = detail, onBackClick = onBackClick)
        DetailBody(detail = detail)
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailSuccessPreview() {
    WcTheme {
        DetailContent(
            uiState = DetailUiState.Success(
                detail = CountryDetail(
                    group = "A",
                    code = "MEX",
                    name = "Mexico",
                    nick = "El Tri",
                    appearances = 17,
                    best = "Quarter-final",
                    ranking = 15,
                    topScorer = "Javier Hernández",
                    star = "Hirving Lozano",
                    moment = "1986 Quarter-final",
                    fact = "Only nation to host two World Cups (1970 & 1986)",
                    groupCountries = listOf(
                        GroupTeam("Netherlands"),
                        GroupTeam("Egypt")
                    ),
                    flagUrl = ""
                )
            ),
            onBackClick = {},
        )
    }
}
