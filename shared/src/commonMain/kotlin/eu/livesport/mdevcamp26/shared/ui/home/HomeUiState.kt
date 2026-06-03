package eu.livesport.mdevcamp26.shared.ui.home

import eu.livesport.mdevcamp26.shared.domain.model.WcGroup

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val header: String,
        val title: String,
        val subtitle: String,
        val groups: List<WcGroup>,
        val kickoffMs: Long,
        val stadium: String,
        val searchQuery: String,
    ) : HomeUiState {
        val visibleCount: Int = groups.sumOf { it.countries.size }
    }

    data class Error(val message: String) : HomeUiState
}
