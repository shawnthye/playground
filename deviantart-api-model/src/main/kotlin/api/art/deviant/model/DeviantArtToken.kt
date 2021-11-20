@file:UseSerializers(NotBlankStringSerializer::class)

package api.art.deviant.model

import api.art.deviant.model.json.NotBlankStringSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class DeviantArtToken(val access_token: String, val token_type: String)
