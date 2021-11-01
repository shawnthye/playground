@file:Suppress("unused")

package core.playground

import javax.inject.Qualifier

/**
 * This dispatcher is optimized to perform CPU-intensive work outside of the main thread.
 *
 * Example use cases include sorting a list and parsing JSON.
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

/**
 * This dispatcher is optimized to perform disk or network I/O outside of the main thread.
 *
 * Examples include using the Room component, reading from or writing to files, and running any
 * network operations.
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

/**
 *  Use this dispatcher to run a coroutine on the main Android thread. This should be used only for
 *  interacting with the UI and performing quick work.
 *
 *  Examples include calling suspend functions, running Android UI framework operations, and
 *  updating LiveData objects.
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

/**
 * Executes coroutines immediately when it is already in the right context
 *
 * See [medium](https://medium.com/@trionkidnapper/launching-a-kotlin-coroutine-for-immediate-execution-on-the-main-thread-8555e701163b)
 *
 * Example
 *
 * ```
 * launch {
 *     Log.d("MainActivity", "A")
 * }
 *
 * launch(Dispatchers.Main.immediate) {
 *     Log.d("MainActivity", "B")
 * }
 * ```
 *
 * OUTPUT
 * - B <--- immediate goes first
 * - A
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher

/**
 * This provide the application CoroutineScope, so coroutine will survive until application killed
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationScope
