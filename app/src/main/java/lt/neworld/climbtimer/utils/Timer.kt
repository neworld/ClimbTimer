package lt.neworld.climbtimer.utils

import java.time.Clock

/**
 * @author Andrius Semionovas
 * @since 2017-05-04
 *
 * @param runTime runTime between rounds in ms
 */
class Timer(
        val runTime: Long,
        val waitTime: Long,
        val warningTime: Long,
        private val clock: Clock = Clock.systemUTC()
) {
    private var started = false
    private var startTime: Long = 0
    private var lastEventRange: LongRange? = null

    /**
     * Open for tests only. Don't use in production
     */
    val eventRanges: Set<Pair<LongRange, Event>> by lazy {
        mutableSetOf<Pair<LongRange, Event>>().apply {
            add(0L..waitTime - 1 to Event.FINISH)
            var curTime = waitTime
            while (curTime < waitTime + warningTime) {
                val upper = Math.min(curTime + SECOND_MS, waitTime + warningTime)
                add(curTime..upper - 1 to Event.LAST_SECOND)
                curTime = upper
            }

            if (runTime > LAST_MINUTE_MS) {
                add(waitTime + warningTime..waitTime + LAST_MINUTE_MS - 1 to Event.LAST_MINUTE)
                curTime = waitTime + LAST_MINUTE_MS
            }

            add(curTime..runTime + waitTime to Event.START)
        }
    }

    fun start() {
        started = true
        startTime = clock.millis()
    }

    val state: State
        get() {
            return if (started) {
                val status: Status

                var left = calcLeftInTheCycle()

                if (left == 0L) {
                    left = runTime
                }

                if (left > waitTime + warningTime) {
                    left -= waitTime
                    status = Status.RUNNING
                } else if (left > waitTime) {
                    left -= waitTime
                    status = Status.WARNING
                } else {
                    status = Status.WAITING
                }

                State(status, left, calcEvent())
            } else {
                State(Status.STOPPED, runTime, null)
            }
        }

    private fun calcLeftInTheCycle(curTime: Long = clock.millis()): Long {
        val fullCycle = runTime + waitTime
        val leftInTheCycle = fullCycle - (curTime - startTime) % fullCycle
        return leftInTheCycle
    }

    private fun calcEvent(): Event? {
        val leftInTheCycle = calcLeftInTheCycle()

        val (curRange, curEvent) = eventRanges.find { it.first.contains(leftInTheCycle) }!!
        if (curRange == lastEventRange) return null

        lastEventRange = curRange

        return curEvent
    }

    override fun toString(): String {
        return "Timer(runTime=$runTime, waitTime=$waitTime, warningTime=$warningTime)"
    }


    data class State(val status: Status, val left: Long, val event: Event?)

    enum class Status {
        STOPPED, RUNNING, WAITING, WARNING
    }

    enum class Event {
        START, LAST_MINUTE, LAST_SECOND, FINISH
    }

    private val LAST_MINUTE_MS = 60_000L
    private val SECOND_MS = 1_000L
}