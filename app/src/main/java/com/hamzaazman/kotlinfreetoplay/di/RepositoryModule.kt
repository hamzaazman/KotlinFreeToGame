package com.hamzaazman.kotlinfreetoplay.di

import com.hamzaazman.kotlinfreetoplay.data.api.GameApi
import com.hamzaazman.kotlinfreetoplay.data.repository.GameRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGameRepository(gameApi: GameApi): GameRepositoryImpl {
        return GameRepositoryImpl(gameApi)
    }
}