package lt.neworld.climbtimer.utils

import javafx.beans.property.Property
import kotlin.reflect.KProperty

/**
 * @author Andrius Semionovas
 * @since 2017-11-18
 */
class FxProperty<T>(private val property: Property<T>) {
    operator fun getValue(imageSliderView: Any?, property: KProperty<*>): T = this.property.value

    operator fun setValue(ref: Any?, property: KProperty<*>, value: T) {
        this.property.value = value
    }
}