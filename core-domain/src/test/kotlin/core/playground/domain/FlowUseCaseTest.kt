package core.playground.domain

/* ktlint-disable import-ordering */
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import testing.playground.core.CoroutineTest

internal class FlowUseCaseTest : CoroutineTest() {

    @Test
    fun `exception emits Result#Error`() = runTest {
        val useCase = ExceptionUseCase(testDispatcher)
        val result = useCase(Unit)
        assertThat(result.last(), `is`(instanceOf(Result.Error::class.java)))
    }

    inner class ExceptionUseCase(
        coroutineDispatcher: CoroutineDispatcher,
    ) : FlowUseCase<Unit, Unit>(coroutineDispatcher) {
        override fun execute(params: Unit): Flow<Result<Unit>> = flow {
            throw Exception("Test exception")
        }
    }
}
