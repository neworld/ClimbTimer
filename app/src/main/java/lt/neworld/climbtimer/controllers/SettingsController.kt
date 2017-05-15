package lt.neworld.climbtimer.controllers

import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import java.net.URL
import java.util.*

/**
 * @author Andrius Semionovas
 * @since 2017-05-15
 */
class SettingsController : Initializable {
    override fun initialize(location: URL, resources: ResourceBundle?) {

    }

    fun start() {
        TimerController.newStage().show()
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