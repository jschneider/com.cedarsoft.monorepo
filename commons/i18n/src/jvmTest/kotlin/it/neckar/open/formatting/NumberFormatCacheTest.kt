package it.neckar.open.formatting

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class NumberFormatCacheTest {
  @Test
  fun testIt() {
    val cache = NumberFormatCache("NumberFormatCacheTest") {
      isGroupingUsed = true
      minimumIntegerDigits = 77
    }

    assertThat(cache[it.neckar.open.i18n.Locale.US]).isSameAs(cache[it.neckar.open.i18n.Locale.US])
    assertThat(cache[it.neckar.open.i18n.Locale.Germany]).isSameAs(cache[it.neckar.open.i18n.Locale.Germany])

    assertThat(cache[it.neckar.open.i18n.Locale.Germany]).isNotEqualTo(cache[it.neckar.open.i18n.Locale.US])

    assertThat(cache[it.neckar.open.i18n.Locale.Germany].minimumIntegerDigits).isEqualTo(77)
    assertThat(cache[it.neckar.open.i18n.Locale.US].minimumIntegerDigits).isEqualTo(77)
  }
}
