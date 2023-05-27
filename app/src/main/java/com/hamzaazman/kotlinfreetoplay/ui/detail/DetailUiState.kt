package com.hamzaazman.kotlinfreetoplay.ui.detail

import com.hamzaazman.kotlinfreetoplay.domain.model.GameDetailUi

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val data: GameDetailUi) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

