package com.cedarsoft.guava

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet

/**
 * Contains extension methods for guava collections
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

/**
 * Converts a list to an immutable list
 */
fun <T> List<T>.toImmutable(): ImmutableList<T> {
  return ImmutableList.copyOf(this)
}

fun <T> List<T>.toImmutableSet(): ImmutableSet<T> {
  return ImmutableSet.copyOf(this)
}

/**
 * Converts a set to an immutable set
 */
fun <T> Set<T>.toImmutable(): ImmutableSet<T> {
  return ImmutableSet.copyOf(this)
}

/**
 * Converts a sequence to an immutable list
 */
fun <T> Sequence<T>.toImmutable(): ImmutableList<T> {
  val builder = ImmutableList.builder<T>()

  for (item in this) {
    builder.add(item)
  }

  return builder.build()
}

/**
 * Converts a sequence to an immutable set
 */
fun <T> Sequence<T>.toImmutableSet(): ImmutableSet<T> {
  val builder = ImmutableSet.builder<T>()

  for (item in this) {
    builder.add(item)
  }

  return builder.build()
}

/**
 * Converts to an immutable map
 */
fun <K, V> Map<K, V>.toImmutable(): ImmutableMap<K, V> {
  return ImmutableMap.copyOf(this)
}

fun <T> Collection<T>.toImmutableList(): ImmutableList<T> {
  return ImmutableList.copyOf(this)
}
