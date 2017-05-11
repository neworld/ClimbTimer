package lt.neworld.climbtimer.extensions

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.net.URI

/**
 * @author Andrius Semionovas
 * @since 2017-05-11
 */

fun ImageView.loadImage(uri: URI?) {
    uri ?: return
    this.image = Image(uri.toURL().toString())
}