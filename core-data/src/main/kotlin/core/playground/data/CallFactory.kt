package core.playground.data

import okhttp3.Call
import okhttp3.OkHttpClient

/**
 * NOTES
 * we having this because we have multiple api need to customize their okhttp for Auth
 * also because we want to try out different HttpEngine
 */
abstract class CallFactory {
    abstract operator fun invoke(block: () -> OkHttpClient): Call.Factory
}
