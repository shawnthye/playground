package app.playground.ui.debug.data

import android.util.Log

enum class CoilLogLevel(val level: Int) {
    NONE(Log.ERROR),
    VERBOSE(Log.VERBOSE),
    DEBUG(Log.DEBUG),
    INFO(Log.INFO),
    WARN(Log.WARN),
    ERROR(Log.ERROR),
    ASSERT(Log.ASSERT),
}
