package lt.neworld.climbtimer.utils

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock

/**
 * @author Andrius Semionovas
 * @since 2017-05-04
 */
class TimerTest {
    val clock: Clock = mock()
    val interval = 5000L
    val fixture = Timer(interval, clock)

    @Test
    fun state_statEqualsIntervalAndStopped() {
        val status = fixture.state

        assertEquals(Timer.State(Timer.Status.STOPPED, interval), status)
    }

    @Test
    fun start_statusShouldBeRunning() {
        fixture.start()

        assertEquals(Timer.Status.RUNNING, fixture.state.status)
    }

    @Test
    fun start_leftShouldBeAsInterval() {
        fixture.start()

        assertEquals(interval, fixture.state.left)
    }

    @Test
    fun afterSecond_leftShouldBeOneSecondLess() {
        fixture.start()

        whenever(clock.millis()).thenReturn(1000)

        assertEquals(interval - 1000, fixture.state.left)
    }

    @Test
    fun afterInterval_leftShouldBeInterval() {
        fixture.start()

        whenever(clock.millis()).thenReturn(interval)

        assertEquals(interval, fixture.state.left)
    }

    @Test
    fun afterOneSecondAfterInterval_leftShouldBeOneSecondLessThanInterval() {
        fixture.start()

        whenever(clock.millis()).thenReturn(interval + 1000)

        assertEquals(interval - 1000, fixture.state.left)
    }
}