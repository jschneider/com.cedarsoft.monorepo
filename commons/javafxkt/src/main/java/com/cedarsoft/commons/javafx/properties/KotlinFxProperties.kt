package com.cedarsoft.commons.javafx.properties

import javafx.beans.binding.DoubleExpression
import javafx.beans.binding.IntegerExpression
import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableDoubleValue
import javafx.beans.value.WritableIntegerValue
import kotlin.reflect.KProperty

/**
 * Extension methods to allow simplified JavaFX properties
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
operator fun <T> ObservableValue<T>.getValue(thisRef: Any, property: KProperty<*>) = value

operator fun <T> Property<T>.setValue(thisRef: Any, property: KProperty<*>, value: T?) = setValue(value)


operator fun DoubleExpression.getValue(thisRef: Any, property: KProperty<*>) = value

operator fun WritableDoubleValue.setValue(thisRef: Any, property: KProperty<*>, value: Double?) = setValue(value)


operator fun IntegerExpression.getValue(thisRef: Any, property: KProperty<*>) = value

operator fun WritableIntegerValue.setValue(thisRef: Any, property: KProperty<*>, value: Int?) = setValue(value)
