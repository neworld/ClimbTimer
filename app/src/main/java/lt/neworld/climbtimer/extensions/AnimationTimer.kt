package lt.neworld.climbtimer.extensions

import javafx.animation.AnimationTimer

/**
 * @author Andrius Semionovas
 * @since 2017-05-04
 */

fun startAnimationTimer(handler: (now: Long) -> Unit): AnimationTimer {
    return object : AnimationTimer() {
        override fun handle(now: Long) {
            handler(now)
        }
    }.apply { start() }
}