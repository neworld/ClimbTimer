package lt.neworld.climbtimer.utils

import java.time.Clock

/**
 * @author Andrius Semionovas
 * @since 2017-05-04
 *
 * @param runTime runTime between rounds in ms
 */
class Timer(
        private val runTime: Long,
        private val waitTime: Long,
        private val warningTime: Long,
        private val clock: Clock = Clock.systemUTC()
) {
    private var started = false
    private var startTime: Long = 0

    fun start() {
        started = true
        startTime = clock.millis()
    }

    val state: State
        get() {
            return if (started) {
                val status: Status
                val fullCycle = runTime + waitTime
                var left = fullCycle - (clock.millis() - startTime) % fullCycle

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

                State(status, left)
            } else {
                State(Status.STOPPED, runTime)
            }
        }

    data class State(val status: Status, val left: Long)

    enum class Status {
        STOPPED, RUNNING, WAITING, WARNING
    }
}