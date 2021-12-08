package core.playground.domain

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import testing.playground.core.CoroutineTest

internal class ResultTest : CoroutineTest() {

    @Test
    fun `Result succeeded`() {
        val success: Result<*> = Result.Success(Unit)
        assertThat(success.succeeded, `is`(true))

        val nullLoading: Result<*> = Result.Loading<String>()
        assertThat(nullLoading.succeeded, `is`(false))

        val cachedLoading: Result<*> = Result.Loading(Unit)
        assertThat(cachedLoading.succeeded, `is`(false))

        val nullError: Result<*> = Result.Error<Unit>(Throwable("error"))
        assertThat(nullError.succeeded, `is`(false))

        val cachedError: Result<*> = Result.Error(Throwable("error"), Unit)
        assertThat(cachedError.succeeded, `is`(false))
    }

    @Test
    fun `Result data`() {
        val success: Result<*> = Result.Success(Unit)
        assertThat(success.data, `is`(notNullValue()))

        val nullLoading: Result<*> = Result.Loading<String>(null)
        assertThat(nullLoading.data, `is`(nullValue()))

        val cachedLoading: Result<*> = Result.Loading(Unit)
        assertThat(cachedLoading.data, `is`(notNullValue()))

        val nullError: Result<*> = Result.Error(Throwable("error"), null)
        assertThat(nullError.data, `is`(nullValue()))

        val cachedError: Result<*> = Result.Error(Throwable("error"), Unit)
        assertThat(cachedError.succeeded, `is`(notNullValue()))
    }

    @Test
    fun `Result successOr`() {
        val success: Result<String> = Result.Success("success")
        assertThat(success.successOr(Unit), `is`("success"))

        val nullLoading: Result<String> = Result.Loading<String>(null)
        assertThat(nullLoading.successOr(Unit), `is`(Unit))

        val cacheLoading: Result<*> = Result.Loading("cached")
        assertThat(cacheLoading.successOr(Unit), `is`(Unit))
        assertThat(cacheLoading.successOr(null), `is`(nullValue()))

        val nullError: Result<*> = Result.Error(Throwable("error"), null)
        assertThat(nullError.successOr(Unit), `is`(Unit))
        assertThat(nullError.successOr(null), `is`(nullValue()))

        val cachedError: Result<*> = Result.Error(Throwable("error"), "cached")
        assertThat(cachedError.successOr(Unit), `is`(Unit))
        assertThat(cachedError.successOr(null), `is`(nullValue()))
    }

    @Test
    fun `Result toString`() {
        val success: Result<*> = Result.Success(Unit)
        assertThat("$success".isBlank(), `is`(false))

        val loading: Result<*> = Result.Loading<String>(null)
        assertThat("$loading", `is`(notNullValue()))

        val error: Result<*> = Result.Error(Throwable("error"), null)
        assertThat("$error", `is`(notNullValue()))
    }

    @Test
    fun `Result mapLatestError`() = runBlockingTest {

        val successFlow = flow<Result<*>> {
            emit(Result.Success(Any()))
        }.mapOnError { it }

        assertThat(successFlow.lastOrNull(), `is`(nullValue()))

        val errorFlow = flow<Result<*>> {
            emit(Result.Error(Throwable(message = null), Any()))
        }.mapOnError {
            it
        }

        assertThat(errorFlow.last(), `is`(instanceOf(Throwable::class.java)))
    }
}
