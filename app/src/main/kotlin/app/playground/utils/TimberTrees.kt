package app.playground.utils

import android.os.Build
import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

internal object CrashlyticsTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.WARN,
            Log.ERROR,
            -> Firebase.crashlytics.log(message)
        }

        val throwable = t ?: return
        Firebase.crashlytics.recordException(throwable)
    }
}

internal class DebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {

        val tag = "Timber:${super.createStackElementTag(element)!!}"

        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
    }

    private companion object {
        private const val MAX_TAG_LENGTH = 23
    }
}
