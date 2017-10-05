package com.cedarsoft.unit.other

import java.lang.annotation.Inherited

/**
 * Marks sorted values
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
annotation class Sorted(
  val value: Order = Order.ASC
)

