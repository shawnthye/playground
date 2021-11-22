package app.playground.entities.mappers

import api.art.deviant.model.Deviation
import api.art.deviant.model.DeviationImage
import app.playground.entities.entries.DeviationEntry
import core.playground.data.Mapper
import core.playground.findExtension
import javax.inject.Inject

class DeviationToEntity
@Inject constructor() : Mapper<Deviation, DeviationEntry>() {
    override suspend fun map(
        from: Deviation,
    ): DeviationEntry = DeviationEntry(
        deviationId = from.deviationid,
        url = from.url,
        title = from.title,
        coverUrl = from.findCover()!!.src,
        imageUrl = from.findImage()!!.src,
        imageWidth = from.findImage()?.width ?: 0,
        imageHeight = from.findImage()?.height ?: 0,
    )
}

internal val Deviation.isGif: Boolean
    get() = "gif" == content?.src?.findExtension()

internal fun Deviation.findCover(): DeviationImage? {
    return if (isGif) {
        content ?: thumbs?.lastOrNull() ?: preview
    } else {
        thumbs?.lastOrNull() ?: preview ?: content
    }
}

internal fun Deviation.findImage(): DeviationImage? {
    return content ?: preview ?: thumbs?.lastOrNull()
}
