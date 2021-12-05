package testing.playground.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension

/**
 * Add this JUnit 5 extension to your test class using
 * [JvmField]
 * [RegisterExtension]
 *
 * // @JvmField
 * // @RegisterExtension
 * // val coroutineExtension = MainCoroutineExtension()
 */
class MainCoroutineExtension(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
) : BeforeEachCallback,
    AfterEachCallback,
    TestCoroutineScope by TestCoroutineScope(testDispatcher) {

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
        cleanupTestCoroutines()
    }
}

fun MainCoroutineExtension.runBlockingTest(
    block: suspend TestCoroutineScope.() -> Unit,
) = this.testDispatcher.runBlockingTest {
    block()
}

/**
 * Creates a new [CoroutineScope] with the rule's testDispatcher
 */
@Suppress("unused", "TestFunctionName")
fun MainCoroutineExtension.CoroutineScope(): CoroutineScope = CoroutineScope(testDispatcher)
