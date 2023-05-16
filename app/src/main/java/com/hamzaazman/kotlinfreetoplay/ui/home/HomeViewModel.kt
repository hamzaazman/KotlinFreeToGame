package com.hamzaazman.kotlinfreetoplay.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.kotlinfreetoplay.common.NetworkResource
import com.hamzaazman.kotlinfreetoplay.data.repository.GameRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepositoryImpl
) : ViewModel() {

    private val _gameList: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    val gameList: StateFlow<HomeUiState> get() = _gameList.asStateFlow()


     fun getAllGame() = viewModelScope.launch {
        gameRepository.getAllGame().onEach { response ->
            when (response) {
                is NetworkResource.Loading -> {
                    _gameList.value = HomeUiState.Loading
                }

                is NetworkResource.Error -> {
                    _gameList.value = HomeUiState.Error(response.throwable.toString())
                }

                is NetworkResource.Success -> {
                    _gameList.value = HomeUiState.Success(data = response.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getGameByCategory(category: String) {
        gameRepository.getGameByCategory(category).onEach { response ->
            when (response) {
                is NetworkResource.Loading -> {
                    _gameList.value = HomeUiState.Loading
                }

                is NetworkResource.Error -> {
                    _gameList.value = HomeUiState.Error(response.throwable.toString())
                }

                is NetworkResource.Success -> {
                    _gameList.value = HomeUiState.Success(data = response.data)
                }
            }
        }.launchIn(viewModelScope)
    }

}