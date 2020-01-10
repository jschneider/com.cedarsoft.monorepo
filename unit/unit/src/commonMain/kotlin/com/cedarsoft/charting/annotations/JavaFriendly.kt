package com.cedarsoft.charting.annotations

import com.cedarsoft.unit.other.px

/**
 * Marks Kotlin code as Java-friendly.
 *
 * Such code might seem unnecessary but simplifies Java calls into Kotlin code.
 *
 * @author Christian Erbelding ([ce@cedarsoft.com](mailto:ce@cedarsoft.com))
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
@px
annotation class JavaFriendly
