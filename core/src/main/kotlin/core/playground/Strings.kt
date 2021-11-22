package core.playground

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object Strings {
    fun String.findExtension(): String? {
        return lowercase()
            .toHttpUrlOrNull()
            ?.pathSegments
            ?.lastOrNull()
            ?.substringAfterLast(".", "")
            .takeUnless { it.isNullOrBlank() }
    }
}
