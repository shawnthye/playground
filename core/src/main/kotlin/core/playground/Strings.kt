package core.playground

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

fun String.findExtension(): String? {
    return lowercase()
        .toHttpUrlOrNull()
        ?.pathSegments
        ?.lastOrNull()
        ?.substringAfterLast(".", "")
        .takeUnless { it.isNullOrBlank() }
}
