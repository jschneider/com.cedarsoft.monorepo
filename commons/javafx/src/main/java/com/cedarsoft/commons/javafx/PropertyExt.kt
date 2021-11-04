package com.cedarsoft.commons.javafx

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty


/**
 * Wraps this in a simple object property
 */
fun <T> T.asProperty(): ObjectProperty<T> {
  return SimpleObjectProperty(this)
}

fun Double.asProperty(): DoubleProperty {
  return SimpleDoubleProperty(this)
}
