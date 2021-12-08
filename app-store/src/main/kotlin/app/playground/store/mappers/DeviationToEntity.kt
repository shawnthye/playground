package app.playground.store.mappers

import api.art.deviant.model.DeviationImage
import app.playground.store.database.entities.Deviation
import core.playground.Strings.findExtension
import core.playground.data.Mapper
import javax.inject.Inject
import api.art.deviant.model.Deviation as DeviantArtDeviation

class DeviationToEntity
@Inject constructor() : Mapper<DeviantArtDeviation, Deviation>() {
    override suspend fun parse(
        from: DeviantArtDeviation,
    ): Deviation = Deviation(
        deviationId = from.deviationid,
        url = from.url,
        title = from.title,
        category = from.category,
        coverUrl = from.findCover()!!.src,
        imageUrl = from.findImage()!!.src,
        imageWidth = from.findImage()?.width ?: 0,
        imageHeight = from.findImage()?.height ?: 0,
        authorId = from.author.userid,
        authorName = from.author.username,
        authorIconUrl = from.author.iconUrl,
        published = from.published,
    )
}

internal val DeviantArtDeviation.isGif: Boolean
    get() = "gif" == content?.src?.findExtension()

internal fun DeviantArtDeviation.findCover(): DeviationImage? {
    return if (isGif) {
        content ?: thumbs?.lastOrNull() ?: preview
    } else {
        thumbs?.lastOrNull() ?: preview ?: content
    }
}

internal fun DeviantArtDeviation.findImage(): DeviationImage? {
    return content ?: preview ?: thumbs?.lastOrNull()
}
