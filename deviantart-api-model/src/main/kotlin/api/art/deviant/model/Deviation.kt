@file:UseSerializers(NotBlankStringSerializer::class)

package api.art.deviant.model

import api.art.deviant.model.json.NotBlankStringSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class Deviation(
    @Suppress("SpellCheckingInspection")
    val deviationid: String,
    val url: String,
    val title: String,
    val content: DeviationImage? = null,
    val preview: DeviationImage,
    val thumbs: List<DeviationImage>,
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
