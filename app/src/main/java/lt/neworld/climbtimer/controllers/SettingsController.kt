package lt.neworld.climbtimer.controllers

import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.stage.FileChooser
import javafx.stage.Window
import lt.neworld.climbtimer.AppProperties
import lt.neworld.climbtimer.extensions.msToSec
import lt.neworld.climbtimer.extensions.secToMs
import java.io.File
import java.net.URL
import java.util.*
import java.util.function.UnaryOperator

/**
 * @author Andrius Semionovas
 * @since 2017-05-15
 */
class SettingsController : Initializable {

    @FXML
    private lateinit var runningTime: TextField
    @FXML
    private lateinit var waitingTime: TextField
    @FXML
    private lateinit var warningTime: TextField
    @FXML
    private lateinit var soundStart: TextField
    @FXML
    private lateinit var soundLastMinute: TextField
    @FXML
    private lateinit var soundLastSeconds: TextField
    @FXML
    private lateinit var soundFinish: TextField
    @FXML
    private lateinit var logoRight: TextField
    @FXML
    private lateinit var logoLeft: TextField

    private val soundFileChooser = FileChooser().apply {
        extensionFilters.add(FileChooser.ExtensionFilter("Audio (*.wav, *.mp3)", "*.wav", "*.mp3"))
    }

    private val logoFileChooser = FileChooser().apply {
        extensionFilters.add(FileChooser.ExtensionFilter("Image (*.png, *.jpg, *.bmp)", "*.png", "*.jpg", "*.bmp"))
    }

    private val workingDir = File(".").canonicalFile
    private val soundsDir = File(workingDir, "sounds")

    private lateinit var window: Window

    override fun initialize(location: URL, resources: ResourceBundle?) {
        runningTime.textFormatter = createNumberFormatter()
        waitingTime.textFormatter = createNumberFormatter()
        warningTime.textFormatter = createNumberFormatter()

        load()
    }

    private fun createNumberFormatter(): TextFormatter<String> {
        return TextFormatter<String>(UnaryOperator<TextFormatter.Change> { change ->
            if (change.text.matches("[0-9]*".toRegex())) {
                change
            } else {
                change.clone().apply {
                    text = ""
                }
            }
        })
    }

    @FXML
    fun onSoundChooserClick(event: Event) {
        val initialFileName = when (event.source) {
            soundStart -> AppProperties.soundStart
            soundFinish -> AppProperties.soundFinish
            soundLastMinute -> AppProperties.soundLastMinute
            soundLastSeconds -> AppProperties.soundLastSeconds
            else -> throw RuntimeException("Unsupported source ${event.source}")
        }

        soundFileChooser.initialDirectory = initialFileName?.parentFile ?: soundsDir

        val newFile = soundFileChooser.showOpenDialog(window)

        when (event.source) {
            soundStart -> AppProperties.soundStart = newFile
            soundFinish -> AppProperties.soundFinish = newFile
            soundLastMinute -> AppProperties.soundLastMinute = newFile
            soundLastSeconds -> AppProperties.soundLastSeconds = newFile
            else -> throw RuntimeException("Unsupported source ${event.source}")
        }

        refreshSoundChoosers()
    }

    @FXML
    fun onLogoChooserClick(event: Event) {
        val initialFileName = when (event.source) {
            logoLeft -> AppProperties.logoLeft
            logoRight -> AppProperties.logoRight
            else -> throw RuntimeException("Unsupported source ${event.source}")
        }

        logoFileChooser.initialDirectory = initialFileName?.parentFile ?: workingDir

        val newFile = logoFileChooser.showOpenDialog(window)

        when (event.source) {
            logoLeft -> AppProperties.logoLeft = newFile
            logoRight -> AppProperties.logoRight = newFile
            else -> throw RuntimeException("Unsupported source ${event.source}")
        }

        refreshLogoChoosers()
    }

    private fun refreshSoundChoosers() {
        soundStart.text = AppProperties.soundStart?.path
        soundLastMinute.text = AppProperties.soundLastMinute?.path
        soundLastSeconds.text = AppProperties.soundLastSeconds?.path
        soundFinish.text = AppProperties.soundFinish?.path
    }

    private fun refreshLogoChoosers() {
        logoLeft.text = AppProperties.logoLeft?.path
        logoRight.text = AppProperties.logoRight?.path
    }

    fun start() {
        save()
        TimerController.newStage().show()
    }

    private fun save() {
        AppProperties.runTime = runningTime.text.toLong().secToMs()
        AppProperties.waitTime = waitingTime.text.toLong().secToMs()
        AppProperties.warningTime = warningTime.text.toLong().secToMs()
    }

    private fun load() {
        runningTime.text = AppProperties.runTime.msToSec().toString()
        waitingTime.text = AppProperties.waitTime.msToSec().toString()
        warningTime.text = AppProperties.warningTime.msToSec().toString()

        refreshSoundChoosers()
        refreshLogoChoosers()
    }

    companion object {
        fun newScene(window: Window): Scene {
            val loader = FXMLLoader(TimerController::class.java.getResource("settings.fxml"))
            val root: Parent = loader.load()
            val controller: SettingsController = loader.getController()
            controller.window = window
            return Scene(root)
        }
    }
}