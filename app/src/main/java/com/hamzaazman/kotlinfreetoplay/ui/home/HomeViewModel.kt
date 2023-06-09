package com.hamzaazman.kotlinfreetoplay.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.kotlinfreetoplay.common.NetworkResource
import com.hamzaazman.kotlinfreetoplay.data.datastore.CategoryType
import com.hamzaazman.kotlinfreetoplay.data.datastore.DataStoreRepositoryImpl
import com.hamzaazman.kotlinfreetoplay.data.repository.GameRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepositoryImpl,
    private val dataStoreRepository: DataStoreRepositoryImpl
) : ViewModel() {

    private val _gameList: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    val gameList: StateFlow<HomeUiState> get() = _gameList.asStateFlow()

    val getCategoryAndId: Flow<CategoryType> = dataStoreRepository.getCategoryAndId
    suspend fun clearCategoryFilter() = dataStoreRepository.clearCategory()

    fun saveCategoryAndId(category: String, categoryId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveCategoryAndId(category = category, categoryId = categoryId)
        }

    fun getAllGame() = viewModelScope.launch(Dispatchers.IO) {
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