package lt.neworld.climbtimer.utils

import java.time.Clock

/**
 * @author Andrius Semionovas
 * @since 2017-05-04
 *
 * @param interval interval between rounds in ms
 */
class Timer(private val interval: Long, private val clock: Clock = Clock.systemUTC()) {
    private var started = false
    private var startTime: Long = 0

    fun start() {
        started = true
        startTime = clock.millis()
    }

    val state: State
        get() {
            return if (started) {
                var left = interval - (clock.millis() - startTime) % interval
                if (left == 0L) {
                    left = interval
                }
                State(Status.RUNNING, left)
            } else {
                State(Status.STOPPED, interval)
            }
        }

    data class State(val status: Status, val left: Long)

    enum class Status {
        STOPPED, RUNNING
    }
}