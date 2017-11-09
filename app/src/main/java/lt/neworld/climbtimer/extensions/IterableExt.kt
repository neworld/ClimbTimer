package lt.neworld.climbtimer.extensions

/**
 * @author Andrius Semionovas
 * @since 2017-11-09
 */

fun <T> Iterable<T>.partition(size: Int): Iterable<Iterable<T>> {
    return this.withIndex().groupBy { it.index / size }.values.map { it.map { it.value } }
}