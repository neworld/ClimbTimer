package lt.neworld.climbtimer

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.reflect.KProperty

/**
 * @author Andrius Semionovas
 * @since 2017-05-05
 */
object AppProperties {
    private const val PROP_RUN_TIME = "run_time"
    private const val PROP_WAIT_TIME = "wait_time"
    private const val PROP_WARNING_TIME = "warning_time"
    private const val PROP_TITLE = "title"
    private const val PROP_COLOR_OF_RUN_TIME = "color_of_run_time"
    private const val PROP_COLOR_OF_WAIT_TIME = "color_of_wait_time"
    private const val PROP_COLOR_OF_WARNING_TIME = "color_of_warning_time"

    private val file = File("climber_timer.properties")

    private val properties by lazy {
        Properties().apply {
            if (!file.exists()) {
                file.createNewFile()
            }
            val fin = FileInputStream(file)
            load(fin)
            fin.close()
        }
    }

    private fun save() {
        val fout = FileOutputStream(file)
        properties.store(fout, "")
        fout.close()
    }

    var runTime: Long by Field(PROP_RUN_TIME, 5 * 1000, String::toLong, Long::toString)
    var waitTime: Long by Field(PROP_WAIT_TIME, 1 * 1000, String::toLong, Long::toString)
    var warningTime: Long by Field(PROP_WARNING_TIME, 1 * 1000, String::toLong, Long::toString)

    var title: String by Field(PROP_TITLE, "ClimbTimer", { it }, { it })

    var colorOfRunTime: Int by Field(PROP_COLOR_OF_RUN_TIME, 0xFFFFFF, String::toInt, Int::toString)
    var colorOfWaitTime: Int by Field(PROP_COLOR_OF_WAIT_TIME, 0x00FF00, String::toInt, Int::toString)
    var colorOfWarning: Int by Field(PROP_COLOR_OF_WARNING_TIME, 0xFF0000, String::toInt, Int::toString)

    class Field<T>(
            val key: String,
            val default: T,
            val reader: (String) -> T,
            val writer: (T) -> String
    ) {
        operator fun getValue(thisRef: Any, property: KProperty<*>): T {
            return properties.getProperty(key, null)?.let(reader) ?: default
        }

        operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            properties[key] = value.let(writer)
            save()
        }
    }
}
