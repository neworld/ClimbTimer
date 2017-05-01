package lt.neworld.climbtimer

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * @author Andrius Semionovas
 * @since 2017-05-01
 */
class Main : Application() {
    override fun start(primaryStage: Stage) {
        val root: Parent = FXMLLoader.load(javaClass.getResource("main.fxml"))
        primaryStage.title = "Climb timer"
        primaryStage.scene = Scene(root, 600.0, 300.0)
        primaryStage.show()
    }
}

fun main(vararg args: String) {
    Application.launch(Main::class.java, *args)
}