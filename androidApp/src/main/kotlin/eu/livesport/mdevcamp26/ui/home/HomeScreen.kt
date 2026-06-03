package eu.livesport.mdevcamp26.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import eu.livesport.mdevcamp26.domain.repository.model.Country
import eu.livesport.mdevcamp26.domain.repository.model.WcGroup
import eu.livesport.mdevcamp26.ui.home.components.GroupCard
import eu.livesport.mdevcamp26.ui.home.components.KickoffCountdown
import eu.livesport.mdevcamp26.ui.home.components.Masthead
import eu.livesport.mdevcamp26.ui.theme.WcTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        modifier = modifier,
        uiState = uiState,
        onQueryChange = homeViewModel::onQueryChange,
        onCountryClick = onCountryClick,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onQueryChange: (String) -> Unit,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        when (uiState) {
            is HomeUiState.Loading -> {
                HomeLoadingContent(modifier = Modifier.padding(innerPadding))
            }

            is HomeUiState.Error -> {
                HomeErrorContent(
                    message = uiState.message,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is HomeUiState.Success -> {
                HomeSuccessContent(
                    uiState = uiState,
                    onQueryChange = onQueryChange,
                    onCountryClick = onCountryClick,
                    contentPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun HomeLoadingContent(
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
private fun HomeErrorContent(
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
private fun HomeSuccessContent(
    uiState: HomeUiState.Success,
    onQueryChange: (String) -> Unit,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        item {
            Masthead(
                searchQuery = uiState.searchQuery,
                onQueryChange = onQueryChange,
                visibleCount = uiState.visibleCount,
                header = uiState.header,
                title = uiState.title,
                subtitle = uiState.subtitle,
            )
        }
        items(uiState.groups, key = { it.name }) { group ->
            GroupCard(group, onCountryClick = onCountryClick)
        }
        item {
            KickoffCountdown(
                kickoffMs = uiState.kickoffMs,
                stadium = uiState.stadium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeSuccessPreview() {
    WcTheme {
        HomeContent(
            uiState = HomeUiState.Success(
                groups = listOf(
                    WcGroup(
                        name = "A",
                        countries = listOf(
                            Country("MEX", "Mexico", 15, 17),
                            Country("NED", "Netherlands", 6, 11)
                        )
                    )
                ),
                kickoffMs = 1781208000000L,
                stadium = "Azteca Stadium",
                searchQuery = "",
                header = "This is header",
                title = "This is title",
                subtitle = "This is subtitle",
            ),
            onQueryChange = {},
            onCountryClick = {},
        )
    }
}
