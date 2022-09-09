package it.neckar.react.common.annotations

/**
 * Marker interface for methods that use hooks (or might use hooks).
 * These methods must only be called from read components
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
annotation class UsesHooks()
