package lt.neworld.climbtimer

import javafx.application.Application
import javafx.stage.Stage
import lt.neworld.climbtimer.controllers.SettingsController
import lt.neworld.climbtimer.utils.UncaughtExceptionHandlerLogger
import java.io.File


/**
 * @author Andrius Semionovas
 * @since 2017-05-01
 */
class Main : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Climb timer"
        primaryStage.scene = SettingsController.newScene(primaryStage)
        primaryStage.show()
    }
}

private const val ARG_SKIP_CRASH_LOGGING = "--skip-crash-logging"

fun main(vararg args: String) {
    if (!args.contains(ARG_SKIP_CRASH_LOGGING)) {
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandlerLogger(File("."), defaultUncaughtExceptionHandler))
    }

    Application.launch(Main::class.java, *args)
}
