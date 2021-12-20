package core.playground.data

interface PageableEntry {
    val nextPage: String?
}

interface Pageable<Entry : PageableEntry> {
    val entry: Entry
}
