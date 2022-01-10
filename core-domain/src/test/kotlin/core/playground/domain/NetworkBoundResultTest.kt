package core.playground.domain

import core.playground.data.Response
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import testing.playground.core.CoroutineTest

internal class NetworkBoundResultTest : CoroutineTest() {

    private var cache: Any? = null
    private val remote = Any()

    private val query = flow {
        if (cache != null) {
            emit(cache)
        }
    }

    private val response = flow {
        emit(Response.Success(remote))
    }.flowOn(testDispatcher)

    @AfterEach
    internal fun tearDown() {
        cache = null
    }

    @Test
    fun `success with cache`() = runTest {

        cache = Any()

        val networkBoundResult = response.asNetworkBoundResult(
            query = query,
        ) {
            cache = it
        }

        assertThat(networkBoundResult.take(2).last().data, `is`(cache))

        val result = networkBoundResult.last()
        assertThat(result.data, `is`(remote))
    }

    @Test
    fun `fetch without cache`() = runTest {

        val networkBoundResult = response.asNetworkBoundResult(
            query = query,
            shouldFetch = { true },
        ) {
            cache = it
        }

        val result = networkBoundResult.last()
        assertThat(result.data, `is`(remote))
    }

    @Test
    fun `should not fetch`() = runTest {

        cache = Any()

        val networkBoundResult = response.asNetworkBoundResult(
            query = query,
            shouldFetch = { false },
        ) {
            cache = it
        }

        val result = networkBoundResult.last()
        assertThat(result.data, `is`(cache))
    }
}
