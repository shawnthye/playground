package core.playground

import core.playground.Strings.findExtension
import org.junit.jupiter.api.Test

class StringsTest {

    @Test
    fun addition_isCorrect() {
        val ext = "https://www.example.com/a/b/c/d/e/hello.gif".findExtension()
        assert(ext.isNullOrEmpty().not())
    }
}
