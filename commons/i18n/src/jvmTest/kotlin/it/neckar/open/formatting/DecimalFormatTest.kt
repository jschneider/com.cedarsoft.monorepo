package it.neckar.open.formatting

import assertk.*
import assertk.assertions.*
import it.neckar.open.i18n.I18nConfiguration
import org.junit.jupiter.api.Test

internal class DecimalFormatTest {
  @Test
  fun testMinus0() {
    assertThat(DecimalFormat(3, 2, 1).format(-0.0, I18nConfiguration.US)).isEqualTo("0.00")
    assertThat(DecimalFormat(3, 0, 1).format(-0.0, I18nConfiguration.US)).isEqualTo("0")

    //??? Is this what we want? Maybe rounding should be preferred?
    assertThat(DecimalFormat(3, 2, 1).format(-0.00000000000001, I18nConfiguration.US)).isEqualTo("-0.00")
    assertThat(DecimalFormat(3, 0, 1).format(-0.00000000000001, I18nConfiguration.US)).isEqualTo("-0")
  }

  @Test
  fun testNegativeValues() {
    assertThat(DecimalFormat(3, 2, 1).format(-10.0, I18nConfiguration.US)).isEqualTo("-10.00")
    assertThat(DecimalFormat(3, 2, 1).format(-50.0, I18nConfiguration.US)).isEqualTo("-50.00")
  }

  @Test
  fun testNegativeInt() {
    assertThat(intFormat.format(-10.0, I18nConfiguration.US)).isEqualTo("-10")
    assertThat(intFormat.format(-50.0, I18nConfiguration.US)).isEqualTo("-50")
  }

  @Test
  internal fun testMinusZero() {
    assertThat(DecimalFormat(3, 2, 1).format(-0.0, I18nConfiguration.US)).isEqualTo("0.00")
    assertThat(DecimalFormat(3, 2, 1).format(-0.00001, I18nConfiguration.US)).isEqualTo("-0.00")
  }

  @Test
  fun testNumberFormatBugExponentialWith0MinimumIntegerDigits() {
    assertThat(ExponentialFormat(3, 2, 1).format(123456789.0, I18nConfiguration.US)).isEqualTo("1.235e8")

    //TODO does this make sense?
    assertThat(ExponentialFormat(3, 2, 0).format(123456789.0, I18nConfiguration.US)).isEqualTo(".123e9")
  }

  @Test
  internal fun testSimple() {
    val de = I18nConfiguration.Germany.copy(formatLocale = it.neckar.open.i18n.Locale("de"))
    assertThat(DecimalFormat(3, 2, 0, true).format(1.23, de)).isEqualTo("1,23")
    assertThat(DecimalFormat(3, 2, 0).format(1.234, de)).isEqualTo("1,234")
    assertThat(DecimalFormat(3, 2, 0).format(1.237, de)).isEqualTo("1,237")
    assertThat(DecimalFormat(3, 2, 0).format(1.2371, de)).isEqualTo("1,237")
    assertThat(DecimalFormat(3, 2, 0).format(1.2379, de)).isEqualTo("1,238")

    val deDe = I18nConfiguration.Germany.copy(formatLocale = it.neckar.open.i18n.Locale("de-DE"))
    assertThat(DecimalFormat(3, 2, 0, true).format(1.23, deDe)).isEqualTo("1,23")
    assertThat(DecimalFormat(3, 2, 0).format(1.234, deDe)).isEqualTo("1,234")
    assertThat(DecimalFormat(3, 2, 0).format(1.237, deDe)).isEqualTo("1,237")
    assertThat(DecimalFormat(3, 2, 0).format(1.2371, deDe)).isEqualTo("1,237")
    assertThat(DecimalFormat(3, 2, 0).format(1.2379, deDe)).isEqualTo("1,238")
  }

  @Test
  internal fun testExponential2() {
    assertThat(ExponentialFormat(3, 2, 1).format(123456789.0, I18nConfiguration.US)).isEqualTo("1.235e8")
    assertThat(ExponentialFormat(3, 2, 1).format(123456789.0, I18nConfiguration.Germany)).isEqualTo("1,235e8")
  }

  @Test
  fun testExponentialFormat() {
    assertThat(ExponentialFormat.createExponentialFormat(2, 2, 1, it.neckar.open.i18n.Locale.US).format(123456.0)).isEqualTo("1.23e5")
    assertThat(ExponentialFormat.createExponentialFormat(2, 2, 1, it.neckar.open.i18n.Locale.Germany).format(123456.0)).isEqualTo("1,23e5")
    assertThat(ExponentialFormat.createExponentialFormat(2, 2, 1, it.neckar.open.i18n.Locale.US).format(123456.78)).isEqualTo("1.23e5")
    assertThat(ExponentialFormat.createExponentialFormat(3, 2, 1, it.neckar.open.i18n.Locale.US).format(123.78)).isEqualTo("1.238e2")
    assertThat(ExponentialFormat.createExponentialFormat(3, 2, 1, it.neckar.open.i18n.Locale.US).format(-123.78)).isEqualTo("-1.238e2")
    assertThat(ExponentialFormat.createExponentialFormat(3, 2, 3, it.neckar.open.i18n.Locale.US).format(1234.78)).isEqualTo("123.478e1")
    assertThat(ExponentialFormat.createExponentialFormat(3, 2, 1, it.neckar.open.i18n.Locale.US).format(1.1)).isEqualTo("1.10e0")
    assertThat(ExponentialFormat.createExponentialFormat(3, 2, 1, it.neckar.open.i18n.Locale.US).format(10.1)).isEqualTo("1.01e1")
    assertThat(ExponentialFormat.createExponentialFormat(3, 2, 1, it.neckar.open.i18n.Locale.US).format(-0.0)).isEqualTo("-0.00e0")
    assertThat(ExponentialFormat.createExponentialFormat(3, 2, 1, it.neckar.open.i18n.Locale.US).format(0.0)).isEqualTo("0.00e0")
    assertThat(ExponentialFormat.createExponentialFormat(2, 2, 1, it.neckar.open.i18n.Locale.US).format(0.000345)).isEqualTo("3.45e-4")
    assertThat(ExponentialFormat.createExponentialFormat(1, 1, 1, it.neckar.open.i18n.Locale.US).format(0.000348)).isEqualTo("3.5e-4")
    assertThat(ExponentialFormat.createExponentialFormat(3, 3, 2, it.neckar.open.i18n.Locale.US).format(0.000348)).isEqualTo("34.800e-5")
  }

  @Test
  fun testPrecision() {
    assertThat(DecimalFormat(0, 1, 2, true).precision).isEqualTo(1.0)
    assertThat(DecimalFormat(1, 1, 2, true).precision).isEqualTo(0.1)
    assertThat(DecimalFormat(2, 1, 2, true).precision).isEqualTo(0.01)
    assertThat(DecimalFormat(3, 1, 2, true).precision).isEqualTo(0.001)
  }
}
