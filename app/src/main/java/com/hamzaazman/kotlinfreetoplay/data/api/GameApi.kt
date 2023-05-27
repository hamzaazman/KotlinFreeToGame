package com.hamzaazman.kotlinfreetoplay.data.api

import com.hamzaazman.kotlinfreetoplay.data.dto.GameDetailDto
import com.hamzaazman.kotlinfreetoplay.data.dto.GameDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameApi {

    @GET("api/games")
    //@GET("games.json")
    suspend fun getAllGame(): List<GameDto>

    @GET("api/games/")
    suspend fun getGameByCategory(
        @Query("category") category: String
    ): List<GameDto>

    @GET("api/game")
    suspend fun getGameDetailById(
        @Query("id") gameId: Int
    ): GameDetailDto



}