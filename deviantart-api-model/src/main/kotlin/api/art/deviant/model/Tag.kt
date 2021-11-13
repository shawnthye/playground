package api.art.deviant.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val tagName: String,
    val sponsored: Boolean,
    val sponsor: String = "",
)
