package it.neckar.open.formatting

import assertk.*
import assertk.assertions.*
import it.neckar.open.i18n.I18nConfiguration
import it.neckar.open.i18n.Locale
import it.neckar.open.time.TimeZone
import it.neckar.open.unit.si.ms
import org.junit.jupiter.api.Test

/**
 *
 */
class YearMonthFormatTest {
  val englishUTC: I18nConfiguration = I18nConfiguration(
    Locale.US,
    Locale.US,
    TimeZone.UTC
  )
  val germanTokyo: I18nConfiguration = I18nConfiguration(
    Locale.Germany,
    Locale.Germany,
    TimeZone.Tokyo
  )

  val englishTokyo: I18nConfiguration = I18nConfiguration(
    Locale.US,
    Locale.US,
    TimeZone.Tokyo
  )

  val englishBerlin: I18nConfiguration = I18nConfiguration(
    Locale.US,
    Locale.US,
    TimeZone.Berlin
  )

  @Test
  fun testIt() {
    @ms val millis = 1614703978000.0
    assertThat(millis.formatUtc()).isEqualTo("2021-03-02T16:52:58.000")
    assertThat(YearMonthFormat().format(millis, I18nConfiguration.Germany)).isEqualTo("März 2021")
    assertThat(YearMonthFormat().format(millis, englishBerlin)).isEqualTo("March 2021")
    assertThat(YearMonthFormat().format(millis, englishTokyo)).isEqualTo("March 2021")
  }
}
