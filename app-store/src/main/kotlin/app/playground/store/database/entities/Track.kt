package app.playground.store.database.entities

enum class Track(val path: String) {
    UNKNOWN(""),
    RECOMMENDED("recommended"),
    DAILY(@Suppress("SpellCheckingInspection") "dailydeviations"),
    NEWEST("newest"),
    HOT("hot")
}
