@file:UseSerializers(NotBlankStringSerializer::class)

package api.art.deviant.model

import api.art.deviant.model.json.NotBlankStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class MetaData(

    @Suppress("SpellCheckingInspection")
    @SerialName("deviationid")
    val deviationId: String,

    @Suppress("SpellCheckingInspection")
    @SerialName("printid")
    val printId: String,

    val author: User,

    val tags: List<Tag>,

    val stats: MetaDataStats,
)

@Serializable
data class MetaDataStats(
    val views: Int,
    val views_today: Int,
    val favourites: Int,
    val comments: Int,
    val downloads: Int,
    val downloads_today: Int,
)
