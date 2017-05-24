package lt.neworld.climbtimer.extensions

import javafx.scene.paint.Color
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * @author Andrius Semionovas
 * *
 * @since 2017-05-24
 */
class ColorExtKtTest {
    @Test
    fun toHex() {
        val random = Random()

        for (i in 1..100) {
            val hex = "%06X".format(random.nextInt(0xFFFFFF))
            val color = Color.web(hex)
            assertEquals(hex, color.toHex())
        }
    }
}