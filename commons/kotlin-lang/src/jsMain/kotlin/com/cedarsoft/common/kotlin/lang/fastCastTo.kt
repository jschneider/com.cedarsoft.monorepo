package com.cedarsoft.common.kotlin.lang

/**
 * Casts unchecked
 */
actual inline fun <T> Any?.fastCastTo(): T {
  return this.unsafeCast<T>()
}
