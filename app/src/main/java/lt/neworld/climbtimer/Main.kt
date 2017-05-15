package lt.neworld.climbtimer

import javafx.application.Application
import javafx.stage.Stage
import lt.neworld.climbtimer.controllers.SettingsController
import lt.neworld.climbtimer.controllers.TimerController

/**
 * @author Andrius Semionovas
 * @since 2017-05-01
 */
class Main : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Climb timer"
        primaryStage.scene = SettingsController.newScene()
        primaryStage.show()
    }
}

fun main(vararg args: String) {
    Application.launch(Main::class.java, *args)
}