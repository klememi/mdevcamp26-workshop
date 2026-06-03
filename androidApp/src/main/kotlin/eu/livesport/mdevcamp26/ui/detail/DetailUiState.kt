package eu.livesport.mdevcamp26.ui.detail

import eu.livesport.mdevcamp26.domain.repository.model.CountryDetail

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val detail: CountryDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
