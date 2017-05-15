package lt.neworld.climbtimer.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import lt.neworld.climbtimer.AppProperties
import lt.neworld.climbtimer.extensions.msToSec
import lt.neworld.climbtimer.extensions.secToMs
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
    }

    companion object {
        fun newScene(): Scene {
            val loader = FXMLLoader(TimerController::class.java.getResource("settings.fxml"))
            val root: Parent = loader.load()
            val controller: SettingsController = loader.getController()
            return Scene(root)
        }
    }
}