package com.cedarsoft.unit.other

/**
 * Marks sorted values
 */
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Sorted(
  val value: Order = Order.ASC
)

