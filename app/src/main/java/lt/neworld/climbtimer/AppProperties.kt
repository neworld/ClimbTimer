package lt.neworld.climbtimer

/**
 * @author Andrius Semionovas
 * @since 2017-05-05
 */
object AppProperties {
    var runTime: Int = 5 * 60 * 1000
    var waitTime: Int = 30 * 1000
    var closeTheEnd: Int = 5 * 1000
    var title: String = "Climb timer"

    var colorOfRunTime: Int = 0xFFFFFF
    var colorOfWaitTime: Int = 0x00FF00
    var colorOfCloseTheEnd: Int = 0xFF0000
}