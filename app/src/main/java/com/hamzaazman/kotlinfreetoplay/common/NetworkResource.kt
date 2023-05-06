package com.hamzaazman.kotlinfreetoplay.common

sealed class NetworkResource<out T : Any> {
    object Loading : NetworkResource<Nothing>()
    data class Success<out T : Any>(val data: T) : NetworkResource<T>()
    data class Error(val throwable: String?) : NetworkResource<Nothing>()
}