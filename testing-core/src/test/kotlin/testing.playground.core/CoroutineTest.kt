package testing.playground.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.coroutines.CoroutineContext

open class CoroutineTest : CoroutineScope {

    @JvmField
    @RegisterExtension
    internal val coroutineExtension = MainCoroutineExtension()

    protected val testDispatcher = coroutineExtension.testDispatcher

    protected fun runBlockingTest(block: suspend () -> Unit) = coroutineExtension.runBlockingTest {
        block()
    }

    override val coroutineContext: CoroutineContext
        get() = testDispatcher
}
