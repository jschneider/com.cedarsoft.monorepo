package com.cedarsoft.annotations

/**
 * Marks values that are loaded lazily.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
annotation class LazyLoading(
  val description: String = "",
)
