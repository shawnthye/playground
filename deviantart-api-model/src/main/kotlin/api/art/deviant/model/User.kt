package api.art.deviant.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userid: String,
    val username: String,

    @SerialName("usericon")
    val iconUrl: String,
)
