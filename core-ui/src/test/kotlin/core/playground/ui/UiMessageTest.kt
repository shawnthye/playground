package core.playground.ui

import org.junit.jupiter.api.Test

// TODO: added Junit4 and Vintage engine
internal class UiMessageTest {

    @Test
    fun addition_isCorrect() {
        // 16:9
        // 1280×720

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
