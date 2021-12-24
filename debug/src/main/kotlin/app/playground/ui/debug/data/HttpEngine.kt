package app.playground.ui.debug.data

enum class HttpEngine {
    OKHTTP,
    CRONET;

    override fun toString(): String {
        return when (this) {
            CRONET -> "${super.toString()} (experimental)"
            else -> super.toString()
        }
    }
}
