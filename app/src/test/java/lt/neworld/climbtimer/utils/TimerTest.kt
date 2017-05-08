package lt.neworld.climbtimer.utils

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import lt.neworld.climbtimer.utils.Timer.Event.*
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
        val state = fixture.state

        assertEquals(STOPPED, state.status)
        assertEquals(runTime, state.left)
    }

    @Test
    fun start_statusShouldBeRunning() {
        fixture.start()

        assertEquals(Timer.Status.RUNNING, fixture.state.status)
    }

    @Test
    fun start_leftShouldBeAsInterval() {
        fixture.start()

        val state = fixture.state
        assertEquals(RUNNING, state.status)
        assertEquals(runTime, state.left)

    }

    @Test
    fun afterSecond_leftShouldBeOneSecondLess() {
        fixture.start()

        whenever(clock.millis()).thenReturn(1000)

        val state = fixture.state
        assertEquals(RUNNING, state.status)
        assertEquals(runTime - 1000, state.left)
    }

    @Test
    fun afterInterval_leftShouldBeInterval() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime + waitTime)

        val state = fixture.state
        assertEquals(RUNNING, state.status)
        assertEquals(runTime, state.left)
    }

    @Test
    fun afterOneSecondAfterInterval_leftShouldBeOneSecondLessThanInterval() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime + waitTime + 1000)

        val state = fixture.state
        assertEquals(RUNNING, state.status)
        assertEquals(runTime - 1000, state.left)
    }

    @Test
    fun afterRunTime_leftShouldBeOneSecondLessThanInterval() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime)

        val state = fixture.state
        assertEquals(WAITING, state.status)
        assertEquals(waitTime, state.left)
    }

    @Test
    fun inTheWarning() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime - warningTime)

        val state = fixture.state
        assertEquals(WARNING, state.status)
        assertEquals(warningTime, state.left)
    }

    @Test
    fun start_startEvent() {
        fixture.start()

        assertEquals(START, fixture.state.event)
    }

    @Test
    fun tenSecondAfterStart_startEvent() {
        fixture.start()

        whenever(clock.millis()).thenReturn(10)

        assertEquals(START, fixture.state.event)
    }

    @Test
    fun sameTimeNextEventAfterStart_noEvent() {
        fixture.start()

        fixture.state

        assertEquals(null, fixture.state.event)
    }

    @Test
    fun MoveThroughLastMinute_lastMinuteEvent() {
        val fixture = Timer(65_000L, 0L, 0L, clock)

        fixture.start()

        fixture.state.status
        whenever(clock.millis()).thenReturn(5001)

        assertEquals(LAST_MINUTE, fixture.state.event)
    }

    @Test
    fun moveThroughOverWarningTime_lastSecondEvent() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime - warningTime)
        fixture.state
        whenever(clock.millis()).thenReturn(runTime - warningTime + 1)

        assertEquals(LAST_SECONDS, fixture.state.event)
    }

    @Test
    fun moveThroughLastSecond_lastSecondEvent() {
        val fixture = Timer(65_000L, 0L, 5000L, clock)
        fixture.start()

        whenever(clock.millis()).thenReturn(63_000L)
        fixture.state
        whenever(clock.millis()).thenReturn(64_000L)

        assertEquals(LAST_SECONDS, fixture.state.event)
    }

    @Test
    fun moveThroughTheEnd_finishEvent() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime)
        fixture.state
        whenever(clock.millis()).thenReturn(runTime + 1)

        assertEquals(FINISH, fixture.state.event)
    }

    @Test
    fun moveThroughNewCycle_startEvent() {
        fixture.start()

        whenever(clock.millis()).thenReturn(runTime + warningTime - 1)
        fixture.state
        whenever(clock.millis()).thenReturn(runTime + warningTime)

        assertEquals(START, fixture.state.event)
    }

    @Test
    fun notStarted_noEvent() {
        fixture.state

        assertEquals(null, fixture.state.event)
    }
}
