package lt.neworld.climbtimer

import javafx.application.Application
import javafx.stage.Stage

/**
 * @author Andrius Semionovas
 * @since 2017-05-01
 */
class Main : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Climb timer"
        primaryStage.scene = MainController.newScene()
        primaryStage.isFullScreen = true
        primaryStage.show()
    }
}

fun main(vararg args: String) {
    Application.launch(Main::class.java, *args)
}