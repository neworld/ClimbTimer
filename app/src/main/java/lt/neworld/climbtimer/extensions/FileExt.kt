package lt.neworld.climbtimer.extensions

import java.io.File

/**
 * @author Andrius Semionovas
 * @since 2017-05-24
 */

fun File.relativeOrAbsolute(other: File): File {
    return if (startsWith(other.canonicalFile)) {
        relativeTo(other.canonicalFile)
    } else {
        this
    }
}