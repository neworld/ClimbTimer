package lt.neworld.climbtimer.controllers

import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ColorPicker
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.image.ImageView
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Region.USE_COMPUTED_SIZE
import javafx.scene.layout.RowConstraints
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Window
import lt.neworld.climbtimer.AppProperties
import lt.neworld.climbtimer.extensions.loadImage
import lt.neworld.climbtimer.extensions.msToSec
import lt.neworld.climbtimer.extensions.partition
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
    @FXML
    private lateinit var colorRunTime: ColorPicker
    @FXML
    private lateinit var colorWaitTime: ColorPicker
    @FXML
    private lateinit var colorWarningTime: ColorPicker
    @FXML
    private lateinit var title: TextField
    @FXML
    private lateinit var hotkeys: GridPane
    @FXML
    private lateinit var logoLeftView: GridPane
    @FXML
    private lateinit var logoRightView: GridPane

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
        fillHotkeys()

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
        }.firstOrNull()

        logoFileChooser.initialDirectory = initialFileName?.parentFile ?: workingDir
        logoFileChooser.initialFileName = initialFileName?.path

        val newFile = logoFileChooser.showOpenMultipleDialog(window)

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
        fun makePathText(files: List<File>): String {
            return when (files.count()) {
                0 -> ""
                1 -> files.first().path
                else -> "${files.count()} files"
            }
        }

        logoLeft.text = makePathText(AppProperties.logoLeft)
        logoRight.text = makePathText(AppProperties.logoRight)

        logoLeftView.fillImages(AppProperties.logoLeft)
        logoRightView.fillImages(AppProperties.logoRight)
    }

    fun start() {
        save()
        TimerController.newStage().show()
    }

    private fun save() {
        AppProperties.runTime = runningTime.text.toLong().secToMs()
        AppProperties.waitTime = waitingTime.text.toLong().secToMs()
        AppProperties.warningTime = warningTime.text.toLong().secToMs()

        AppProperties.colorOfRunTime = colorRunTime.value
        AppProperties.colorOfWaitTime = colorWaitTime.value
        AppProperties.colorOfWarning = colorWarningTime.value

        AppProperties.title = title.text
    }

    private fun load() {
        runningTime.text = AppProperties.runTime.msToSec().toString()
        waitingTime.text = AppProperties.waitTime.msToSec().toString()
        warningTime.text = AppProperties.warningTime.msToSec().toString()

        colorRunTime.value = AppProperties.colorOfRunTime
        colorWaitTime.value = AppProperties.colorOfWaitTime
        colorWarningTime.value = AppProperties.colorOfWarning

        title.text = AppProperties.title

        refreshSoundChoosers()
        refreshLogoChoosers()
    }

    private fun fillHotkeys() {
        hotkeys.rowConstraints += (0 until hotkeysList.size * 2 - 1).map {
            RowConstraints(8.0, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE)
        }

        hotkeysList.toList().zip(0 until hotkeysList.size).forEach { (pair, index) ->
            val (key, desc) = pair
            hotkeys.addRow(index * 2, Text(key), Text(desc))
        }
    }

    private fun GridPane.fillImages(images: List<File>) {
        val size = Math.ceil(Math.sqrt(images.count().toDouble())).toInt()

        children.clear()
        rowConstraints.clear()
        columnConstraints.clear()

        rowConstraints += (0 until size).map {
            RowConstraints().apply {
                percentHeight = 100.0 / size
            }
        }

        columnConstraints += (0 until size).map {
            ColumnConstraints().apply {
                percentWidth = 100.0 / size
            }
        }

        images
                .map { it.toURI() }
                .map {
                    ImageView().apply {
                        loadImage(it)
                        fitWidthProperty().bind(this@fillImages.widthProperty().divide(size))
                        fitHeightProperty().bind(this@fillImages.heightProperty().divide(size))
                    }
                }
                .partition(size)
                .withIndex()
                .forEach { (index, images) -> addRow(index + 0, *images.toList().toTypedArray()) }
    }

    companion object {
        fun newScene(window: Window): Scene {
            val loader = FXMLLoader(TimerController::class.java.getResource("settings.fxml"))
            val root: Parent = loader.load()
            val controller: SettingsController = loader.getController()
            controller.window = window
            return Scene(root)
        }

        private val hotkeysList = mapOf(
                "<esc>" to "close",
                "<space>" to "launch/reset",
                "+" to "grow timer",
                "-" to "shrink timer"
        )
    }
}