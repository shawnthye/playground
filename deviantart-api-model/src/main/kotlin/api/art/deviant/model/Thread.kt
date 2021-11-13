package api.art.deviant.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(

    @Suppress("SpellCheckingInspection")
    @SerialName("commentid")
    val id: String,

    @Suppress("SpellCheckingInspection")
    @SerialName("parentid")
    val parentId: String,

    val posted: String,
    val replies: Int,
    val hidden: String?,
    val body: String,
    val likes: Int,
    val user: User,
)
