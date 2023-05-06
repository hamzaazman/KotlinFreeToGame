package com.hamzaazman.kotlinfreetoplay.domain.model

data class GameUi(
    val id: Int,
    val platform: String,
    val release_date: String,
    val short_description: String,
    val genre: String,
    val thumbnail: String,
    val title: String,
    val game_url: String,
)
