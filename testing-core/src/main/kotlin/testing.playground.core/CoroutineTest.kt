package testing.playground.core

import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.extension.RegisterExtension

open class CoroutineTest {

    @JvmField
    @RegisterExtension
    internal val coroutineExtension = MainCoroutineExtension()

    protected val testDispatcher = coroutineExtension.testDispatcher

    protected fun runBlockingTest(block: suspend () -> Unit) = coroutineExtension.runBlockingTest {
        block()
    }
}
