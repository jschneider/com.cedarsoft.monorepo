package it.neckar.open.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

@Suppress("DEPRECATION")
class CacheTest {
  @Test
  fun testCacheTypes() {
    Cache<Int, String>().getOrStore(7) {
      "7"
    }.let {
      assertThat(it).isEqualTo("7")
    }

    Cache<Int, String?>().getOrStore(7) {
      null
    }.let {
      assertThat(it).isNull()
    }

    Cache<Int, String?>().getOrStore(7) {
      "7"
    }.let {
      assertThat(it).isEqualTo("7")
    }
  }

  @Test
  internal fun testRemove() {
    val cache = Cache<Int, String>(1)

    cache.store(1, "1")
    assertThat(cache[1]).isEqualTo("1")

    cache.remove(1)
    assertThat(cache[1]).isNull()

    assertThat(cache.size).isEqualTo(0)

    cache.store(2, "1")
    cache.store(3, "1")
    cache.store(4, "1")
  }

  @Test
  internal fun testCache() {
    val cache = Cache<Int, String>(4)
    assertThat(cache.maxSize).isEqualTo(4)

    assertThat(cache[7]).isNull()

    cache.apply {
      store(7, "asdf")
      assertThat(get(7)).isEqualTo("asdf")
    }
  }

  @Test
  internal fun testEviction() {
    val cache = Cache<Int, String>(4)
    cache[0] = "0"
    assertThat(cache.size).isEqualTo(1)

    cache[1] = "1"
    assertThat(cache.size).isEqualTo(2)

    cache[2] = "2"
    assertThat(cache.size).isEqualTo(3)

    cache[3] = "3"
    assertThat(cache.size).isEqualTo(4)

    cache[4] = "4"
    assertThat(cache.size).isEqualTo(4)
    assertThat(cache[0]).isNull()
    assertThat(cache[1]).isEqualTo("1")
    assertThat(cache[2]).isEqualTo("2")
    assertThat(cache[3]).isEqualTo("3")
    assertThat(cache[4]).isEqualTo("4")
  }

  @Test
  internal fun testGetOrStore() {
    val cache = Cache<Int, String>(4)

    assertThat(cache[0]).isNull()
    assertThat(cache.getOrStore(0) { "A" }).isEqualTo("A")
    assertThat(cache[0]).isEqualTo("A")
    assertThat(cache.getOrStore(0) { "B" }).isEqualTo("A")
  }
}
