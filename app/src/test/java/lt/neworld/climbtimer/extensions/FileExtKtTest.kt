package lt.neworld.climbtimer.extensions

import org.junit.Assert
import org.junit.Test
import java.io.File

/**
 * @author Andrius Semionovas
 * *
 * @since 2017-05-24
 */
class FileExtKtTest {
    @Test
    fun relativeOrAbsolute() {
        val other = File("/bar")

        Assert.assertEquals(File("/foo"), File("/foo").relativeOrAbsolute(other))
        Assert.assertEquals(File("/foo/bar"), File("/foo/bar").relativeOrAbsolute(other))
        Assert.assertEquals(File("foo"), File("/bar/foo").relativeOrAbsolute(other))
    }
}