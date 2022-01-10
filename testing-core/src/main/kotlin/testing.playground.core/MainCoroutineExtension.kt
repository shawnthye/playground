package testing.playground.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
    val testScope: TestScope = TestScope(testDispatcher),
) : BeforeEachCallback,
    AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}

fun MainCoroutineExtension.runTest(
    block: suspend TestScope.() -> Unit,
) = this.testScope.runTest {
    block()
}

/**
 * Creates a new [CoroutineScope] with the rule's testDispatcher
 */
@Suppress("unused", "TestFunctionName")
fun MainCoroutineExtension.CoroutineScope(): CoroutineScope = testScope
