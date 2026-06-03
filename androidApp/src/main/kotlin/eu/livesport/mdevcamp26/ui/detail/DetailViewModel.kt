package eu.livesport.mdevcamp26.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.livesport.mdevcamp26.domain.repository.CountryDetailRepository
import eu.livesport.mdevcamp26.domain.repository.key.CountryDetailKey
import eu.livesport.mdevcamp26.shared.domain.model.CountryDetail
import eu.livesport.mdevcamp26.shared.domain.model.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DetailViewModel(
    countryCode: String,
    repository: CountryDetailRepository
) : ViewModel() {

    val uiState: StateFlow<DetailUiState> =
        repository.getData(CountryDetailKey(countryCode))
            .map { result -> toUiState(result) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DetailUiState.Loading
            )

    private fun toUiState(result: Result<CountryDetail>): DetailUiState {
        return when (result) {
            is Result.Success -> DetailUiState.Success(result.data)
            is Result.Error -> DetailUiState.Error("Failed to load data. Please try again later.")
        }
    }
}
