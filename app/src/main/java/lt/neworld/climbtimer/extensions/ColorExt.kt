package lt.neworld.climbtimer.extensions

import javafx.scene.paint.Color

/**
 * @author Andrius Semionovas
 * @since 2017-05-24
 */

fun Color.toHex(): String {
    return "%02X%02X%02X".format((red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())
}
