package it.neckar.open.formatting

import assertk.*
import assertk.assertions.*
import it.neckar.open.i18n.I18nConfiguration
import it.neckar.open.i18n.Locale
import it.neckar.open.time.TimeZone
import org.junit.jupiter.api.Test


class DateTimeFormatTest {
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
  fun testDateFormatUTC() {
    val format = DateTimeFormatUTC()
    val timestamp = 900000000000.0 // Thu Jul 09 1998 18:00:00 GMT+0200 (Central European Summer Time)
    assertThat(format.format(timestamp, I18nConfiguration.US_UTC)).isEqualTo("1998-07-09T16:00:00.000");
    assertThat(format.format(timestamp, I18nConfiguration.GermanyUTC)).isEqualTo("1998-07-09T16:00:00.000");

    assertThat(format.format(timestamp, englishBerlin)).isEqualTo("1998-07-09T16:00:00.000");
    assertThat(format.format(timestamp, I18nConfiguration.Germany)).isEqualTo("1998-07-09T16:00:00.000");

    assertThat(format.format(timestamp, englishTokyo)).isEqualTo("1998-07-09T16:00:00.000");
    assertThat(format.format(timestamp, germanTokyo)).isEqualTo("1998-07-09T16:00:00.000");
  }
}
