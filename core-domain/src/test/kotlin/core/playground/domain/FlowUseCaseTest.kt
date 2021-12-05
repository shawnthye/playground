package core.playground.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import testing.playground.core.MainCoroutineExtension

internal class FlowUseCaseTest {

    @JvmField
    @RegisterExtension
    val coroutineExtension = MainCoroutineExtension()

    @Test
    fun `exception emits Result#Error`() = coroutineExtension.runBlockingTest {
        val useCase = ExceptionUseCase(coroutineExtension.testDispatcher)
        val result = useCase(Unit)
        assertThat(result.last(), `is`(instanceOf(Result.Error::class.java)))
    }

    inner class ExceptionUseCase(
        coroutineDispatcher: CoroutineDispatcher,
    ) : FlowUseCase<Unit, Unit>(coroutineDispatcher) {
        override fun execute(parameters: Unit): Flow<Result<Unit>> = flow {
            throw Exception("Test exception")
        }
    }
}
