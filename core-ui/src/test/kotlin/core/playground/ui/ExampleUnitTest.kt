package core.playground.ui

import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //16:9
        //1280×720

        val mod = Math.floorMod(1280, 720)

        println(mod) // 560

        // 720 / 1280 = 0.5625

        val gcd = gcd(16, 9)
        println(gcd)
    }

    fun gcd(a: Long, b: Long): Long {
        return if (a % b != 0L) gcd(b, a % b) else b
    }
}
