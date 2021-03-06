package lt.neworld.climbtimer.controllers

import javafx.animation.AnimationTimer
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.Duration
import lt.neworld.climbtimer.AppProperties
import lt.neworld.climbtimer.extensions.startAnimationTimer
import lt.neworld.climbtimer.utils.Sounds
import lt.neworld.climbtimer.utils.Timer
import views.ImageSliderView
import java.io.File
import java.net.URL
import java.util.*

/**
 * @author Andrius Semionovas
 * *
 * @since 2017-05-01
 */
class TimerController : Initializable {

    @FXML
    private lateinit var clock: Label

    @FXML
    private lateinit var title: Label

    @FXML
    private lateinit var logoLeft: ImageSliderView

    @FXML
    private lateinit var logoRight: ImageSliderView

    private val timer = Timer(
            runTime = AppProperties.runTime,
            waitTime = AppProperties.waitTime,
            warningTime = AppProperties.warningTime
    )
    private var animationTimer: AnimationTimer? = null

    override fun initialize(location: URL, resources: ResourceBundle?) {
        fun toImage(file: File) = Image(file.toURI().toString())

        title.text = AppProperties.title

        with(logoLeft) {
            images = AppProperties.logoLeft.map(::toImage)
            interval = Duration.millis(AppProperties.animationInterval.toDouble())
        }

        with(logoRight) {
            images = AppProperties.logoRight.map(::toImage)
            interval = Duration.millis(AppProperties.animationInterval.toDouble())
        }

        showTimer()
    }


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

    private fun adjustFont(relativeSize: Int) {
        val font = clock.font
        clock.font = Font(font.name, font.size + relativeSize)
    }

    private fun showTimer() {
        val state = timer.state
        val min = state.left / (1000 * 60)
        val sec = state.left % (1000 * 60) / 1000
        val ms = state.left % (1000) / 100
        clock.text = "%02d:%02d:%d".format(min, sec, ms)

        clock.textFill = when (state.status) {
            Timer.Status.WAITING -> AppProperties.colorOfWaitTime
            Timer.Status.WARNING -> AppProperties.colorOfWarning
            else -> AppProperties.colorOfRunTime
        }

        when (state.event) {
            Timer.Event.START -> Sounds.playStart()
            Timer.Event.LAST_MINUTE -> Sounds.playLastMinute()
            Timer.Event.LAST_SECONDS -> Sounds.playLastSeconds()
            Timer.Event.FINISH -> Sounds.playFinish()
        }
    }

    companion object {
        fun newScene(): Scene {
            val loader = FXMLLoader(TimerController::class.java.getResource("timer.fxml"))
            val root: Parent = loader.load()
            val controller: TimerController = loader.getController()
            return Scene(root).apply {
                setOnKeyPressed {
                    when (it.code) {
                        KeyCode.ESCAPE -> {
                            controller.stop()
                            (window as Stage).close()
                        }
                        KeyCode.SPACE -> controller.start()
                        KeyCode.PLUS, KeyCode.EQUALS -> controller.adjustFont(1)
                        KeyCode.MINUS -> controller.adjustFont(-1)
                    }
                }
            }
        }

        fun newStage(): Stage {
            return Stage().apply {
                title = "Timer"
                scene = newScene()
                isFullScreen = true
            }
        }
    }
}
