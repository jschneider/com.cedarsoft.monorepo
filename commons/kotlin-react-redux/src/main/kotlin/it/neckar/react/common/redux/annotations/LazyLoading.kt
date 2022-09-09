package it.neckar.react.common.redux.annotations

/**
 * Marks values within the store that are loaded lazily.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
annotation class LazyLoading(
  val description: String = "",
)
