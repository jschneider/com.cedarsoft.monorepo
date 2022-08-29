package com.cedarsoft.commons.javafx

import javafx.beans.Observable
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList


/**
 * Wraps this in a simple object property
 */
fun <T> T.asProperty(): ObjectProperty<T> {
  return SimpleObjectProperty(this)
}

fun Double.asProperty(): DoubleProperty {
  return SimpleDoubleProperty(this)
}

fun Int.asProperty(): IntegerProperty {
  return SimpleIntegerProperty(this)
}

fun String.asProperty(): StringProperty {
  return SimpleStringProperty(this)
}

/**
 * Creates a new observable list for the given list
 */
fun <T> List<T>.asObservableList(extractor: (T) -> List<Observable>): ObservableList<T> {
  return FXCollections.observableList(this) {
    extractor(it).toTypedArray()
  }
}
