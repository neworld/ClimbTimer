package lt.neworld.climbtimer.utils

import org.junit.Test

/**
 * @author Andrius Semionovas
 * *
 * @since 2017-05-07
 */
class TimerRangesTest {
    @Test
    fun test() {
        assertConsistentRanges(Timer(1000, 0, 0))
        assertConsistentRanges(Timer(1000, 1000, 0))
        assertConsistentRanges(Timer(1000, 1000, 500))
        assertConsistentRanges(Timer(65_000, 1000, 500))
        assertConsistentRanges(Timer(65_000, 4000, 0))
    }

    fun assertConsistentRanges(timer: Timer) {
        val eventRanges = timer.eventRanges
        val min = 0
        val max = timer.runTime + timer.warningTime
        for (time in min..max) {
            val ranges = eventRanges.filter { it.first.contains(time) }
            if (ranges.isEmpty()) {
                throw AssertionError("There is a space in $time millisecond. $timer has $eventRanges")
            } else if (ranges.size > 1) {
                throw AssertionError("There is overlaping ranges in $time millisecond ($ranges). $timer has $eventRanges")
            }
        }
    }
}