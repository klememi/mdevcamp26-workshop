package eu.livesport.mdevcamp26.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.livesport.mdevcamp26.domain.repository.HomeDataRepository
import eu.livesport.mdevcamp26.domain.repository.key.HomeDataKey
import eu.livesport.mdevcamp26.shared.domain.model.HomeData
import eu.livesport.mdevcamp26.shared.domain.model.Result
import eu.livesport.mdevcamp26.shared.domain.model.WcGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    repository: HomeDataRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> =
        combine(
            repository.getData(HomeDataKey),
            _query
        ) { result, query ->
            toUiState(result, query)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

    fun onQueryChange(query: String) {
        _query.value = query
    }

    private fun toUiState(result: Result<HomeData>, query: String): HomeUiState {
        return when (result) {
            is Result.Error -> HomeUiState.Error("Failed to load data. Please try again later.")
            is Result.Success -> {
                val homeData = result.data
                HomeUiState.Success(
                    header = homeData.header,
                    title = homeData.title,
                    subtitle = homeData.subtitle,
                    groups = filterGroups(homeData.groups, query),
                    kickoffMs = homeData.kickoffMs,
                    stadium = homeData.stadium,
                    searchQuery = query
                )
            }
        }
    }

    private fun filterGroups(groups: List<WcGroup>, query: String): List<WcGroup> {
        if (query.isBlank()) return groups
        return groups
            .map { g -> g.copy(countries = g.countries.filter { it.name.contains(query, ignoreCase = true) }) }
            .filter { it.countries.isNotEmpty() }
    }
}
