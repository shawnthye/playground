package api.art.deviant.model

import kotlinx.serialization.Serializable

@Serializable
data class Deviation(
    @Suppress("SpellCheckingInspection")
    val deviationid: String,
    val url: String,
    val title: String,
    val content: DeviationImage,
)

@Serializable
data class DeviationImage(
    val src: String,
    val height: Int,
    val width: Int,
)

@Serializable
data class DeviationResult(
    val next_offset: Int? = null,
    val results: List<Deviation>,
)
