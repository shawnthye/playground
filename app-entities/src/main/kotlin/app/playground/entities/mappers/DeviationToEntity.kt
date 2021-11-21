package app.playground.entities.mappers

import api.art.deviant.model.Deviation
import app.playground.entities.DeviationEntity
import core.playground.data.Mapper
import javax.inject.Inject

class DeviationToEntity
@Inject constructor() : Mapper<Deviation, DeviationEntity>() {
    override suspend fun map(
        from: Deviation,
    ): DeviationEntity = DeviationEntity(
        deviationId = from.deviationid,
        url = from.url,
        title = from.title,
        imageSrc = (from.thumbs?.lastOrNull() ?: from.preview ?: from.content)!!.src,
        imageHeight = from.content?.height ?: from.thumbs?.lastOrNull()?.height ?: 0,
        imageWidth = from.content?.width ?: from.thumbs?.lastOrNull()?.width ?: 0,
    )
}
