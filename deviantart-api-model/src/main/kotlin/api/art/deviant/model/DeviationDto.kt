package api.art.deviant.model

import kotlinx.serialization.Serializable

@Serializable
data class DeviationDto(
    @Suppress("SpellCheckingInspection")
    val deviationid: String,
    val url: String,
    val title: String,
    val content: DeviationImageDto,
)

@Serializable
data class DeviationImageDto(
    val src: String,
    val height: Int,
    val width: Int,
)

@Serializable
data class DeviationResultDto(
    val next_offset: Int? = null,
    val results: List<DeviationDto>,
)
