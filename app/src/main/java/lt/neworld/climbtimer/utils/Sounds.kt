package lt.neworld.climbtimer.utils

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import lt.neworld.climbtimer.AppProperties

/**
 * @author Andrius Semionovas
 * @since 2017-05-08
 */
object Sounds {
    private val startMedia by lazy {
        AppProperties.soundStart?.let { Media(it.toURI().toString()) }
    }
    private val finishMedia by lazy {
        AppProperties.soundFinish?.let { Media(it.toURI().toString()) }
    }
    private val lastMinuteMedia by lazy {
        AppProperties.soundLastMinute?.let { Media(it.toURI().toString()) }
    }
    private val lastSecondsMedia by lazy {
        AppProperties.soundLastSeconds?.let { Media(it.toURI().toString()) }
    }

    private fun play(media: Media?) {
        media ?: return
        MediaPlayer(media).play()
    }

    fun playStart() {
        play(startMedia)
    }

    fun playFinish() {
        play(finishMedia)
    }

    fun playLastMinute() {
        play(lastMinuteMedia)
    }

    fun playLastSeconds() {
        play(lastSecondsMedia)
    }
}