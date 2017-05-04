package lt.neworld.climbtimer.controllers

import javafx.animation.AnimationTimer
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import lt.neworld.climbtimer.extensions.startAnimationTimer
import lt.neworld.climbtimer.utils.Timer

/**
 * @author Andrius Semionovas
 * *
 * @since 2017-05-01
 */
class TimerController {

    @FXML
    private lateinit var clock: Label

    private val timer = Timer(5000)
    private var animationTimer: AnimationTimer? = null

    private fun start() {
        stop()
        timer.start()
        animationTimer = startAnimationTimer {
            showTimer()
        }
    }

    private fun stop() {
        animationTimer?.stop()
    }

    private fun showTimer() {
        val state = timer.state
        val min = state.left / (1000 * 60)
        val sec = state.left % (1000 * 60) / 1000
        val ms = state.left % (1000) / 10
        clock.text = "%02d:%02d:%02d".format(min, sec, ms)
    }

    companion object {
        fun newScene(): Scene {
            val loader = FXMLLoader(TimerController.javaClass.getResource("timer.fxml"))
            val root: Parent = loader.load()
            val controller: TimerController = loader.getController()
            return Scene(root).apply {
                setOnKeyPressed {
                    when (it.code) {
                        KeyCode.ESCAPE -> {
                            controller.stop()
                            (window as Stage).close()
                        }
                        KeyCode.SPACE -> {
                            controller.start()
                        }
                    }
                }
            }
        }
    }
}
