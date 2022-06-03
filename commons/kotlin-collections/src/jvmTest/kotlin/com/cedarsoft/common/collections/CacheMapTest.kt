package com.cedarsoft.common.collections

import assertk.*
import assertk.assertions.*
import assertk.assertions.support.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 */
@Suppress("DEPRECATION")
class CacheMapTest {
  @Test
  fun test() {
    val freeLog = arrayListOf<String>()
    val cache = Cache<String, Int>(maxSize = 2) { k, v ->
      freeLog += "$k:$v"
    }
    cache["a"] = 1
    cache["b"] = 2
    cache["c"] = 3
    assertEquals("{b=2, c=3}", cache.toString())
    assertEquals("a:1", freeLog.joinToString(", "))

    assertEquals(false, "a" in cache)
    assertEquals(true, "b" in cache)
    assertEquals(true, "c" in cache)

    assertEquals(2, cache.getOrStore("b") { 20 })
    assertEquals(10, cache.getOrStore("a") { 10 })
    assertEquals(3, cache.getOrStore("d") { 3 })

    cache["aa"] = 1
    cache["bb"] = 2
    cache["cc"] = 3

    assertEquals("{bb=2, cc=3}", cache.toString())
  }

  @Test
  fun testRemoveIf() {
    val freeLog = arrayListOf<String>()
    val cache = Cache<String, Int>(maxSize = 4) { k, v ->
      "$k:$v"
    }

    assertThat(freeLog).isEmpty()

    cache["a"] = 1
    cache["b"] = 2
    cache["c"] = 3
    cache["d"] = 4

    assertThat(cache).hasSize(4)
    assertThat(cache["a"]).isEqualTo(1)
    assertThat(cache["b"]).isEqualTo(2)
    assertThat(cache["c"]).isEqualTo(3)

    cache.removeIf {
      it == "a" || it == "c"
    }

    assertThat(cache).hasSize(2)

    assertThat(cache["a"]).isNull()
    assertThat(cache["b"]).isEqualTo(2)
    assertThat(cache["c"]).isNull()
  }
}

private fun Assert<Cache<*, *>>.hasSize(expectedSize: Int) = given { actual ->
  if (actual.size == expectedSize) return
  expected("size to be $expectedSize was: ${show(actual.size)} for ${show(actual)}")
}

