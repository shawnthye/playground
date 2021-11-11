package api.art.deviant.model

import kotlinx.serialization.Serializable

@Serializable
data class DeviantArtToken(val access_token: String, val token_type: String)
