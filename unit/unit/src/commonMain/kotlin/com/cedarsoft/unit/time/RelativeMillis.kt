package com.cedarsoft.unit.time

import com.cedarsoft.unit.Name
import com.cedarsoft.unit.Symbol
import com.cedarsoft.unit.Unit
import com.cedarsoft.unit.other.Relative
import com.cedarsoft.unit.prefix.milli
import com.cedarsoft.unit.quantity.Time
import com.cedarsoft.unit.si.ms
import com.cedarsoft.unit.si.s

/**
 * Represents a *relative* time
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Retention(AnnotationRetention.SOURCE)
@Target(
  AnnotationTarget.CLASS,
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.TYPE_PARAMETER,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.FIELD,
  AnnotationTarget.LOCAL_VARIABLE,
  AnnotationTarget.VALUE_PARAMETER,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.TYPE,
  AnnotationTarget.EXPRESSION,
  AnnotationTarget.FILE,
  AnnotationTarget.TYPEALIAS
)
@MustBeDocumented
@Suppress("ClassName")
@Unit
@Time
@Symbol(ms.SYMBOL)
@Name("relative milliseconds")
@milli(s::class)
@Relative
annotation class RelativeMillis {
  companion object {
    const val SYMBOL: String = "ms"
  }
}

