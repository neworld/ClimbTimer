package lt.neworld.climbtimer

/**
 * @author Andrius Semionovas
 * @since 2017-05-05
 */
object AppProperties {
    var runTime: Long = 5 * 1000
    var waitTime: Long = 1 * 1000
    var warningTime: Long = 1 * 1000
    var title: String = "Climb timer"

    var colorOfRunTime: Int = 0xFFFFFF
    var colorOfWaitTime: Int = 0x00FF00
    var colorOfWarning: Int = 0xFF0000
}
