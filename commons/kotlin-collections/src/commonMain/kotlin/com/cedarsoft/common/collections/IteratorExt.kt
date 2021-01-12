package com.cedarsoft.common.collections

fun <T> Iterator(hasNext: () -> Boolean, next: () -> T): Iterator<T> = object : Iterator<T> {
  override fun hasNext(): Boolean = hasNext()
  override fun next(): T = next()
}

