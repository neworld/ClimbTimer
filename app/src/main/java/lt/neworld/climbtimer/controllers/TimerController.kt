package lt.neworld.climbtimer.controllers

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
class TimerController {

    companion object {
        fun newScene(): Scene {
            val root: Parent = FXMLLoader.load(TimerController.javaClass.getResource("timer.fxml"))
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
