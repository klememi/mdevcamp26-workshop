package eu.livesport.mdevcamp26.shared.ui.detail

import eu.livesport.mdevcamp26.shared.domain.model.CountryDetail

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val detail: CountryDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
