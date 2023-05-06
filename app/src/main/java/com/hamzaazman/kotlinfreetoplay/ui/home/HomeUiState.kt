package com.hamzaazman.kotlinfreetoplay.ui.home

import com.hamzaazman.kotlinfreetoplay.domain.model.GameUi

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: List<GameUi>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}