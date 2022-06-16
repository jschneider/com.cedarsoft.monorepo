package com.cedarsoft.common.kotlin.lang

actual inline fun <T> Any?.fastCastTo(): T {
  return this as T
}
