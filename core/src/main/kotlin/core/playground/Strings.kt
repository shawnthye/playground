package core.playground

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object Strings {
    fun String.findExtension(): String? = lowercase()
        .toHttpUrlOrNull()
        ?.pathSegments
        ?.lastOrNull()
        ?.substringAfterLast(".", "")
        .takeUnless { it.isNullOrBlank() }
}
