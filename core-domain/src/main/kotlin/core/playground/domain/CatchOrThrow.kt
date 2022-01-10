package core.playground.domain

import core.playground.Reason

internal fun <T> Throwable.catchOrThrow(caught: (Throwable) -> T): T {
    return when {
        this is Reason -> caught(this)
        // for apollo where they wrap our Reason in nested cause
        cause is Reason -> caught(this)
        // beside network or api issue we throw exception while we are not in Debug build
        else -> caught(this)
    }
}
