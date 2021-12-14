package core.playground.domain

import kotlinx.coroutines.CoroutineDispatcher
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import testing.playground.core.CoroutineTest

internal class UseCaseTest : CoroutineTest() {

    @Test
    fun `result return Result#Success`() = runBlockingTest {
        val useCase = object : UseCase<Unit, Unit>(testDispatcher) {
            override suspend fun execute(params: Unit) {
            }
        }
        val result = useCase(Unit)
        assertThat(result, `is`(instanceOf(Result.Success::class.java)))
    }

    @Test
    fun `exception return Result#Error`() = runBlockingTest {
        val useCase = ExceptionUseCase(testDispatcher)
        val result = useCase(Unit)
        assertThat(result, `is`(instanceOf(Result.Error::class.java)))
    }

    inner class ExceptionUseCase(
        coroutineDispatcher: CoroutineDispatcher,
    ) : UseCase<Unit, Unit>(coroutineDispatcher) {
        override suspend fun execute(params: Unit) {
            throw Exception("Test exception")
        }
    }
}
