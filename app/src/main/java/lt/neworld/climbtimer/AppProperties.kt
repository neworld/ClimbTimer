package lt.neworld.climbtimer

import javafx.scene.paint.Color
import lt.neworld.climbtimer.extensions.relativeOrAbsolute
import lt.neworld.climbtimer.extensions.toHex
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
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

    private const val PROP_SOUND_START = "sound_start"
    private const val PROP_SOUND_LAST_MINUTE = "sound_last_minute"
    private const val PROP_SOUND_LAST_SECONDS = "sound_last_seconds"
    private const val PROP_SOUND_FINISH = "sound_finish"

    private const val PROP_LOGO_LEFT = "logo_left"
    private const val PROP_LOGO_RIGHT = "logo_right"

    private val file = File("climber_timer.properties")

    private val properties by lazy {
        Properties().apply {
            if (!file.exists()) {
                file.createNewFile()
            }
            val fin = FileInputStream(file)
            load(InputStreamReader(fin))
            fin.close()
        }
    }

    private fun save() {
        val fout = FileOutputStream(file)
        properties.store(fout, "")
        fout.close()
    }

    var runTime: Long by TimeField(PROP_RUN_TIME, 5 * 1000)
    var waitTime: Long by TimeField(PROP_WAIT_TIME, 1 * 1000)
    var warningTime: Long by TimeField(PROP_WARNING_TIME, 1 * 1000)

    var title: String by StringField(PROP_TITLE, "ClimbTimer")

    var colorOfRunTime: Color by ColorField(PROP_COLOR_OF_RUN_TIME, Color.web("FFFFFF"))
    var colorOfWaitTime: Color by ColorField(PROP_COLOR_OF_WAIT_TIME, Color.web("00FF00"))
    var colorOfWarning: Color by ColorField(PROP_COLOR_OF_WARNING_TIME, Color.web("FF0000"))

    var soundStart: File? by FileField(PROP_SOUND_START, null)
    var soundLastMinute: File? by FileField(PROP_SOUND_LAST_MINUTE, null)
    var soundLastSeconds: File? by FileField(PROP_SOUND_LAST_SECONDS, null)
    var soundFinish: File? by FileField(PROP_SOUND_FINISH, null)

    var logoLeft: File? by FileField(PROP_LOGO_LEFT, null)
    var logoRight: File? by FileField(PROP_LOGO_RIGHT, null)

    class TimeField(key: String, default: Long) : Field<Long>(key, default) {
        override fun deserialize(raw: String): Long = raw.toLong() * 1000

        override fun serialize(value: Long): String = (value / 1000).toString()
    }

    class StringField(key: String, default: String) : Field<String>(key, default) {
        override fun deserialize(raw: String): String = raw

        override fun serialize(value: String): String = value
    }

    class ColorField(key: String, default: Color) : Field<Color>(key, default) {
        override fun deserialize(raw: String) = Color.web(raw)!!

        override fun serialize(value: Color) = value.toHex()
    }

    class FileField(key: String, default: File?) : Field<File?>(key, default) {
        override fun deserialize(raw: String): File? {
            return if (raw.isNotBlank()) File(raw) else null
        }

        override fun serialize(value: File?): String {
            return value?.relativeOrAbsolute(File("."))?.path ?: ""
        }
    }

    abstract class Field<T>(
            val key: String,
            val default: T
    ) {
        operator fun getValue(thisRef: Any, property: KProperty<*>): T {
            return properties.getProperty(key, null)?.let(this::deserialize) ?: default
        }

        operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            properties[key] = value.let(this::serialize)
            save()
        }

        abstract fun deserialize(raw: String): T

        abstract fun serialize(value: T): String
    }
}
