package api.art.deviant.model

import kotlinx.serialization.SerialName

data class MetaData(

    @Suppress("SpellCheckingInspection")
    @SerialName("deviationid")
    val deviationId: String,

    @Suppress("SpellCheckingInspection")
    @SerialName("printid")
    val printId: String,

    val author: User,

    val tags: List<Tag>,
)
