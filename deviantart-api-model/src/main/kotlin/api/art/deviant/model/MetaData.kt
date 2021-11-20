@file:UseSerializers(NotBlankStringSerializer::class)

package api.art.deviant.model

import api.art.deviant.model.json.NotBlankStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.UseSerializers

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
