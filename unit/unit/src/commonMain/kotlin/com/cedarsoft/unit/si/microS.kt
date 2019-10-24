package com.cedarsoft.unit.si



import com.cedarsoft.unit.Name
import com.cedarsoft.unit.Symbol
import com.cedarsoft.unit.Unit
import com.cedarsoft.unit.prefix.micro
import com.cedarsoft.unit.quantity.Time

/**
 * micro seconds
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
@Symbol(microS.SYMBOL)
@Name("microsecond")
@micro(s::class)
annotation class microS {
  companion object {
    const val SYMBOL: String = "μs"
  }
}
