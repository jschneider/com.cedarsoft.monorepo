package com.cedarsoft.formatting

import assertk.*
import assertk.assertions.*
import com.cedarsoft.i18n.I18nConfiguration
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class CachedFormatterTest {
  @Test
  internal fun testCachedWithOffset() {
    val i18nConfiguration = I18nConfiguration.Germany
    var currentOffset = 17.0

    val numberFormat = OffsetFormatter(DecimalFormat(1, 1).cached(2)) { currentOffset }

    assertThat(numberFormat.currentCacheSize).isEqualTo(0)
    assertThat(numberFormat.format(1.0, i18nConfiguration)).isEqualTo("18,0")
    assertThat(numberFormat.currentCacheSize).isEqualTo(1)

    currentOffset = 10.0

    assertThat(numberFormat.format(2.0, i18nConfiguration)).isEqualTo("12,0")
    assertThat(numberFormat.currentCacheSize).isEqualTo(2)

    //Ask for the cached value again
    assertThat(numberFormat.format(1.0, i18nConfiguration)).isEqualTo("11,0")
    assertThat(numberFormat.currentCacheSize).isEqualTo(2)
  }

  @Test
  fun testIt() {
    val i18nConfiguration = I18nConfiguration.Germany
    val decimalFormat = DecimalFormat().cached(4)

    assertThat(decimalFormat.currentCacheSize).isEqualTo(0)
    assertThat(decimalFormat.format(1.0, i18nConfiguration)).isEqualTo("1")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(1)
    assertThat(decimalFormat.format(2.0, i18nConfiguration)).isEqualTo("2")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(2)
    assertThat(decimalFormat.format(3.0, i18nConfiguration)).isEqualTo("3")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(3)

    //Check the same
    assertThat(decimalFormat.format(3.0, i18nConfiguration)).isEqualTo("3")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(3)
    assertThat(decimalFormat.format(1.0, i18nConfiguration)).isEqualTo("1")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(3)

    assertThat(decimalFormat.format(4.0, i18nConfiguration)).isEqualTo("4")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(4)

    assertThat(decimalFormat.format(5.0, i18nConfiguration)).isEqualTo("5")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(4)
  }

  @Test
  fun testMiniCache() {
    val i18nConfiguration = I18nConfiguration.Germany
    val decimalFormat = DecimalFormat().cached(1)

    assertThat(decimalFormat.currentCacheSize).isEqualTo(0)
    assertThat(decimalFormat.format(1.0, i18nConfiguration)).isEqualTo("1")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(1)

    assertThat(decimalFormat.format(2.0, i18nConfiguration)).isEqualTo("2")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(1)
  }

  @Test
  fun testCache2Locales() {
    val decimalFormat = DecimalFormat(1, 1)
      .cached(4)

    assertThat(decimalFormat.currentCacheSize).isEqualTo(0)
    assertThat(decimalFormat.format(1.0, I18nConfiguration.Germany)).isEqualTo("1,0")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(1)
    assertThat(decimalFormat.format(1.0, I18nConfiguration.US)).isEqualTo("1.0")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(2)

    assertThat(decimalFormat.format(2.0, I18nConfiguration.Germany)).isEqualTo("2,0")
    assertThat(decimalFormat.format(2.0, I18nConfiguration.US)).isEqualTo("2.0")
    assertThat(decimalFormat.currentCacheSize).isEqualTo(4)
  }
}
