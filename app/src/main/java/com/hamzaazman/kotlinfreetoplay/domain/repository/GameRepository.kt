package com.hamzaazman.kotlinfreetoplay.domain.repository

import com.hamzaazman.kotlinfreetoplay.common.NetworkResource
import com.hamzaazman.kotlinfreetoplay.domain.model.GameUi
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun getAllGame(): Flow<NetworkResource<List<GameUi>>>
    suspend fun getGameByCategory(category: String): Flow<NetworkResource<List<GameUi>>>
}