@file:UseSerializers(NotBlankStringSerializer::class)

package api.art.deviant.model

import api.art.deviant.model.json.NotBlankStringSerializer
import kotlinx.serialization.UseSerializers

data class Stat(
    val views: Int,
    val views_today: Int,
    val favourites: Int,
    val comments: Int,
    val downloads: Int,
    val downloads_today: Int,
)
