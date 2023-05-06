package com.hamzaazman.kotlinfreetoplay.data.mapper

import com.hamzaazman.kotlinfreetoplay.data.dto.GameDto
import com.hamzaazman.kotlinfreetoplay.domain.model.GameUi


fun toDomain(dto: GameDto): GameUi {
    return GameUi(
        id = dto.id,
        title = dto.title.orEmpty(),
        game_url = dto.game_url.orEmpty(),
        platform = dto.platform.orEmpty(),
        genre = dto.genre.orEmpty(),
        release_date = dto.release_date.orEmpty(),
        short_description = dto.short_description.orEmpty(),
        thumbnail = dto.thumbnail.orEmpty()
    )
}