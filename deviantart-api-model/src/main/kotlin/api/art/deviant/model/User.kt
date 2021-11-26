@file:UseSerializers(NotBlankStringSerializer::class)

package api.art.deviant.model

import api.art.deviant.model.json.NotBlankStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class User(
    val userid: String,

    val username: String,

    @Suppress("SpellCheckingInspection")
    @SerialName("usericon")
    val iconUrl: String,
)
