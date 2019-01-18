package com.cedarsoft.commons.tornadofx

import com.cedarsoft.commons.javafx.OneWayConverter
import com.sun.javafx.collections.ObservableSetWrapper
import javafx.beans.property.ObjectProperty
import javafx.beans.value.ObservableValue
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.control.Label
import tornadofx.*

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */

/**
 * Creates a label with a convert method
 */
inline fun <reified T> EventTarget.label(
  observable: ObservableValue<T>,
  graphicProperty: ObjectProperty<Node>? = null,
  crossinline convert: (T) -> String
): Label {

  val converter = object : OneWayConverter<T>() {
    override fun format(toFormat: T): String {
      if (toFormat is ObservableSetWrapper<*>) {
        println("Uups")
      }
      return convert(toFormat)
    }
  }

  return this@label.label(
    observable = observable,
    graphicProperty = graphicProperty,
    converter = converter
  )
}
