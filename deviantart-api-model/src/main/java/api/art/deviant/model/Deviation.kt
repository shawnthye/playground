package api.art.deviant.model

data class Deviation(
    @Suppress("SpellCheckingInspection")
    val deviationid: String,
    val url: String,
    val title: String,
    val content: DeviationImage,
)

data class DeviationImage(
    val src: String,
    val height: Int,
    val width: Int,
    val width1: Int,
    val width2: Int,
)

data class DeviationResult(
    val next_offset: Int? = null,
    val results: List<Deviation>,
)
