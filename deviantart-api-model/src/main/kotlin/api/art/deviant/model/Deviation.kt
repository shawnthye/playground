@file:UseSerializers(NotBlankStringSerializer::class)

package api.art.deviant.model

import api.art.deviant.model.json.NotBlankStringSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Date

@Serializable
data class Deviation(
    @Suppress("SpellCheckingInspection")
    val deviationid: String,
    val url: String,
    val title: String,
    val category: String,
    val content: DeviationImage? = null,
    val preview: DeviationImage? = null,
    val thumbs: List<DeviationImage>? = null,
    val author: User,
    val stats: DeviationStats,
    @SerialName("cover_image")
    val coverImage: Deviation? = null,

    @Contextual
    @SerialName("published_time")
    val published: Date,
)

@Serializable
data class DeviationImage(
    val src: String,
    val height: Int,
    val width: Int,
    @Suppress("SpellCheckingInspection")
    val filesize: Int = 0,
)

@Serializable
data class DeviationResult(
    val next_offset: Int? = null,
    val results: List<Deviation>,
)

@Serializable
data class DeviationStats(
    val favourites: Int,
    val comments: Int,
)
