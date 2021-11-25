package core.playground.data

interface PageableEntry {
    val nextPage: String?
}

interface Pageable<Entry : PageableEntry, Relation> {
    val entry: Entry
    val relation: Relation
}
