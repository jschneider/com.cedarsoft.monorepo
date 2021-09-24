package com.cedarsoft.formatting

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

    assertThat(cache[com.cedarsoft.i18n.Locale.US]).isSameAs(cache[com.cedarsoft.i18n.Locale.US])
    assertThat(cache[com.cedarsoft.i18n.Locale.Germany]).isSameAs(cache[com.cedarsoft.i18n.Locale.Germany])

    assertThat(cache[com.cedarsoft.i18n.Locale.Germany]).isNotEqualTo(cache[com.cedarsoft.i18n.Locale.US])

    assertThat(cache[com.cedarsoft.i18n.Locale.Germany].minimumIntegerDigits).isEqualTo(77)
    assertThat(cache[com.cedarsoft.i18n.Locale.US].minimumIntegerDigits).isEqualTo(77)
  }
}
