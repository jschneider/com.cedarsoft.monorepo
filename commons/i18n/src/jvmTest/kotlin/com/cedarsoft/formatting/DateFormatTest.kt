package com.cedarsoft.formatting

import assertk.*
import assertk.assertions.*
import com.cedarsoft.time.toDoubleMillis
import com.cedarsoft.i18n.I18nConfiguration
import com.cedarsoft.i18n.Locale
import com.cedarsoft.time.TimeZone
import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class DateFormatTest {
  val time: ZonedDateTime = ZonedDateTime.of(2019, 1, 7, 19, 11, 59, 123123123, ZoneId.of("Asia/Tokyo"))

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

  @Test
  fun testIsoFormat() {
    assertThat(dateTimeFormatIso8601.format(time.toDoubleMillis(), germanTokyo)).isEqualTo("2019-01-07T19:11:59.123+09:00[Asia/Tokyo]")
    assertThat(dateTimeFormatIso8601.format(time.toDoubleMillis(), englishTokyo)).isEqualTo("2019-01-07T19:11:59.123+09:00[Asia/Tokyo]")
  }

  @Test
  fun testIsoDate() {
    assertThat(dateFormatIso8601.format(time.toDoubleMillis(), germanTokyo)).isEqualTo("2019-01-07")
    assertThat(dateFormatIso8601.format(time.toDoubleMillis(), englishTokyo)).isEqualTo("2019-01-07")
  }

  @Test
  fun testIsoTime() {
    assertThat(timeFormatIso8601.format(time.toDoubleMillis(), germanTokyo)).isEqualTo("19:11:59.123")
    assertThat(timeFormatIso8601.format(time.toDoubleMillis(), englishTokyo)).isEqualTo("19:11:59.123")
  }

  @Test
  fun testTimeFormat() {
    assertThat(timeFormat.format(time.toDoubleMillis(), germanTokyo)).isEqualTo("19:11:59")
    assertThat(timeFormat.format(time.toDoubleMillis(), englishTokyo)).isEqualTo("7:11:59 PM")

    assertThat(timeFormatWithMillis.format(time.toDoubleMillis(), germanTokyo)).isEqualTo("19:11:59.123")
    assertThat(timeFormatWithMillis.format(time.toDoubleMillis(), englishTokyo)).isEqualTo("7:11:59.123 PM")
  }

  @Test
  internal fun testDateFormat() {
    assertThat(dateFormat.format(time.toDoubleMillis(), germanTokyo)).isEqualTo("07.01.2019")
    assertThat(dateFormat.format(time.toDoubleMillis(), englishTokyo)).isEqualTo("Jan 7, 2019")
  }

  @Test
  internal fun testDateTime() {
    assertThat(dateTimeFormat.format(time.toDoubleMillis(), germanTokyo)).isEqualTo("07.01.2019 19:11:59")
    assertThat(dateTimeFormat.format(time.toDoubleMillis(), englishTokyo)).isEqualTo("Jan 7, 2019 7:11:59 PM")

    assertThat(dateTimeFormatWithMillis.format(time.toDoubleMillis(), germanTokyo)).isEqualTo("07.01.2019 19:11:59.123")
    assertThat(dateTimeFormatWithMillis.format(time.toDoubleMillis(), englishTokyo)).isEqualTo("Jan 7, 2019 7:11:59.123 PM")
  }
}
