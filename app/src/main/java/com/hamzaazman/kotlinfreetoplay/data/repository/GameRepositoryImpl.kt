package com.hamzaazman.kotlinfreetoplay.data.repository

import com.hamzaazman.kotlinfreetoplay.common.NetworkResource
import com.hamzaazman.kotlinfreetoplay.data.api.GameApi
import com.hamzaazman.kotlinfreetoplay.data.mapper.detailToDomain
import com.hamzaazman.kotlinfreetoplay.data.mapper.toDomain
import com.hamzaazman.kotlinfreetoplay.domain.model.GameDetailUi
import com.hamzaazman.kotlinfreetoplay.domain.model.GameUi
import com.hamzaazman.kotlinfreetoplay.domain.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val api: GameApi
) : GameRepository {
    override suspend fun getAllGame(): Flow<NetworkResource<List<GameUi>>> = flow {
        emit(NetworkResource.Loading)
        try {
            val response = api.getAllGame()
            emit(NetworkResource.Success(data = response.map { toDomain(it) }))
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(NetworkResource.Error(e.localizedMessage?.toString()))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(NetworkResource.Error(e.localizedMessage?.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getGameByCategory(category: String): Flow<NetworkResource<List<GameUi>>> =
        flow {
            emit(NetworkResource.Loading)
            try {
                val response = api.getGameByCategory(category = category)
                emit(NetworkResource.Success(data = response.map { toDomain(it) }))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(NetworkResource.Error(e.localizedMessage?.toString()))
            } catch (e: IOException) {
                e.printStackTrace()
                emit(NetworkResource.Error(e.localizedMessage?.toString()))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getGameDetailById(id: Int): Flow<NetworkResource<GameDetailUi>> =
        flow {
            emit(NetworkResource.Loading)
            try {
                val response = api.getGameDetailById(id)
                emit(NetworkResource.Success(data = detailToDomain(response)))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(NetworkResource.Error(e.localizedMessage?.toString()))
            } catch (e: IOException) {
                e.printStackTrace()
                emit(NetworkResource.Error(e.localizedMessage?.toString()))
            }
        }.flowOn(Dispatchers.IO)
}