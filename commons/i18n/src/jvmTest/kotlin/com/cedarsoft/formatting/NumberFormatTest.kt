package com.cedarsoft.formatting

import assertk.*
import assertk.assertions.*
import com.cedarsoft.formatting.NumberFormatPatternParser.parsePattern
import com.cedarsoft.i18n.I18nConfiguration
import org.junit.jupiter.api.Test
import kotlin.text.Typography.nbsp

class NumberFormatTest {
  @Test
  fun testPercentage0Digits() {
    assertThat(percentageFormat0digits.format(1.234567, I18nConfiguration.US)).isEqualTo("123$nbsp%")
  }

  @Test
  internal fun testDefaultLocaleEnglish() {
    val format = DecimalFormat(maximumFractionDigits = 1)
    assertThat(format.format(1.23, I18nConfiguration.US)).isEqualTo("1.2")
  }

  @Test
  internal fun testDefaultLocaleGerman() {
    val format = DecimalFormat(maximumFractionDigits = 1)
    assertThat(format.format(1.23, I18nConfiguration.Germany)).isEqualTo("1,2")
  }

  @Test
  fun testParseEmptyPattern() {
    val numberFormat1 = parsePattern("")
    assertThat(numberFormat1).isNotNull()
    assertThat(numberFormat1.useGrouping).isFalse()
    assertThat(numberFormat1.minimumIntegerDigits).isEqualTo(1)
    assertThat(numberFormat1.minimumFractionDigits).isEqualTo(0)
    assertThat(numberFormat1.maximumFractionDigits).isEqualTo(0)
  }

  @Test
  fun testParsePattern() {
    parsePattern("#").also {
      assertThat(it).isNotNull()
      assertThat(it.useGrouping).isFalse()
      assertThat(it.minimumIntegerDigits).isEqualTo(1)
      assertThat(it.minimumFractionDigits).isEqualTo(0)
      assertThat(it.maximumFractionDigits).isEqualTo(0)
    }
    parsePattern("0").also {
      assertThat(it).isNotNull()
      assertThat(it.useGrouping).isFalse()
      assertThat(it.minimumIntegerDigits).isEqualTo(1)
      assertThat(it.minimumFractionDigits).isEqualTo(0)
      assertThat(it.maximumFractionDigits).isEqualTo(0)
    }
    parsePattern("00.0").also {
      assertThat(it).isNotNull()
      assertThat(it.useGrouping).isFalse()
      assertThat(it.minimumIntegerDigits).isEqualTo(2)
      assertThat(it.minimumFractionDigits).isEqualTo(1)
      assertThat(it.maximumFractionDigits).isEqualTo(1)
    }
    parsePattern("#,0.00#").also {
      assertThat(it).isNotNull()
      assertThat(it.useGrouping).isTrue()
      assertThat(it.minimumIntegerDigits).isEqualTo(1)
      assertThat(it.minimumFractionDigits).isEqualTo(2)
      assertThat(it.maximumFractionDigits).isEqualTo(3)
    }
  }

}
