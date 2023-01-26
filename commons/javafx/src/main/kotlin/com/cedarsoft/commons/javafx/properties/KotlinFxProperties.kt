package com.cedarsoft.commons.javafx.properties

import javafx.beans.binding.DoubleExpression
import javafx.beans.binding.IntegerExpression
import javafx.beans.binding.LongExpression
import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableDoubleValue
import javafx.beans.value.WritableIntegerValue
import javafx.beans.value.WritableLongValue
import kotlin.reflect.KProperty

/**
 * Extension methods to allow simplified JavaFX properties
 */
operator fun <T> ObservableValue<T>.getValue(thisRef: Any, property: KProperty<*>): T = value

operator fun <T> Property<T>.setValue(thisRef: Any, property: KProperty<*>, value: T?): Unit = setValue(value)


operator fun DoubleExpression.getValue(thisRef: Any, property: KProperty<*>): Double = value

operator fun WritableDoubleValue.setValue(thisRef: Any, property: KProperty<*>, value: Double?): Unit = setValue(value)


operator fun LongExpression.getValue(thisRef: Any, property: KProperty<*>): Long = value

operator fun WritableLongValue.setValue(thisRef: Any, property: KProperty<*>, value: Long?): Unit = setValue(value)


operator fun IntegerExpression.getValue(thisRef: Any, property: KProperty<*>): Int = value

operator fun WritableIntegerValue.setValue(thisRef: Any, property: KProperty<*>, value: Int?): Unit = setValue(value)
