package lt.neworld.climbtimer

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

    var colorOfRunTime: Int by ColorField(PROP_COLOR_OF_RUN_TIME, 0xFFFFFF)
    var colorOfWaitTime: Int by ColorField(PROP_COLOR_OF_WAIT_TIME, 0x00FF00)
    var colorOfWarning: Int by ColorField(PROP_COLOR_OF_WARNING_TIME, 0xFF0000)

    class TimeField(key: String, default: Long) : Field<Long>(key, default) {
        override fun deserialize(raw: String): Long = raw.toLong() * 1000

        override fun serialize(value: Long): String = (value / 1000).toString()
    }

    class StringField(key: String, default: String) : Field<String>(key, default) {
        override fun deserialize(raw: String): String = raw

        override fun serialize(value: String): String = value
    }

    class ColorField(key: String, default: Int) : Field<Int>(key, default) {
        override fun deserialize(raw: String) = Integer.parseInt(raw, 16)

        override fun serialize(value: Int) = "%06X".format(value)
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
