package com.cearsoft.guava

import com.google.common.collect.ImmutableList

/**
 * Contains extension methods for guava collections
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

/**
 * Creates a new copy including the additional element
 */
fun <T> ImmutableList<T>.copyAndAdd(elementToAdd: T): ImmutableList<T> {
  return ImmutableList
    .builder<T>()
    .addAll(this)
    .add(elementToAdd)
    .build()
}
