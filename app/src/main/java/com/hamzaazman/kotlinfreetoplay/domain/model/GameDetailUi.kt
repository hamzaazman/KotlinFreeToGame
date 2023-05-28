package com.hamzaazman.kotlinfreetoplay.domain.model

import com.google.gson.annotations.SerializedName
import com.hamzaazman.kotlinfreetoplay.data.dto.MinimumSystemRequirements
import com.hamzaazman.kotlinfreetoplay.data.dto.Screenshot

data class GameDetailUi(
    val description: String?,
    @SerializedName("freetogame_profile_url")
    val freetogameProfile_url: String?,
    @SerializedName("game_url")
    val gameUrl: String?,
    val genre: String?,
    val id: Int,
    @SerializedName("minimum_system_requirements")
    val minimumSystemRequirements: MinimumSystemRequirements?,
    val platform: String?,
    val publisher: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val screenshots: List<Screenshot>?,
    val thumbnail: String?,
    val title: String?
)
