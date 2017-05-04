package lt.neworld.climbtimer

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.stage.Stage

/**
 * @author Andrius Semionovas
 * *
 * @since 2017-05-01
 */
class MainController {

    companion object {
        fun newScene(): Scene {
            val root: Parent = FXMLLoader.load(javaClass.getResource("main.fxml"))
            return Scene(root).apply {
                setOnKeyPressed {
                    when (it.code) {
                        KeyCode.ESCAPE -> (window as Stage).close()
                    }
                }
            }
        }
    }
}
