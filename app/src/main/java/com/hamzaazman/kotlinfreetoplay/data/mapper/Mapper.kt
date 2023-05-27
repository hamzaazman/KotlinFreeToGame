package com.hamzaazman.kotlinfreetoplay.data.mapper

import com.hamzaazman.kotlinfreetoplay.data.dto.GameDetailDto
import com.hamzaazman.kotlinfreetoplay.data.dto.GameDto
import com.hamzaazman.kotlinfreetoplay.domain.model.GameDetailUi
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
fun detailToDomain(detailDto: GameDetailDto): GameDetailUi {
    return GameDetailUi(
        id = detailDto.id,
        title = detailDto.title.orEmpty(),
        thumbnail = detailDto.thumbnail.orEmpty(),
        publisher = detailDto.publisher.orEmpty(),
        platform = detailDto.platform.orEmpty(),
        description = detailDto.description.orEmpty(),
        gameUrl = detailDto.game_url.orEmpty(),
        genre = detailDto.genre.orEmpty(),
        freetogameProfile_url = detailDto.freetogame_profile_url.orEmpty(),
        minimumSystemRequirements = detailDto.minimum_system_requirements,
        releaseDate = detailDto.release_date.orEmpty(),
        screenshots = detailDto.screenshots.orEmpty(),
        shortDescription = detailDto.short_description.orEmpty()
    )
}