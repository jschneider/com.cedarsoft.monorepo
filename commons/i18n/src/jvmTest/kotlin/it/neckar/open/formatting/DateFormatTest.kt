package it.neckar.open.formatting

import assertk.*
import assertk.assertions.*
import it.neckar.open.time.toDoubleMillis
import it.neckar.open.i18n.I18nConfiguration
import it.neckar.open.i18n.Locale
import it.neckar.open.time.TimeZone
import it.neckar.open.unit.si.ms
import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 */
class DateFormatTest {
  val time: @ms Double = ZonedDateTime.of(2019, 1, 7, 19, 11, 59, 123123123, ZoneId.of("Asia/Tokyo")).toDoubleMillis()

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

  val englishNewYork: I18nConfiguration = I18nConfiguration(
    Locale.US,
    Locale.US,
    TimeZone.NewYork
  )

  @Test
  fun testSetup() {
    assertThat(time).isEqualTo(1.546855919123123E12)
  }

  @Test
  fun testIsoFormat() {
    assertThat(dateTimeFormatIso8601.format(time, germanTokyo)).isEqualTo("2019-01-07T19:11:59.123+09:00[Asia/Tokyo]")
    assertThat(dateTimeFormatIso8601.format(time, englishTokyo)).isEqualTo("2019-01-07T19:11:59.123+09:00[Asia/Tokyo]")
    assertThat(dateTimeFormatIso8601.format(time, englishNewYork)).isEqualTo("2019-01-07T05:11:59.123-05:00[America/New_York]")
  }

  @Test
  fun testYear() {
    assertThat(yearFormat.format(time, germanTokyo)).isEqualTo("2019")
    assertThat(yearFormat.format(time, englishTokyo)).isEqualTo("2019")
    assertThat(yearFormat.format(time, englishNewYork)).isEqualTo("2019")
  }

  @Test
  fun testYearMonth() {
    assertThat(yearMonthFormat.format(time, germanTokyo)).isEqualTo("Januar 2019")
    assertThat(yearMonthFormat.format(time, englishTokyo)).isEqualTo("January 2019")
    assertThat(yearMonthFormat.format(time, englishNewYork)).isEqualTo("January 2019")
  }

  @Test
  fun testIsoDate() {
    assertThat(dateFormatIso8601.format(time, germanTokyo)).isEqualTo("2019-01-07")
    assertThat(dateFormatIso8601.format(time, englishTokyo)).isEqualTo("2019-01-07")
    assertThat(dateFormatIso8601.format(time, englishNewYork)).isEqualTo("2019-01-07")
  }

  @Test
  fun testIsoTime() {
    assertThat(timeFormatIso8601.format(time, germanTokyo)).isEqualTo("19:11:59.123")
    assertThat(timeFormatIso8601.format(time, englishTokyo)).isEqualTo("19:11:59.123")
    assertThat(timeFormatIso8601.format(time, englishNewYork)).isEqualTo("05:11:59.123")
  }

  @Test
  fun testTimeFormat() {
    assertThat(timeFormat.format(time, germanTokyo)).isEqualTo("19:11:59")
    assertThat(timeFormat.format(time, englishTokyo)).isEqualTo("7:11:59 PM")
    assertThat(timeFormat.format(time, englishNewYork)).isEqualTo("5:11:59 AM")

    assertThat(timeFormatWithMillis.format(time, germanTokyo)).isEqualTo("19:11:59.123")
    assertThat(timeFormatWithMillis.format(time, englishTokyo)).isEqualTo("7:11:59.123 PM")
    assertThat(timeFormatWithMillis.format(time, englishNewYork)).isEqualTo("5:11:59.123 AM")
  }

  @Test
  internal fun testDateFormat() {
    assertThat(dateFormat.format(time, germanTokyo)).isEqualTo("07.01.2019")
    assertThat(dateFormat.format(time, englishTokyo)).isEqualTo("Jan 7, 2019")
    assertThat(dateFormat.format(time, englishNewYork)).isEqualTo("Jan 7, 2019")
  }

  @Test
  internal fun testDateTime() {
    assertThat(dateTimeFormat.format(time, germanTokyo)).isEqualTo("07.01.2019 19:11:59")
    assertThat(dateTimeFormat.format(time, englishTokyo)).isEqualTo("Jan 7, 2019 7:11:59 PM")
    assertThat(dateTimeFormat.format(time, englishNewYork)).isEqualTo("Jan 7, 2019 5:11:59 AM")

    assertThat(dateTimeFormatWithMillis.format(time, germanTokyo)).isEqualTo("07.01.2019 19:11:59.123")
    assertThat(dateTimeFormatWithMillis.format(time, englishTokyo)).isEqualTo("Jan 7, 2019 7:11:59.123 PM")
    assertThat(dateTimeFormatWithMillis.format(time, englishNewYork)).isEqualTo("Jan 7, 2019 5:11:59.123 AM")
  }
}
