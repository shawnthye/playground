package app.playground

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@DisplayName("Example unit test")
class ExampleUnitTest {

    @Test
    @DisplayName("addition is correct")
    fun additionIsCorrect() {
        assertThat(4, `is`(2 + 2))
    }
}
