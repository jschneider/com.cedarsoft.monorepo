package com.cedarsoft.commons.javafx

import javafx.beans.binding.Bindings
import javafx.beans.binding.StringBinding
import javafx.beans.property.StringProperty
import org.apache.commons.lang3.StringUtils
import java.util.concurrent.Callable

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */


/**
 * Returns a string binding with a max length for a string property
 */
fun StringProperty.maxLength(maxLength: Int): StringBinding {
  return Bindings.createStringBinding(Callable {
    val string = this.get()

    return@Callable StringUtils.abbreviate(string, maxLength)
  }, this)
}
