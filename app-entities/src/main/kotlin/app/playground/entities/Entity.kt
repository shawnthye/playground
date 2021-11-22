package app.playground.entities

interface Entity<ID> {
    val id: ID
}

data class AAA(override val id: Long) : Entity<Long>
