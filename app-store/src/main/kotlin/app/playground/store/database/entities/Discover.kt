package app.playground.store.database.entities

import androidx.room.Entity

@Entity(tableName = "discovery")
data class Discovery(
    val item: Any,
    val type: DiscoverType,
) {
    enum class DiscoverType {
        POST,
        TOPIC,
        COLLECTION,
    }
}
