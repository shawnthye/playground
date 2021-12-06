package core.playground

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object Strings {
    fun String.findExtension(): String? {
        val url = lowercase().toHttpUrlOrNull() ?: return null
        val last = url.pathSegments.lastOrNull { it.isNotBlank() } ?: return null
        val ext = last.substringAfterLast(".", "")

        return ext.takeUnless { it.isBlank() }
    }
}
