package lt.neworld.climbtimer.utils

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import lt.neworld.climbtimer.utils.Timer.State
import lt.neworld.climbtimer.utils.Timer.Status.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock

/**
 * @author Andrius Semionovas
 * @since 2017-05-04
 */
class TimerTest {
    val clock: Clock = mock()
    val runTime = 5000L
    val waitTime = 1000L
    val warningTime = 1000L
    val fixture = Timer(runTime, waitTime, warningTime, clock)

    @Test
    fun state_statEqualsIntervalAndStopped() {
        val status = fixture.state

        assertEquals(State(Timer.Status.STOPPED, runTime), status)
    }

    @Test
    fun start_statusShouldBeRunning() {
        fixture.start()

        assertEquals(Timer.Status.RUNNING, fixture.state.status)
    }

    @Test
    fun start_leftShouldBeAsInterval() {
        fixture.start()

        assertEquals(State(RUNNING, runTime), fixture.state)

    }

    @Test
    fun afterSecond_leftShouldBeOneSecondLess() {
        fixture.start()

        whenever(clock.millis()).thenReturn(1000)

        assertEquals(State(RUNNING, runTime - 1000), fixture.state)
    }

    @Test
    fun afterInterval_leftShouldBeInterval() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime + waitTime)

        assertEquals(State(RUNNING, runTime), fixture.state)
    }

    @Test
    fun afterOneSecondAfterInterval_leftShouldBeOneSecondLessThanInterval() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime + waitTime + 1000)

        assertEquals(State(RUNNING, runTime - 1000), fixture.state)
    }

    @Test
    fun afterRunTime_leftShouldBeOneSecondLessThanInterval() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime)

        assertEquals(State(WAITING, waitTime), fixture.state)
    }

    @Test
    fun inTheWarning() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime - warningTime)

        assertEquals(State(WARNING, warningTime), fixture.state)
    }
}
