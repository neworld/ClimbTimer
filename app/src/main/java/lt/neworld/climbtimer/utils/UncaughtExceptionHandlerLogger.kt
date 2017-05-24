package lt.neworld.climbtimer.utils

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Andrius Semionovas
 * @since 2017-05-24
 */
class UncaughtExceptionHandlerLogger(
        val dir: File,
        val decorated: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {
    private val currentTimeString: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            return dateFormat.format(Date())
        }

    override fun uncaughtException(t: Thread, e: Throwable) {
        val logFile = File(dir, "crash-$currentTimeString.txt")
        val writer = logFile.printWriter()
        e.printStackTrace(writer)
        writer.close()
        decorated?.uncaughtException(t, e)
    }
}
