package core.playground

import core.playground.Strings.findExtension
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class StringsTest {

    @Test
    fun `valid extension`() {
        val ext = "https://www.example.com/a/b/c/d/e/hello.gif".findExtension()
        assertThat(ext, `is`("gif"))
    }

    @Test
    fun `invalid url`() {
        val ext = "www.example.com/a/b/c/d/e/hello.gif".findExtension()
        assertThat(ext, `is`(nullValue()))
    }

    @Test
    fun `invalid without path segment`() {
        val ext = "https://www.example.com".findExtension()
        assertThat(ext, `is`(nullValue()))
    }

    @Test
    fun `invalid without path segment with ended slash`() {
        val ext = "https://www.example.com/".findExtension()
        assertThat(ext, `is`(nullValue()))
    }

    @Test
    fun `invalid with path segment`() {
        val ext = "https://www.example.com/a/b/c/d/e".findExtension()
        assertThat(ext, `is`(nullValue()))
    }
}
