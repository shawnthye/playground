package core.playground.domain

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ResultTest {

    @Test
    fun `Result succeeded`() {
        val success = Result.Success(Unit)
        assertThat(success.succeeded, `is`(true))

        val nullLoading = Result.Loading<String>(null)
        assertThat(nullLoading.succeeded, `is`(false))

        val cachedLoading = Result.Loading(Unit)
        assertThat(cachedLoading.succeeded, `is`(false))

        val nullError = Result.Error(Throwable("error"), null)
        assertThat(nullError.succeeded, `is`(false))

        val cachedError = Result.Error(Throwable("error"), Unit)
        assertThat(cachedError.succeeded, `is`(false))
    }

    @Test
    fun `Result data`() {
        val success = Result.Success(Unit)
        assertThat(success.data, `is`(notNullValue(Unit::class.java)))

        val nullLoading = Result.Loading<String>(null)
        assertThat(nullLoading.data, `is`(nullValue()))

        val cachedLoading = Result.Loading(Unit)
        assertThat(cachedLoading.data, `is`(notNullValue()))

        val nullError = Result.Error(Throwable("error"), null)
        assertThat(nullError.data, `is`(nullValue()))

        val cachedError = Result.Error(Throwable("error"), Unit)
        assertThat(cachedError.succeeded, `is`(notNullValue()))
    }

    @Test
    fun `Result successOr`() {
        val success = Result.Success(Unit)
        assertThat(success.successOr("fallback"), `is`(Unit))

        val nullLoading = Result.Loading<String>(null)
        assertThat(nullLoading.successOr(Unit), `is`(Unit))

        val cacheLoading = Result.Loading("cached")
        assertThat(cacheLoading.successOr(Unit), `is`(Unit))

        val nullError = Result.Error(Throwable("error"), null)
        assertThat(nullError.successOr(Unit), `is`(Unit))

        val cachedError = Result.Error(Throwable("error"), "cached")
        assertThat(cachedError.successOr(Unit), `is`(Unit))
    }
}
