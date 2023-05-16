package com.hamzaazman.kotlinfreetoplay.domain.datastore

interface DataStoreRepository {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
}