package com.hamzaazman.kotlinfreetoplay.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.kotlinfreetoplay.common.NetworkResource
import com.hamzaazman.kotlinfreetoplay.data.repository.GameRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val gameRepository: GameRepositoryImpl,
) : ViewModel() {

    private val _detailData: MutableStateFlow<DetailUiState> =
        MutableStateFlow(DetailUiState.Loading)
    val detailData: StateFlow<DetailUiState> get() = _detailData.asStateFlow()

    suspend fun getGameDetailById(id: Int) = viewModelScope.launch {
        gameRepository.getGameDetailById(id).onEach { response ->
            when (response) {
                is NetworkResource.Loading -> {
                    _detailData.value = DetailUiState.Loading
                }

                is NetworkResource.Error -> {
                    _detailData.value = DetailUiState.Error(response.throwable.toString())
                }

                is NetworkResource.Success -> {
                    _detailData.value = DetailUiState.Success(data = response.data)
                }
            }
        }.flowOn(Dispatchers.IO).collect()
    }

}